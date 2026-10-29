import { useEffect, useState } from 'react'
import { Link, useNavigate, useParams } from 'react-router-dom'
import api, { getApiErrorMessage } from '../services/api.js'

const EMPTY_FORM = {
  titulo: '',
  imagenPrincipalUrl: '',
  puntaje: '',
  descripcionCard: '',
  disponible: true,
  categoriaId: '',
  ciudadId: '',
  ubicacion: {
    id: null,
    direccion: '',
    descripcion: '',
    latitud: '',
    longitud: '',
  },
  descripcion: {
    id: null,
    descripcion: '',
  },
  caracteristicas: [],
  imagenesSecundarias: [],
  politica: [],
}

const FEATURE_FIELDS = [
  { name: 'nombre', label: 'Nombre' },
  { name: 'urlIcono', label: 'URL del ícono', type: 'url' },
]

const IMAGE_FIELDS = [
  { name: 'titulo', label: 'Título' },
  { name: 'urlImagen', label: 'URL de imagen', type: 'url' },
]

const POLICY_FIELDS = [
  { name: 'tipoPolitica', label: 'Tipo de política' },
  { name: 'titulo', label: 'Título' },
  { name: 'descripcion', label: 'Descripción', textarea: true },
]

function valueOrEmpty(value) {
  return value ?? ''
}

function productToForm(product) {
  const description = product.descripciones?.[0]
  return {
    titulo: valueOrEmpty(product.titulo),
    imagenPrincipalUrl: valueOrEmpty(product.imagenPrincipalUrl),
    puntaje: valueOrEmpty(product.puntaje),
    descripcionCard: valueOrEmpty(product.descripcionCard),
    disponible: Boolean(product.disponible),
    categoriaId: String(product.categoria?.id ?? ''),
    ciudadId: String(product.ciudad?.id ?? ''),
    ubicacion: {
      id: product.ubicacion?.id ?? null,
      direccion: valueOrEmpty(product.ubicacion?.direccion),
      descripcion: valueOrEmpty(product.ubicacion?.descripcion),
      latitud: valueOrEmpty(product.ubicacion?.latitud),
      longitud: valueOrEmpty(product.ubicacion?.longitud),
    },
    descripcion: {
      id: description?.id ?? null,
      descripcion: valueOrEmpty(description?.descripcion),
    },
    caracteristicas: product.caracteristicas?.map((item) => ({ ...item })) ?? [],
    imagenesSecundarias: product.imagenesSecundarias?.map((item) => ({ ...item })) ?? [],
    politica: product.politica?.map((item) => ({ ...item })) ?? [],
  }
}

function trimItem(item, fields) {
  const result = Object.fromEntries(fields.map((field) => [field.name, String(item[field.name] ?? '').trim()]))
  if (item.id) result.id = item.id
  return result
}

function RepeatableFields({ title, addLabel, items, fields, onChange }) {
  function addItem() {
    onChange([...items, Object.fromEntries(fields.map((field) => [field.name, '']))])
  }

  function updateItem(index, field, value) {
    onChange(items.map((item, itemIndex) => (
      itemIndex === index ? { ...item, [field]: value } : item
    )))
  }

  return (
    <section className="admin-repeatable">
      <div className="admin-repeatable-heading">
        <h2>{title}</h2>
        <button className="admin-secondary-button" type="button" onClick={addItem}>
          {addLabel}
        </button>
      </div>
      {items.length === 0 && <p className="admin-empty">No se agregaron elementos.</p>}
      {items.map((item, index) => (
        <div className="admin-repeatable-row" key={item.id ?? index}>
          <div className="admin-form-grid">
            {fields.map((field) => (
              <label className={field.textarea ? 'admin-field wide' : 'admin-field'} key={field.name}>
                <span>{field.label}</span>
                {field.textarea ? (
                  <textarea
                    value={item[field.name] ?? ''}
                    onChange={(event) => updateItem(index, field.name, event.target.value)}
                    required
                  />
                ) : (
                  <input
                    type={field.type || 'text'}
                    value={item[field.name] ?? ''}
                    onChange={(event) => updateItem(index, field.name, event.target.value)}
                    required
                  />
                )}
              </label>
            ))}
          </div>
          <button
            className="admin-remove-button"
            type="button"
            onClick={() => onChange(items.filter((_, itemIndex) => itemIndex !== index))}
          >
            Quitar
          </button>
        </div>
      ))}
    </section>
  )
}

function AdminProductForm() {
  const { id } = useParams()
  const isEditing = Boolean(id)
  const navigate = useNavigate()
  const [form, setForm] = useState(EMPTY_FORM)
  const [categories, setCategories] = useState([])
  const [cities, setCities] = useState([])
  const [status, setStatus] = useState('loading')
  const [feedback, setFeedback] = useState(null)
  const [submitting, setSubmitting] = useState(false)

  useEffect(() => {
    const controller = new AbortController()

    async function loadFormData() {
      setStatus('loading')
      try {
        const requests = [
          api.get('/categorias', { signal: controller.signal }),
          api.get('/ciudades', { signal: controller.signal }),
        ]
        if (isEditing) {
          requests.push(api.get(`/productos/${encodeURIComponent(id)}`, { signal: controller.signal }))
        }

        const [categoriesResponse, citiesResponse, productResponse] = await Promise.all(requests)
        setCategories(categoriesResponse.data)
        setCities(citiesResponse.data)
        setForm(productResponse ? productToForm(productResponse.data) : EMPTY_FORM)
        setStatus('success')
      } catch (error) {
        if (!controller.signal.aborted) {
          setFeedback({
            type: 'error',
            message: getApiErrorMessage(error, 'No se pudieron cargar los datos del formulario.'),
          })
          setStatus('error')
        }
      }
    }

    loadFormData()
    return () => controller.abort()
  }, [id, isEditing])

  function updateField(field, value) {
    setForm((current) => ({ ...current, [field]: value }))
    setFeedback(null)
  }

  function updateLocation(field, value) {
    setForm((current) => ({
      ...current,
      ubicacion: { ...current.ubicacion, [field]: value },
    }))
    setFeedback(null)
  }

  function validateForm() {
    const required = [
      form.titulo,
      form.imagenPrincipalUrl,
      form.puntaje,
      form.descripcionCard,
      form.categoriaId,
      form.ciudadId,
      form.ubicacion.direccion,
      form.ubicacion.descripcion,
      form.ubicacion.latitud,
      form.ubicacion.longitud,
      form.descripcion.descripcion,
    ]
    if (required.some((value) => !String(value).trim())) return 'Completá todos los campos obligatorios.'

    const latitude = Number(form.ubicacion.latitud)
    const longitude = Number(form.ubicacion.longitud)
    if (!Number.isFinite(latitude) || latitude < -90 || latitude > 90) {
      return 'La latitud debe ser un número entre -90 y 90.'
    }
    if (!Number.isFinite(longitude) || longitude < -180 || longitude > 180) {
      return 'La longitud debe ser un número entre -180 y 180.'
    }

    const groups = [
      [form.caracteristicas, FEATURE_FIELDS, 'características'],
      [form.imagenesSecundarias, IMAGE_FIELDS, 'imágenes secundarias'],
      [form.politica, POLICY_FIELDS, 'políticas'],
    ]
    for (const [items, fields, name] of groups) {
      const incomplete = items.some((item) => fields.some((field) => !String(item[field.name] ?? '').trim()))
      if (incomplete) return `Completá todos los campos de ${name} o quitá la fila incompleta.`
    }

    return null
  }

  function buildProductBody() {
    const locationBody = {
      descripcion: form.ubicacion.descripcion.trim(),
      latitud: Number(form.ubicacion.latitud),
      longitud: Number(form.ubicacion.longitud),
      direccion: form.ubicacion.direccion.trim(),
    }
    if (form.ubicacion.id) locationBody.id = form.ubicacion.id

    const descriptionBody = { descripcion: form.descripcion.descripcion.trim() }
    if (form.descripcion.id) descriptionBody.id = form.descripcion.id

    return {
      titulo: form.titulo.trim(),
      imagenPrincipalUrl: form.imagenPrincipalUrl.trim(),
      puntaje: form.puntaje.trim(),
      descripciones: [descriptionBody],
      descripcionCard: form.descripcionCard.trim(),
      disponible: form.disponible,
      politica: form.politica.map((item) => trimItem(item, POLICY_FIELDS)),
      categoria: { id: Number(form.categoriaId) },
      ubicacion: locationBody,
      caracteristicas: form.caracteristicas.map((item) => trimItem(item, FEATURE_FIELDS)),
      imagenesSecundarias: form.imagenesSecundarias.map((item) => trimItem(item, IMAGE_FIELDS)),
      ciudad: { id: Number(form.ciudadId) },
    }
  }

  async function handleSubmit(event) {
    event.preventDefault()
    const validationMessage = validateForm()
    if (validationMessage) {
      setFeedback({ type: 'error', message: validationMessage })
      return
    }

    setSubmitting(true)
    setFeedback(null)
    try {
      const body = buildProductBody()
      if (isEditing) {
        const { data } = await api.put(`/productos/${id}`, body)
        setForm(productToForm(data))
        setFeedback({ type: 'success', message: 'Propiedad actualizada correctamente.' })
      } else {
        const { data } = await api.post('/productos', body)
        setFeedback({ type: 'success', message: 'Propiedad creada correctamente.' })
        navigate(`/administracion/productos/${data.id}/editar`, { replace: true })
      }
    } catch (error) {
      setFeedback({
        type: 'error',
        message: getApiErrorMessage(error, 'No se pudo guardar la propiedad.'),
      })
    } finally {
      setSubmitting(false)
    }
  }

  if (status === 'loading') return <p role="status">Cargando formulario de propiedad...</p>

  if (status === 'error') {
    return (
      <section className="admin-state" role="alert">
        <h1>No pudimos abrir el formulario</h1>
        {feedback && <p>{feedback.message}</p>}
        <Link className="admin-secondary-button" to="/administracion">Volver al panel</Link>
      </section>
    )
  }

  return (
    <div className="admin-product-form-page">
      <Link className="page-link admin-back" to="/administracion">Volver a administración</Link>
      <header className="admin-form-heading">
        <span className="admin-eyebrow">Propiedades</span>
        <h1>{isEditing ? 'Editar propiedad' : 'Nueva propiedad'}</h1>
        <p>Completá la información que se mostrará en Mini Booking.</p>
      </header>

      {feedback && (
        <p className={`admin-feedback ${feedback.type}`} role={feedback.type === 'error' ? 'alert' : 'status'}>
          {feedback.message}
        </p>
      )}

      <form className="admin-product-form" onSubmit={handleSubmit}>
        <section className="admin-form-section">
          <h2>Datos principales</h2>
          <div className="admin-form-grid">
            <label className="admin-field wide">
              <span>Título</span>
              <input
                value={form.titulo}
                maxLength="150"
                onChange={(event) => updateField('titulo', event.target.value)}
                required
              />
            </label>
            <label className="admin-field wide">
              <span>URL de imagen principal</span>
              <input
                type="url"
                value={form.imagenPrincipalUrl}
                maxLength="1024"
                onChange={(event) => updateField('imagenPrincipalUrl', event.target.value)}
                required
              />
            </label>
            <label className="admin-field">
              <span>Puntaje</span>
              <input
                value={form.puntaje}
                maxLength="10"
                placeholder="4.8"
                onChange={(event) => updateField('puntaje', event.target.value)}
                required
              />
            </label>
            <label className="admin-field">
              <span>Categoría</span>
              <select
                value={form.categoriaId}
                onChange={(event) => updateField('categoriaId', event.target.value)}
                required
              >
                <option value="">Seleccionar categoría</option>
                {categories.map((category) => (
                  <option value={category.id} key={category.id}>{category.titulo}</option>
                ))}
              </select>
            </label>
            <label className="admin-field">
              <span>Ciudad</span>
              <select
                value={form.ciudadId}
                onChange={(event) => updateField('ciudadId', event.target.value)}
                required
              >
                <option value="">Seleccionar ciudad</option>
                {cities.map((city) => (
                  <option value={city.id} key={city.id}>{city.nombre}</option>
                ))}
              </select>
            </label>
            <label className="admin-checkbox">
              <input
                type="checkbox"
                checked={form.disponible}
                onChange={(event) => updateField('disponible', event.target.checked)}
              />
              <span>Propiedad disponible</span>
            </label>
            <label className="admin-field wide">
              <span>Descripción corta para la card</span>
              <textarea
                value={form.descripcionCard}
                maxLength="500"
                onChange={(event) => updateField('descripcionCard', event.target.value)}
                required
              />
            </label>
            <label className="admin-field wide">
              <span>Descripción completa</span>
              <textarea
                value={form.descripcion.descripcion}
                maxLength="2000"
                onChange={(event) => setForm((current) => ({
                  ...current,
                  descripcion: { ...current.descripcion, descripcion: event.target.value },
                }))}
                required
              />
            </label>
          </div>
        </section>

        <section className="admin-form-section">
          <h2>Ubicación</h2>
          <div className="admin-form-grid">
            <label className="admin-field wide">
              <span>Dirección</span>
              <input
                value={form.ubicacion.direccion}
                onChange={(event) => updateLocation('direccion', event.target.value)}
                required
              />
            </label>
            <label className="admin-field wide">
              <span>Descripción de ubicación</span>
              <input
                value={form.ubicacion.descripcion}
                onChange={(event) => updateLocation('descripcion', event.target.value)}
                required
              />
            </label>
            <label className="admin-field">
              <span>Latitud</span>
              <input
                type="number"
                step="any"
                min="-90"
                max="90"
                value={form.ubicacion.latitud}
                onChange={(event) => updateLocation('latitud', event.target.value)}
                required
              />
            </label>
            <label className="admin-field">
              <span>Longitud</span>
              <input
                type="number"
                step="any"
                min="-180"
                max="180"
                value={form.ubicacion.longitud}
                onChange={(event) => updateLocation('longitud', event.target.value)}
                required
              />
            </label>
          </div>
        </section>

        <RepeatableFields
          title="Características"
          addLabel="Agregar característica"
          items={form.caracteristicas}
          fields={FEATURE_FIELDS}
          onChange={(items) => updateField('caracteristicas', items)}
        />
        <RepeatableFields
          title="Imágenes secundarias"
          addLabel="Agregar imagen"
          items={form.imagenesSecundarias}
          fields={IMAGE_FIELDS}
          onChange={(items) => updateField('imagenesSecundarias', items)}
        />
        <RepeatableFields
          title="Políticas"
          addLabel="Agregar política"
          items={form.politica}
          fields={POLICY_FIELDS}
          onChange={(items) => updateField('politica', items)}
        />

        <div className="admin-submit-bar">
          <Link className="admin-secondary-button" to="/administracion">Cancelar</Link>
          <button className="admin-primary-button" type="submit" disabled={submitting}>
            {submitting ? 'Guardando...' : isEditing ? 'Guardar cambios' : 'Crear propiedad'}
          </button>
        </div>
      </form>
    </div>
  )
}

export default AdminProductForm

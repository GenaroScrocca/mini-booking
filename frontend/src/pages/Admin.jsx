import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import ProductAdminList from '../components/admin/ProductAdminList.jsx'
import SimpleEntityManager from '../components/admin/SimpleEntityManager.jsx'
import api, { getApiErrorMessage } from '../services/api.js'

const CATEGORY_FIELDS = [
  { name: 'titulo', label: 'Título', maxLength: 100 },
  { name: 'descripcion', label: 'Descripción', maxLength: 255, wide: true },
  { name: 'urlImagen', label: 'URL de imagen', type: 'url', maxLength: 1024, wide: true },
]

const CITY_FIELDS = [
  { name: 'nombre', label: 'Nombre', maxLength: 100 },
  { name: 'provincia', label: 'Provincia', maxLength: 100 },
  { name: 'pais', label: 'País', maxLength: 100 },
]

function upsert(items, savedItem) {
  const exists = items.some((item) => item.id === savedItem.id)
  return exists
    ? items.map((item) => (item.id === savedItem.id ? savedItem : item))
    : [...items, savedItem]
}

function Admin() {
  const [section, setSection] = useState('resumen')
  const [products, setProducts] = useState([])
  const [categories, setCategories] = useState([])
  const [cities, setCities] = useState([])
  const [status, setStatus] = useState('loading')
  const [retryCount, setRetryCount] = useState(0)
  const [feedback, setFeedback] = useState(null)
  const [deletingProductId, setDeletingProductId] = useState(null)

  useEffect(() => {
    const controller = new AbortController()

    async function loadAdminData() {
      try {
        const [productsResponse, categoriesResponse, citiesResponse] = await Promise.all([
          api.get('/productos', { signal: controller.signal }),
          api.get('/categorias', { signal: controller.signal }),
          api.get('/ciudades', { signal: controller.signal }),
        ])
        setProducts(productsResponse.data)
        setCategories(categoriesResponse.data)
        setCities(citiesResponse.data)
        setStatus('success')
      } catch {
        if (!controller.signal.aborted) setStatus('error')
      }
    }

    loadAdminData()
    return () => controller.abort()
  }, [retryCount])

  async function deleteProduct(product) {
    if (!window.confirm(`¿Eliminar la propiedad "${product.titulo}"?`)) return

    setDeletingProductId(product.id)
    setFeedback(null)
    try {
      await api.delete(`/productos/${product.id}`)
      setProducts((current) => current.filter((item) => item.id !== product.id))
      setFeedback({ type: 'success', message: 'Propiedad eliminada correctamente.' })
    } catch (error) {
      setFeedback({
        type: 'error',
        message: getApiErrorMessage(
          error,
          'No se pudo eliminar la propiedad. Verificá que no tenga reservas asociadas.',
        ),
      })
    } finally {
      setDeletingProductId(null)
    }
  }

  if (status === 'loading') {
    return <p role="status">Cargando panel de administración...</p>
  }

  if (status === 'error') {
    return (
      <section className="admin-state" role="alert">
        <h1>No pudimos cargar el panel</h1>
        <p>Revisá la conexión con el servidor e intentá nuevamente.</p>
        <button
          className="retry-button"
          type="button"
          onClick={() => {
            setStatus('loading')
            setRetryCount((count) => count + 1)
          }}
        >
          Reintentar
        </button>
      </section>
    )
  }

  return (
    <div className="admin-page">
      <header className="admin-heading">
        <div>
          <span className="admin-eyebrow">Panel administrativo</span>
          <h1>Administración</h1>
          <p>Gestioná propiedades, categorías y ciudades de Mini Booking.</p>
        </div>
        <Link className="admin-primary-button" to="/administracion/productos/nuevo">
          Nueva propiedad
        </Link>
      </header>

      <nav className="admin-tabs" aria-label="Secciones de administración">
        {[
          ['resumen', 'Resumen'],
          ['propiedades', 'Propiedades'],
          ['categorias', 'Categorías'],
          ['ciudades', 'Ciudades'],
        ].map(([value, label]) => (
          <button
            className={section === value ? 'active' : ''}
            type="button"
            key={value}
            onClick={() => {
              setSection(value)
              setFeedback(null)
            }}
          >
            {label}
          </button>
        ))}
      </nav>

      {feedback && (
        <p className={`admin-feedback ${feedback.type}`} role={feedback.type === 'error' ? 'alert' : 'status'}>
          {feedback.message}
        </p>
      )}

      {section === 'resumen' && (
        <section className="admin-overview">
          <h2>Resumen general</h2>
          <div className="admin-metrics">
            <button type="button" onClick={() => setSection('propiedades')}>
              <span>Propiedades</span>
              <strong>{products.length}</strong>
            </button>
            <button type="button" onClick={() => setSection('categorias')}>
              <span>Categorías</span>
              <strong>{categories.length}</strong>
            </button>
            <button type="button" onClick={() => setSection('ciudades')}>
              <span>Ciudades</span>
              <strong>{cities.length}</strong>
            </button>
          </div>
        </section>
      )}

      {section === 'propiedades' && (
        <section className="admin-management">
          <div className="admin-section-heading">
            <div>
              <h2>Propiedades</h2>
              <p>Revisá la disponibilidad y mantené actualizados los alojamientos.</p>
            </div>
            <Link className="admin-primary-button" to="/administracion/productos/nuevo">
              Nueva propiedad
            </Link>
          </div>
          <ProductAdminList
            products={products}
            deletingId={deletingProductId}
            onDelete={deleteProduct}
          />
        </section>
      )}

      {section === 'categorias' && (
        <SimpleEntityManager
          title="Categorías"
          singular="Categoría"
          endpoint="/categorias"
          items={categories}
          fields={CATEGORY_FIELDS}
          onSaved={(item) => setCategories((current) => upsert(current, item))}
          onDeleted={(id) => setCategories((current) => current.filter((item) => item.id !== id))}
        />
      )}

      {section === 'ciudades' && (
        <SimpleEntityManager
          title="Ciudades"
          singular="Ciudad"
          endpoint="/ciudades"
          items={cities}
          fields={CITY_FIELDS}
          onSaved={(item) => setCities((current) => upsert(current, item))}
          onDeleted={(id) => setCities((current) => current.filter((item) => item.id !== id))}
        />
      )}
    </div>
  )
}

export default Admin

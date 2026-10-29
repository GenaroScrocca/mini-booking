import { useState } from 'react'
import api, { getApiErrorMessage } from '../../services/api.js'

function createEmptyDraft(fields) {
  return Object.fromEntries(fields.map((field) => [field.name, '']))
}

function SimpleEntityManager({ title, singular, endpoint, items, fields, onSaved, onDeleted }) {
  const [editingId, setEditingId] = useState(null)
  const [draft, setDraft] = useState(() => createEmptyDraft(fields))
  const [feedback, setFeedback] = useState(null)
  const [submitting, setSubmitting] = useState(false)
  const [deletingId, setDeletingId] = useState(null)

  function resetForm() {
    setEditingId(null)
    setDraft(createEmptyDraft(fields))
  }

  function startEditing(item) {
    setEditingId(item.id)
    setDraft(Object.fromEntries(fields.map((field) => [field.name, item[field.name] ?? ''])))
    setFeedback(null)
  }

  async function handleSubmit(event) {
    event.preventDefault()
    const body = Object.fromEntries(
      fields.map((field) => [field.name, String(draft[field.name] ?? '').trim()]),
    )

    if (Object.values(body).some((value) => !value)) {
      setFeedback({ type: 'error', message: 'Completá todos los campos.' })
      return
    }

    setSubmitting(true)
    setFeedback(null)
    try {
      const { data } = editingId
        ? await api.put(`${endpoint}/${editingId}`, body)
        : await api.post(endpoint, body)
      onSaved(data)
      setFeedback({
        type: 'success',
        message: `${singular} ${editingId ? 'actualizada' : 'creada'} correctamente.`,
      })
      resetForm()
    } catch (error) {
      setFeedback({
        type: 'error',
        message: getApiErrorMessage(error, `No se pudo guardar la ${singular.toLowerCase()}.`),
      })
    } finally {
      setSubmitting(false)
    }
  }

  async function handleDelete(item) {
    if (!window.confirm(`¿Eliminar ${singular.toLowerCase()} "${item[fields[0].name]}"?`)) return

    setDeletingId(item.id)
    setFeedback(null)
    try {
      await api.delete(`${endpoint}/${item.id}`)
      onDeleted(item.id)
      if (editingId === item.id) resetForm()
      setFeedback({ type: 'success', message: `${singular} eliminada correctamente.` })
    } catch (error) {
      setFeedback({
        type: 'error',
        message: getApiErrorMessage(
          error,
          `No se pudo eliminar la ${singular.toLowerCase()}. Verificá que no esté en uso.`,
        ),
      })
    } finally {
      setDeletingId(null)
    }
  }

  return (
    <section className="admin-management">
      <div className="admin-section-heading">
        <div>
          <h2>{title}</h2>
          <p>Creá, editá o eliminá los datos disponibles.</p>
        </div>
      </div>

      {feedback && (
        <p className={`admin-feedback ${feedback.type}`} role={feedback.type === 'error' ? 'alert' : 'status'}>
          {feedback.message}
        </p>
      )}

      <form className="admin-inline-form" onSubmit={handleSubmit}>
        <h3>{editingId ? `Editar ${singular.toLowerCase()}` : `Nueva ${singular.toLowerCase()}`}</h3>
        <div className="admin-form-grid">
          {fields.map((field) => (
            <label className={field.wide ? 'admin-field wide' : 'admin-field'} key={field.name}>
              <span>{field.label}</span>
              <input
                type={field.type || 'text'}
                value={draft[field.name]}
                maxLength={field.maxLength}
                onChange={(event) => setDraft((current) => ({
                  ...current,
                  [field.name]: event.target.value,
                }))}
                required
              />
            </label>
          ))}
        </div>
        <div className="admin-form-actions">
          <button className="admin-primary-button" type="submit" disabled={submitting}>
            {submitting ? 'Guardando...' : editingId ? 'Guardar cambios' : `Crear ${singular.toLowerCase()}`}
          </button>
          {editingId && (
            <button className="admin-secondary-button" type="button" onClick={resetForm}>
              Cancelar
            </button>
          )}
        </div>
      </form>

      <div className="admin-simple-list">
        {items.length === 0 && <p className="admin-empty">No hay registros cargados.</p>}
        {items.map((item) => (
          <article className="admin-simple-row" key={item.id}>
            <div>
              <h3>{item[fields[0].name]}</h3>
              <p>{fields.slice(1).map((field) => item[field.name]).filter(Boolean).join(' · ')}</p>
            </div>
            <div className="admin-row-actions">
              <button className="admin-secondary-button" type="button" onClick={() => startEditing(item)}>
                Editar
              </button>
              <button
                className="admin-danger-button"
                type="button"
                disabled={deletingId === item.id}
                onClick={() => handleDelete(item)}
              >
                {deletingId === item.id ? 'Eliminando...' : 'Eliminar'}
              </button>
            </div>
          </article>
        ))}
      </div>
    </section>
  )
}

export default SimpleEntityManager

import { useEffect, useState } from 'react'
import { Link, Navigate } from 'react-router-dom'
import { useAuth } from '../context/auth.js'
import api, { getApiErrorMessage } from '../services/api.js'

const dateFormatter = new Intl.DateTimeFormat('es-AR', {
  day: '2-digit',
  month: 'short',
  year: 'numeric',
  timeZone: 'UTC',
})

function formatDate(date) {
  return dateFormatter.format(new Date(`${date}T00:00:00Z`))
}

function ReservationEditor({ reservation, saving, onCancel, onSave }) {
  const [fechaInicio, setFechaInicio] = useState(reservation.fechaInicio)
  const [fechaFin, setFechaFin] = useState(reservation.fechaFin)
  const [validationError, setValidationError] = useState('')

  function handleSubmit(event) {
    event.preventDefault()
    setValidationError('')

    if (!fechaInicio || !fechaFin) {
      setValidationError('Las fechas de ingreso y salida son obligatorias.')
      return
    }

    if (fechaFin <= fechaInicio) {
      setValidationError('La fecha de salida debe ser posterior a la fecha de ingreso.')
      return
    }

    onSave(fechaInicio, fechaFin)
  }

  return (
    <form className="reservation-edit-form" onSubmit={handleSubmit} noValidate>
      <div className="reservation-edit-fields">
        <label>
          Fecha de ingreso
          <input
            type="date"
            value={fechaInicio}
            onChange={(event) => setFechaInicio(event.target.value)}
            disabled={saving}
            required
          />
        </label>
        <label>
          Fecha de salida
          <input
            type="date"
            value={fechaFin}
            min={fechaInicio || undefined}
            onChange={(event) => setFechaFin(event.target.value)}
            disabled={saving}
            required
          />
        </label>
      </div>

      {validationError && <p className="reservation-operation-message error" role="alert">{validationError}</p>}

      <div className="reservation-edit-actions">
        <button className="reservation-action-button primary" type="submit" disabled={saving}>
          {saving ? 'Guardando...' : 'Guardar cambios'}
        </button>
        <button
          className="reservation-action-button secondary"
          type="button"
          onClick={onCancel}
          disabled={saving}
        >
          Cancelar edición
        </button>
      </div>
    </form>
  )
}

function ReservationCard({
  reservation,
  editing,
  saving,
  deleting,
  actionsDisabled,
  onStartEdit,
  onCancelEdit,
  onSave,
  onDelete,
}) {
  const [imageError, setImageError] = useState(false)
  const product = reservation.producto

  return (
    <article className="my-reservation-card">
      <div className="my-reservation-image">
        {product?.imagenPrincipalUrl && !imageError ? (
          <img
            src={product.imagenPrincipalUrl}
            alt={product.titulo || 'Alojamiento reservado'}
            loading="lazy"
            onError={() => setImageError(true)}
          />
        ) : (
          <span>Imagen no disponible</span>
        )}
      </div>
      <div className="my-reservation-body">
        <h2>{product?.titulo || 'Alojamiento no disponible'}</h2>
        {product?.ciudad?.nombre && <p className="my-reservation-city">{product.ciudad.nombre}</p>}
        {editing ? (
          <ReservationEditor
            reservation={reservation}
            saving={saving}
            onCancel={onCancelEdit}
            onSave={onSave}
          />
        ) : (
          <>
            <dl className="my-reservation-dates">
              <div>
                <dt>Ingreso</dt>
                <dd><time dateTime={reservation.fechaInicio}>{formatDate(reservation.fechaInicio)}</time></dd>
              </div>
              <div>
                <dt>Salida</dt>
                <dd><time dateTime={reservation.fechaFin}>{formatDate(reservation.fechaFin)}</time></dd>
              </div>
            </dl>
            <div className="my-reservation-actions">
              {product?.id && (
                <Link className="detail-link" to={`/product/${product.id}`}>Ver alojamiento</Link>
              )}
              <button
                className="reservation-action-button secondary"
                type="button"
                onClick={onStartEdit}
                disabled={actionsDisabled}
              >
                Modificar
              </button>
              <button
                className="reservation-action-button danger"
                type="button"
                onClick={onDelete}
                disabled={actionsDisabled}
              >
                {deleting ? 'Cancelando...' : 'Cancelar reserva'}
              </button>
            </div>
          </>
        )}
      </div>
    </article>
  )
}

function MyReservationsContent({ userId }) {
  const [reservations, setReservations] = useState([])
  const [status, setStatus] = useState('loading')
  const [retryCount, setRetryCount] = useState(0)
  const [editingId, setEditingId] = useState(null)
  const [updatingId, setUpdatingId] = useState(null)
  const [deletingId, setDeletingId] = useState(null)
  const [feedback, setFeedback] = useState(null)

  useEffect(() => {
    const controller = new AbortController()

    async function loadReservations() {
      try {
        const { data } = await api.get(`/reservas/usuario/${encodeURIComponent(userId)}`, {
          signal: controller.signal,
        })
        if (!Array.isArray(data)) throw new Error('La respuesta de reservas no es una lista')
        setReservations(data)
        setStatus('success')
      } catch {
        if (!controller.signal.aborted) setStatus('error')
      }
    }

    loadReservations()
    return () => controller.abort()
  }, [userId, retryCount])

  function startEditing(reservationId) {
    setFeedback(null)
    setEditingId(reservationId)
  }

  async function updateReservation(reservation, fechaInicio, fechaFin) {
    setFeedback(null)
    setUpdatingId(reservation.id)

    try {
      const { data } = await api.put(`/reservas/${reservation.id}`, {
        fechaInicio,
        fechaFin,
        producto: { id: reservation.producto.id },
        usuario: { id: reservation.usuario.id },
      })

      setReservations((current) => current.map((item) => (item.id === reservation.id ? data : item)))
      setEditingId(null)
      setFeedback({ type: 'success', message: 'Reserva actualizada correctamente.' })
    } catch (error) {
      const fallback = error.response?.status === 409
        ? 'El alojamiento ya está reservado en esas fechas.'
        : 'No se pudo actualizar la reserva.'
      setFeedback({ type: 'error', message: getApiErrorMessage(error, fallback) })
    } finally {
      setUpdatingId(null)
    }
  }

  async function deleteReservation(reservation) {
    const confirmed = window.confirm('¿Seguro que querés cancelar esta reserva?')
    if (!confirmed) return

    setFeedback(null)
    setDeletingId(reservation.id)

    try {
      await api.delete(`/reservas/${reservation.id}`)
      setReservations((current) => current.filter((item) => item.id !== reservation.id))
      if (editingId === reservation.id) setEditingId(null)
      setFeedback({ type: 'success', message: 'Reserva cancelada correctamente.' })
    } catch (error) {
      setFeedback({
        type: 'error',
        message: getApiErrorMessage(error, 'No se pudo cancelar la reserva.'),
      })
    } finally {
      setDeletingId(null)
    }
  }

  const operationInProgress = updatingId !== null || deletingId !== null

  return (
    <section className="my-reservations">
      <h1>Mis reservas</h1>

      {feedback && (
        <p
          className={`reservation-operation-message ${feedback.type}`}
          role={feedback.type === 'error' ? 'alert' : 'status'}
        >
          {feedback.message}
        </p>
      )}

      {status === 'loading' && <p role="status">Cargando reservas...</p>}

      {status === 'error' && (
        <div className="home-message" role="alert">
          <h2>No pudimos cargar tus reservas</h2>
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
        </div>
      )}

      {status === 'success' && reservations.length === 0 && (
        <div className="home-message">
          <h2>Todavía no tenés reservas</h2>
          <p>Explorá los alojamientos y elegí las fechas de tu próxima estadía.</p>
          <Link className="detail-link" to="/">Ver alojamientos</Link>
        </div>
      )}

      {status === 'success' && reservations.length > 0 && (
        <div className="my-reservations-list">
          {reservations.map((reservation) => (
            <ReservationCard
              key={reservation.id}
              reservation={reservation}
              editing={editingId === reservation.id}
              saving={updatingId === reservation.id}
              deleting={deletingId === reservation.id}
              actionsDisabled={operationInProgress}
              onStartEdit={() => startEditing(reservation.id)}
              onCancelEdit={() => setEditingId(null)}
              onSave={(fechaInicio, fechaFin) => updateReservation(reservation, fechaInicio, fechaFin)}
              onDelete={() => deleteReservation(reservation)}
            />
          ))}
        </div>
      )}
    </section>
  )
}

function MyReservations() {
  const { user } = useAuth()
  if (!user) return <Navigate to="/login" replace />
  return <MyReservationsContent userId={user.id} />
}

export default MyReservations

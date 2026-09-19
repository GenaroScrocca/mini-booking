import { useState } from 'react'
import { Link } from 'react-router-dom'
import { useAuth } from '../context/auth.js'
import api from '../services/api.js'

function todayLocal() {
  const date = new Date()
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  return `${year}-${month}-${day}`
}

function ReservationForm({ productId }) {
  const { user } = useAuth()
  const [fechaInicio, setFechaInicio] = useState('')
  const [fechaFin, setFechaFin] = useState('')
  const [feedback, setFeedback] = useState(null)
  const [submitting, setSubmitting] = useState(false)
  const today = todayLocal()

  async function handleSubmit(event) {
    event.preventDefault()

    if (!fechaInicio || !fechaFin) {
      setFeedback({ type: 'error', message: 'Elegí las fechas de ingreso y salida.' })
      return
    }
    if (fechaInicio < todayLocal()) {
      setFeedback({ type: 'error', message: 'La fecha de ingreso no puede ser anterior a hoy.' })
      return
    }
    if (fechaFin <= fechaInicio) {
      setFeedback({ type: 'error', message: 'La fecha de salida debe ser posterior a la de ingreso.' })
      return
    }

    setFeedback(null)
    setSubmitting(true)
    try {
      await api.post('/reservas', {
        fechaInicio,
        fechaFin,
        producto: { id: productId },
        usuario: { id: user.id },
      })
      setFeedback({ type: 'success', message: 'Reserva creada correctamente.' })
    } catch (requestError) {
      const status = requestError.response?.status
      const data = requestError.response?.data
      let message

      if (status === 400) {
        message = data?.errors ? Object.values(data.errors).join(' ') : data?.message
        message ||= 'Revisá las fechas de la reserva.'
      } else if (status === 409) {
        message = 'El alojamiento ya está reservado en esas fechas.'
      } else if (status === 404) {
        message = data?.message || 'El alojamiento o tu usuario ya no existe.'
      } else if (!requestError.response) {
        message = 'No se pudo conectar con el servidor.'
      } else {
        message = 'No pudimos crear la reserva. Intentá nuevamente.'
      }

      setFeedback({ type: 'error', message })
    } finally {
      setSubmitting(false)
    }
  }

  return (
    <section className="detail-section reservation-section" aria-labelledby="reservation-title">
      <h2 id="reservation-title">Reservar alojamiento</h2>
      {!user ? (
        <>
          <p>Iniciá sesión para reservar este alojamiento.</p>
          <Link className="detail-link" to="/login">Iniciar sesión</Link>
        </>
      ) : (
        <form className="reservation-form" onSubmit={handleSubmit} noValidate>
          <div className="reservation-fields">
            <div className="form-field">
              <label htmlFor="reservation-start">Fecha de ingreso</label>
              <input
                id="reservation-start"
                type="date"
                value={fechaInicio}
                min={today}
                onChange={(event) => {
                  const nextDate = event.target.value
                  setFechaInicio(nextDate)
                  if (fechaFin && fechaFin <= nextDate) setFechaFin('')
                  setFeedback(null)
                }}
                required
              />
            </div>
            <div className="form-field">
              <label htmlFor="reservation-end">Fecha de salida</label>
              <input
                id="reservation-end"
                type="date"
                value={fechaFin}
                min={fechaInicio || today}
                onChange={(event) => {
                  setFechaFin(event.target.value)
                  setFeedback(null)
                }}
                required
              />
            </div>
          </div>
          {feedback && (
            <p
              className={`reservation-message reservation-${feedback.type}`}
              role={feedback.type === 'error' ? 'alert' : 'status'}
            >
              {feedback.message}
            </p>
          )}
          <button className="auth-submit reservation-submit" type="submit" disabled={submitting}>
            {submitting ? 'Reservando...' : 'Reservar'}
          </button>
        </form>
      )}
    </section>
  )
}

export default ReservationForm

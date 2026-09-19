import { useEffect, useState } from 'react'
import { Link, Navigate } from 'react-router-dom'
import { useAuth } from '../context/auth.js'
import api from '../services/api.js'

const dateFormatter = new Intl.DateTimeFormat('es-AR', {
  day: '2-digit',
  month: 'short',
  year: 'numeric',
  timeZone: 'UTC',
})

function formatDate(date) {
  return dateFormatter.format(new Date(`${date}T00:00:00Z`))
}

function ReservationCard({ reservation }) {
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
        {product?.id && (
          <Link className="detail-link" to={`/product/${product.id}`}>Ver alojamiento</Link>
        )}
      </div>
    </article>
  )
}

function MyReservationsContent({ userId }) {
  const [reservations, setReservations] = useState([])
  const [status, setStatus] = useState('loading')
  const [retryCount, setRetryCount] = useState(0)

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

  return (
    <section className="my-reservations">
      <h1>Mis reservas</h1>

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
            <ReservationCard key={reservation.id} reservation={reservation} />
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

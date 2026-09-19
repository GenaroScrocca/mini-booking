import { useState } from 'react'
import { Link, Navigate, useLocation, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/auth.js'
import api from '../services/api.js'

const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/

function Login() {
  const { user, login } = useAuth()
  const location = useLocation()
  const navigate = useNavigate()
  const [fields, setFields] = useState({ email: '', password: '' })
  const [error, setError] = useState('')
  const [submitting, setSubmitting] = useState(false)

  function updateField(event) {
    const { name, value } = event.target
    setFields((current) => ({ ...current, [name]: value }))
    setError('')
  }

  async function handleSubmit(event) {
    event.preventDefault()
    const email = fields.email.trim()

    if (!email || !fields.password) {
      setError('Completá email y contraseña.')
      return
    }
    if (!emailPattern.test(email)) {
      setError('Ingresá un email válido.')
      return
    }

    setSubmitting(true)
    try {
      const { data } = await api.post('/auth/login', { email, password: fields.password })
      login(data)
      navigate('/', { replace: true })
    } catch (requestError) {
      const status = requestError.response?.status
      if (status === 401) {
        setError('Credenciales inválidas')
      } else if (status === 400) {
        setError('Revisá el email y la contraseña.')
      } else if (!requestError.response) {
        setError('No se pudo conectar con el servidor. Intentá nuevamente.')
      } else {
        setError('No pudimos iniciar sesión. Intentá nuevamente.')
      }
      setSubmitting(false)
    }
  }

  if (user) return <Navigate to="/" replace />

  return (
    <section className="auth-page">
      <h1>Iniciar sesión</h1>
      <p>Ingresá con tu cuenta de Mini Booking.</p>
      {location.state?.registrationSuccess && (
        <p className="auth-message auth-success" role="status">
          Tu cuenta se creó correctamente. Ya podés iniciar sesión.
        </p>
      )}
      <form className="auth-form" onSubmit={handleSubmit} noValidate>
        <div className="form-field">
          <label htmlFor="login-email">Email</label>
          <input id="login-email" name="email" type="email" value={fields.email} onChange={updateField} autoComplete="email" required />
        </div>
        <div className="form-field">
          <label htmlFor="login-password">Contraseña</label>
          <input id="login-password" name="password" type="password" value={fields.password} onChange={updateField} autoComplete="current-password" required />
        </div>
        {error && <p className="auth-message auth-error" role="alert">{error}</p>}
        <button className="auth-submit" type="submit" disabled={submitting}>
          {submitting ? 'Ingresando...' : 'Iniciar sesión'}
        </button>
      </form>
      <p className="auth-switch">¿Todavía no tenés cuenta? <Link to="/register">Registrarse</Link></p>
    </section>
  )
}

export default Login

import { useState } from 'react'
import { Link, Navigate, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/auth.js'
import api from '../services/api.js'

const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/

function Register() {
  const { user } = useAuth()
  const navigate = useNavigate()
  const [fields, setFields] = useState({
    nombre: '',
    apellido: '',
    email: '',
    password: '',
    confirmPassword: '',
  })
  const [error, setError] = useState('')
  const [submitting, setSubmitting] = useState(false)

  function updateField(event) {
    const { name, value } = event.target
    setFields((current) => ({ ...current, [name]: value }))
    setError('')
  }

  async function handleSubmit(event) {
    event.preventDefault()
    const nombre = fields.nombre.trim()
    const apellido = fields.apellido.trim()
    const email = fields.email.trim()

    if (!nombre || !apellido || !email || !fields.password || !fields.confirmPassword) {
      setError('Completá todos los campos.')
      return
    }
    if (!emailPattern.test(email)) {
      setError('Ingresá un email válido.')
      return
    }
    if (fields.password !== fields.confirmPassword) {
      setError('Las contraseñas no coinciden.')
      return
    }
    if (fields.password.length < 4) {
      setError('La contraseña debe tener al menos 4 caracteres.')
      return
    }

    setSubmitting(true)
    try {
      await api.post('/auth/register', {
        nombre,
        apellido,
        email,
        password: fields.password,
      })
      navigate('/login', { replace: true, state: { registrationSuccess: true } })
    } catch (requestError) {
      const status = requestError.response?.status
      if (status === 409) {
        setError('Ya existe una cuenta con ese email.')
      } else if (status === 400) {
        const details = requestError.response.data?.errors
        const message = details ? Object.values(details).join(' ') : requestError.response.data?.message
        setError(message || 'Revisá los datos ingresados.')
      } else if (!requestError.response) {
        setError('No se pudo conectar con el servidor. Intentá nuevamente.')
      } else {
        setError('No pudimos completar el registro. Intentá nuevamente.')
      }
      setSubmitting(false)
    }
  }

  if (user) return <Navigate to="/" replace />

  return (
    <section className="auth-page">
      <h1>Crear cuenta</h1>
      <p>Registrate para empezar a usar Mini Booking.</p>
      <form className="auth-form" onSubmit={handleSubmit} noValidate>
        <div className="auth-name-row">
          <div className="form-field">
            <label htmlFor="register-name">Nombre</label>
            <input id="register-name" name="nombre" value={fields.nombre} onChange={updateField} autoComplete="given-name" maxLength={100} required />
          </div>
          <div className="form-field">
            <label htmlFor="register-last-name">Apellido</label>
            <input id="register-last-name" name="apellido" value={fields.apellido} onChange={updateField} autoComplete="family-name" maxLength={100} required />
          </div>
        </div>
        <div className="form-field">
          <label htmlFor="register-email">Email</label>
          <input id="register-email" name="email" type="email" value={fields.email} onChange={updateField} autoComplete="email" maxLength={150} required />
        </div>
        <div className="form-field">
          <label htmlFor="register-password">Contraseña</label>
          <input id="register-password" name="password" type="password" value={fields.password} onChange={updateField} autoComplete="new-password" minLength={4} maxLength={100} required />
        </div>
        <div className="form-field">
          <label htmlFor="register-confirm-password">Confirmar contraseña</label>
          <input id="register-confirm-password" name="confirmPassword" type="password" value={fields.confirmPassword} onChange={updateField} autoComplete="new-password" required />
        </div>
        {error && <p className="auth-message auth-error" role="alert">{error}</p>}
        <button className="auth-submit" type="submit" disabled={submitting}>
          {submitting ? 'Creando cuenta...' : 'Registrarse'}
        </button>
      </form>
      <p className="auth-switch">¿Ya tenés cuenta? <Link to="/login">Iniciar sesión</Link></p>
    </section>
  )
}

export default Register

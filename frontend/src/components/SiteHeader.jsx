import { Link, NavLink, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/auth.js'

function SiteHeader() {
  const { user, logout } = useAuth()
  const navigate = useNavigate()

  function handleLogout() {
    logout()
    navigate('/')
  }

  return (
    <header className="site-header">
      <div className="header-inner">
        <Link className="brand" to="/">Mini Booking</Link>
        <nav className="site-nav" aria-label="Navegación principal">
          <NavLink to="/" end>Inicio</NavLink>
          {user ? (
            <>
              <span className="header-greeting">Hola, {user.nombre}</span>
              <button className="logout-button" type="button" onClick={handleLogout}>
                Cerrar sesión
              </button>
            </>
          ) : (
            <>
              <NavLink to="/login">Iniciar sesión</NavLink>
              <NavLink to="/register">Registrarse</NavLink>
            </>
          )}
        </nav>
      </div>
    </header>
  )
}

export default SiteHeader

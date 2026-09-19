import { Link, NavLink } from 'react-router-dom'
import { useAuth } from '../context/auth.js'

function SiteHeader() {
  const { user, logout } = useAuth()

  function handleLogout() {
    logout()
    window.location.replace('/')
  }

  return (
    <header className="site-header">
      <div className="header-inner">
        <Link className="brand" to="/">Mini Booking</Link>
        <nav className="site-nav" aria-label="Navegación principal">
          <NavLink to="/" end>Inicio</NavLink>
          {user ? (
            <>
              <NavLink to="/mis-reservas">Mis reservas</NavLink>
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

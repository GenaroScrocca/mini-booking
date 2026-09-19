import { Link, NavLink } from 'react-router-dom'

function SiteHeader() {
  return (
    <header className="site-header">
      <div className="header-inner">
        <Link className="brand" to="/">Mini Booking</Link>
        <nav className="site-nav" aria-label="Navegación principal">
          <NavLink to="/" end>Inicio</NavLink>
          <NavLink to="/login">Iniciar sesión</NavLink>
          <NavLink to="/register">Registrarse</NavLink>
        </nav>
      </div>
    </header>
  )
}

export default SiteHeader

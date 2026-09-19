import { Link } from 'react-router-dom'

function Home() {
  return (
    <section>
      <h1>Mini-Booking</h1>
      <p>Encontrá tu próximo alojamiento.</p>
      <Link className="page-link" to="/product/1">Ver detalle de ejemplo</Link>
    </section>
  )
}

export default Home

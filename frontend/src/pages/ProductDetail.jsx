import { Link, useParams } from 'react-router-dom'

function ProductDetail() {
  const { id } = useParams()

  return (
    <section>
      <h1>Producto {id}</h1>
      <p>Detalle del producto en preparación.</p>
      <Link className="page-link" to="/">Volver al inicio</Link>
    </section>
  )
}

export default ProductDetail

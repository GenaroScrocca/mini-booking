import { useEffect, useState } from 'react'
import ProductCard from '../components/ProductCard.jsx'
import api from '../services/api.js'

function Home() {
  const [products, setProducts] = useState([])
  const [status, setStatus] = useState('loading')
  const [retryCount, setRetryCount] = useState(0)

  useEffect(() => {
    const controller = new AbortController()

    async function loadProducts() {
      try {
        const { data } = await api.get('/productos', { signal: controller.signal })
        if (!Array.isArray(data)) {
          throw new Error('La respuesta de productos no es una lista')
        }
        setProducts(data)
        setStatus('success')
      } catch {
        if (!controller.signal.aborted) {
          setStatus('error')
        }
      }
    }

    loadProducts()
    return () => controller.abort()
  }, [retryCount])

  return (
    <section className="home">
      <div className="home-heading">
        <h1>Encontrá tu próximo alojamiento</h1>
        <p>Explorá los lugares disponibles en Mini Booking.</p>
      </div>

      {status === 'loading' && <p role="status">Cargando alojamientos...</p>}

      {status === 'error' && (
        <div className="home-message" role="alert">
          <h2>No pudimos cargar los alojamientos</h2>
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

      {status === 'success' && products.length === 0 && (
        <div className="home-message">
          <h2>No hay alojamientos disponibles</h2>
          <p>Volvé a consultar más tarde.</p>
        </div>
      )}

      {status === 'success' && products.length > 0 && (
        <div className="product-grid">
          {products.map((product) => (
            <ProductCard key={product.id} product={product} />
          ))}
        </div>
      )}
    </section>
  )
}

export default Home

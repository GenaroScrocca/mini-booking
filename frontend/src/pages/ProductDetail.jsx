import { useEffect, useState } from 'react'
import { Link, useParams } from 'react-router-dom'
import ReservationForm from '../components/ReservationForm.jsx'
import api from '../services/api.js'

function ProductContent({ product }) {
  const descriptions = product.descripciones?.filter((item) => item.descripcion) ?? []
  const features = product.caracteristicas?.filter((item) => item.nombre) ?? []
  const policies = product.politica?.filter((item) => item.tipoPolitica || item.titulo || item.descripcion) ?? []
  const images = product.imagenesSecundarias?.filter((item) => item.urlImagen) ?? []
  const location = product.ubicacion

  return (
    <>
      <div className="detail-heading">
        <h1>{product.titulo}</h1>
        <div className="detail-meta">
          {product.ciudad?.nombre && <span>{product.ciudad.nombre}</span>}
          {product.categoria?.titulo && <span>{product.categoria.titulo}</span>}
          {product.puntaje && <span className="product-score">Puntaje {product.puntaje}</span>}
        </div>
      </div>

      {product.imagenPrincipalUrl && (
        <div className="detail-main-image">
          <img src={product.imagenPrincipalUrl} alt={product.titulo} />
        </div>
      )}

      <ReservationForm productId={product.id} />

      {images.length > 0 && (
        <section className="detail-section">
          <h2>Galería</h2>
          <div className="detail-gallery">
            {images.map((image) => (
              <figure key={image.id ?? image.urlImagen}>
                <img src={image.urlImagen} alt={image.titulo || product.titulo} loading="lazy" />
                {image.titulo && <figcaption>{image.titulo}</figcaption>}
              </figure>
            ))}
          </div>
        </section>
      )}

      {(product.descripcionCard || descriptions.length > 0) && (
        <section className="detail-section">
          <h2>Descripción</h2>
          {product.descripcionCard && <p className="detail-lead">{product.descripcionCard}</p>}
          {descriptions.map((item) => (
            <p key={item.id ?? item.descripcion}>{item.descripcion}</p>
          ))}
        </section>
      )}

      {features.length > 0 && (
        <section className="detail-section">
          <h2>Características</h2>
          <ul className="detail-features">
            {features.map((item) => (
              <li key={item.id ?? item.nombre}>{item.nombre}</li>
            ))}
          </ul>
        </section>
      )}

      {(location?.direccion || location?.descripcion || product.ciudad) && (
        <section className="detail-section">
          <h2>Ubicación</h2>
          {location?.direccion && <p className="detail-address">{location.direccion}</p>}
          {location?.descripcion && <p>{location.descripcion}</p>}
          {product.ciudad && (
            <p>{[product.ciudad.nombre, product.ciudad.provincia, product.ciudad.pais].filter(Boolean).join(', ')}</p>
          )}
        </section>
      )}

      {policies.length > 0 && (
        <section className="detail-section">
          <h2>Políticas</h2>
          <div className="detail-policies">
            {policies.map((item) => (
              <div key={item.id ?? `${item.tipoPolitica}-${item.titulo}`}>
                {item.tipoPolitica && <span className="detail-policy-type">{item.tipoPolitica}</span>}
                {item.titulo && <h3>{item.titulo}</h3>}
                {item.descripcion && <p>{item.descripcion}</p>}
              </div>
            ))}
          </div>
        </section>
      )}
    </>
  )
}

function ProductDetailView({ id }) {
  const [product, setProduct] = useState(null)
  const [status, setStatus] = useState('loading')
  const [retryCount, setRetryCount] = useState(0)

  useEffect(() => {
    const controller = new AbortController()

    async function loadProduct() {
      try {
        const { data } = await api.get(`/productos/${encodeURIComponent(id)}`, { signal: controller.signal })
        setProduct(data)
        setStatus('success')
      } catch (error) {
        if (!controller.signal.aborted) {
          setStatus(error.response?.status === 404 ? 'not-found' : 'error')
        }
      }
    }

    loadProduct()
    return () => controller.abort()
  }, [id, retryCount])

  return (
    <div className="product-detail">
      <Link className="page-link detail-back" to="/">Volver a alojamientos</Link>

      {status === 'loading' && <p role="status">Cargando alojamiento...</p>}

      {status === 'not-found' && (
        <div className="detail-state">
          <h1>Producto no encontrado</h1>
          <p>El alojamiento solicitado no existe.</p>
        </div>
      )}

      {status === 'error' && (
        <div className="detail-state" role="alert">
          <h1>No pudimos cargar este alojamiento</h1>
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

      {status === 'success' && product && <ProductContent product={product} />}
    </div>
  )
}

function ProductDetail() {
  const { id } = useParams()
  return <ProductDetailView key={id} id={id} />
}

export default ProductDetail

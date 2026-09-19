import { useState } from 'react'
import { Link } from 'react-router-dom'

function ProductCard({ product }) {
  const [imageError, setImageError] = useState(false)

  return (
    <article className="product-card">
      <div className="product-image">
        {product.imagenPrincipalUrl && !imageError ? (
          <img
            src={product.imagenPrincipalUrl}
            alt={product.titulo}
            loading="lazy"
            onError={() => setImageError(true)}
          />
        ) : (
          <span>Imagen no disponible</span>
        )}
      </div>
      <div className="product-body">
        <div className="product-meta">
          <span>{product.categoria?.titulo || 'Sin categoría'}</span>
          <span className="product-score">Puntaje {product.puntaje || '—'}</span>
        </div>
        <h2>{product.titulo}</h2>
        <p className="product-city">{product.ciudad?.nombre || 'Ciudad no indicada'}</p>
        <p className="product-description">{product.descripcionCard}</p>
        <Link className="detail-link" to={`/product/${product.id}`}>Ver detalle</Link>
      </div>
    </article>
  )
}

export default ProductCard

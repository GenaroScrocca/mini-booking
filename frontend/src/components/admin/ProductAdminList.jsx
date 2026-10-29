import { Link } from 'react-router-dom'

function ProductAdminList({ products, deletingId, onDelete }) {
  if (products.length === 0) {
    return <p className="admin-empty">No hay propiedades cargadas.</p>
  }

  return (
    <div className="admin-product-list">
      {products.map((product) => (
        <article className="admin-product-row" key={product.id}>
          <div className="admin-product-thumb">
            {product.imagenPrincipalUrl ? (
              <img src={product.imagenPrincipalUrl} alt="" />
            ) : (
              <span>Sin imagen</span>
            )}
          </div>
          <div className="admin-product-info">
            <h3>{product.titulo}</h3>
            <p>
              {product.ciudad?.nombre || 'Sin ciudad'}
              {' · '}
              {product.categoria?.titulo || 'Sin categoría'}
            </p>
          </div>
          <span className={product.disponible ? 'admin-status available' : 'admin-status unavailable'}>
            {product.disponible ? 'Disponible' : 'No disponible'}
          </span>
          <div className="admin-row-actions">
            <Link className="admin-secondary-button" to={`/administracion/productos/${product.id}/editar`}>
              Editar
            </Link>
            <button
              className="admin-danger-button"
              type="button"
              disabled={deletingId === product.id}
              onClick={() => onDelete(product)}
            >
              {deletingId === product.id ? 'Eliminando...' : 'Eliminar'}
            </button>
          </div>
        </article>
      ))}
    </div>
  )
}

export default ProductAdminList

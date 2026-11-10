import { useEffect, useState } from 'react'
import ProductCard from '../components/ProductCard.jsx'
import api from '../services/api.js'

function todayLocal() {
  const date = new Date()
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  return `${year}-${month}-${day}`
}

function Home() {
  const [products, setProducts] = useState([])
  const [cities, setCities] = useState([])
  const [categories, setCategories] = useState([])
  const [selectedCity, setSelectedCity] = useState('')
  const [selectedCategory, setSelectedCategory] = useState('')
  const [startDate, setStartDate] = useState('')
  const [endDate, setEndDate] = useState('')
  const [hasAppliedFilters, setHasAppliedFilters] = useState(false)
  const [hasAppliedDateFilters, setHasAppliedDateFilters] = useState(false)
  const [validationMessage, setValidationMessage] = useState('')
  const [status, setStatus] = useState('loading')
  const [retryCount, setRetryCount] = useState(0)
  const today = todayLocal()

  useEffect(() => {
    const controller = new AbortController()

    async function loadHomeData() {
      try {
        const [productsResponse, citiesResponse, categoriesResponse] = await Promise.all([
          api.get('/productos', { signal: controller.signal }),
          api.get('/ciudades', { signal: controller.signal }),
          api.get('/categorias', { signal: controller.signal }),
        ])
        if (
          !Array.isArray(productsResponse.data)
          || !Array.isArray(citiesResponse.data)
          || !Array.isArray(categoriesResponse.data)
        ) {
          throw new Error('La API devolvió un formato inesperado')
        }
        setProducts(productsResponse.data)
        setCities(citiesResponse.data)
        setCategories(categoriesResponse.data)
        setHasAppliedFilters(false)
        setHasAppliedDateFilters(false)
        setValidationMessage('')
        setStatus('success')
      } catch {
        if (!controller.signal.aborted) {
          setStatus('error')
        }
      }
    }

    loadHomeData()
    return () => controller.abort()
  }, [retryCount])

  async function handleSearch(event) {
    event.preventDefault()

    const hasStartDate = Boolean(startDate)
    const hasEndDate = Boolean(endDate)
    if (hasStartDate !== hasEndDate) {
      setValidationMessage('Completá la fecha de entrada y la fecha de salida.')
      return
    }
    if (hasStartDate && endDate <= startDate) {
      setValidationMessage('La fecha de salida debe ser posterior a la fecha de entrada.')
      return
    }

    setValidationMessage('')
    setStatus('loading')

    try {
      let endpoint = '/productos'
      let requestOptions
      if (hasStartDate) {
        endpoint = '/productos/disponibles'
        requestOptions = { params: { fechaInicio: startDate, fechaFin: endDate } }
      } else if (selectedCity) endpoint = `/productos/ciudad/${selectedCity}`
      else if (selectedCategory) endpoint = `/productos/categoria/${selectedCategory}`

      const { data } = await api.get(endpoint, requestOptions)
      if (!Array.isArray(data)) throw new Error('La respuesta de productos no es una lista')

      const filteredProducts = data.filter((product) => {
        const matchesCity = !selectedCity || String(product.ciudad?.id) === selectedCity
        const matchesCategory = !selectedCategory || String(product.categoria?.id) === selectedCategory
        return matchesCity && matchesCategory
      })

      setProducts(filteredProducts)
      setHasAppliedFilters(Boolean(selectedCity || selectedCategory || hasStartDate))
      setHasAppliedDateFilters(hasStartDate)
      setStatus('success')
    } catch {
      setStatus('error')
    }
  }

  async function handleClearFilters() {
    setSelectedCity('')
    setSelectedCategory('')
    setStartDate('')
    setEndDate('')
    setValidationMessage('')
    setStatus('loading')

    try {
      const { data } = await api.get('/productos')
      if (!Array.isArray(data)) throw new Error('La respuesta de productos no es una lista')
      setProducts(data)
      setHasAppliedFilters(false)
      setHasAppliedDateFilters(false)
      setStatus('success')
    } catch {
      setStatus('error')
    }
  }

  return (
    <section className="home">
      <div className="home-heading">
        <h1>Encontrá tu próximo alojamiento</h1>
        <p>Explorá los lugares disponibles en Mini Booking.</p>
      </div>

      <form className="home-filters" onSubmit={handleSearch}>
        <div className="home-filter-heading">
          <h2>Buscar propiedades</h2>
          <p>Elegí ubicación, categoría y fechas para encontrar una propiedad disponible.</p>
        </div>
        <div className="home-filter-controls">
          <label className="home-filter-field">
            <span>Ciudad</span>
            <select
              value={selectedCity}
              onChange={(event) => {
                setSelectedCity(event.target.value)
                setValidationMessage('')
              }}
              disabled={status === 'loading'}
            >
              <option value="">Todas las ciudades</option>
              {cities.map((city) => (
                <option value={city.id} key={city.id}>{city.nombre}</option>
              ))}
            </select>
          </label>
          <label className="home-filter-field">
            <span>Categoría</span>
            <select
              value={selectedCategory}
              onChange={(event) => {
                setSelectedCategory(event.target.value)
                setValidationMessage('')
              }}
              disabled={status === 'loading'}
            >
              <option value="">Todas las categorías</option>
              {categories.map((category) => (
                <option value={category.id} key={category.id}>{category.titulo}</option>
              ))}
            </select>
          </label>
          <label className="home-filter-field">
            <span>Fecha de entrada</span>
            <input
              type="date"
              value={startDate}
              min={today}
              onChange={(event) => {
                setStartDate(event.target.value)
                setValidationMessage('')
              }}
              disabled={status === 'loading'}
            />
          </label>
          <label className="home-filter-field">
            <span>Fecha de salida</span>
            <input
              type="date"
              value={endDate}
              min={startDate || today}
              onChange={(event) => {
                setEndDate(event.target.value)
                setValidationMessage('')
              }}
              disabled={status === 'loading'}
            />
          </label>
          <div className="home-filter-actions">
            <button className="filter-primary-button" type="submit" disabled={status === 'loading'}>
              Buscar
            </button>
            <button
              className="filter-secondary-button"
              type="button"
              disabled={
                status === 'loading'
                || (!selectedCity && !selectedCategory && !startDate && !endDate && !hasAppliedFilters)
              }
              onClick={handleClearFilters}
            >
              Limpiar filtros
            </button>
          </div>
        </div>
      </form>

      {validationMessage && <p className="home-filter-error" role="alert">{validationMessage}</p>}

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
          {hasAppliedFilters ? (
            <h2>
              {hasAppliedDateFilters
                ? 'No se encontraron propiedades disponibles para los filtros seleccionados.'
                : 'No se encontraron propiedades para los filtros seleccionados.'}
            </h2>
          ) : (
            <>
              <h2>No hay alojamientos disponibles</h2>
              <p>Volvé a consultar más tarde.</p>
            </>
          )}
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

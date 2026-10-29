import axios from 'axios'

const api = axios.create({
  baseURL: 'http://localhost:8080/api',
})

export function getApiErrorMessage(error, fallback) {
  if (!error.response) return 'No se pudo conectar con el servidor.'

  const data = error.response.data
  if (data?.errors) return Object.values(data.errors).join(' ')
  return data?.message || fallback
}

export default api

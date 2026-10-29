import { Navigate } from 'react-router-dom'
import { useAuth } from '../../context/auth.js'

function AdminRoute({ children }) {
  const { user } = useAuth()
  return user?.rol?.nombre === 'ADMIN' ? children : <Navigate to="/" replace />
}

export default AdminRoute

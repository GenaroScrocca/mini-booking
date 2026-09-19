import { useState } from 'react'
import { AuthContext } from './auth.js'

const STORAGE_KEY = 'mini-booking-user'

function publicUser(user) {
  return {
    id: user.id,
    nombre: user.nombre,
    apellido: user.apellido,
    email: user.email,
    rol: user.rol ? { id: user.rol.id, nombre: user.rol.nombre } : null,
  }
}

function readStoredUser() {
  try {
    const stored = localStorage.getItem(STORAGE_KEY)
    if (!stored) return null

    const user = JSON.parse(stored)
    return user?.id && user?.nombre ? publicUser(user) : null
  } catch {
    return null
  }
}

export function AuthProvider({ children }) {
  const [user, setUser] = useState(readStoredUser)

  function login(userResponse) {
    const nextUser = publicUser(userResponse)
    localStorage.setItem(STORAGE_KEY, JSON.stringify(nextUser))
    setUser(nextUser)
  }

  function logout() {
    localStorage.removeItem(STORAGE_KEY)
    setUser(null)
  }

  return (
    <AuthContext.Provider value={{ user, login, logout }}>
      {children}
    </AuthContext.Provider>
  )
}

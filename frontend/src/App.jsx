import { BrowserRouter, Route, Routes } from 'react-router-dom'
import AdminRoute from './components/admin/AdminRoute.jsx'
import SiteHeader from './components/SiteHeader.jsx'
import { AuthProvider } from './context/AuthContext.jsx'
import Admin from './pages/Admin.jsx'
import AdminProductForm from './pages/AdminProductForm.jsx'
import Home from './pages/Home.jsx'
import Login from './pages/Login.jsx'
import MyReservations from './pages/MyReservations.jsx'
import ProductDetail from './pages/ProductDetail.jsx'
import Register from './pages/Register.jsx'
import './App.css'

function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <SiteHeader />
        <main className="page-content">
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/login" element={<Login />} />
            <Route path="/register" element={<Register />} />
            <Route path="/mis-reservas" element={<MyReservations />} />
            <Route path="/product/:id" element={<ProductDetail />} />
            <Route
              path="/administracion"
              element={<AdminRoute><Admin /></AdminRoute>}
            />
            <Route
              path="/administracion/productos/nuevo"
              element={<AdminRoute><AdminProductForm /></AdminRoute>}
            />
            <Route
              path="/administracion/productos/:id/editar"
              element={<AdminRoute><AdminProductForm /></AdminRoute>}
            />
          </Routes>
        </main>
      </BrowserRouter>
    </AuthProvider>
  )
}

export default App

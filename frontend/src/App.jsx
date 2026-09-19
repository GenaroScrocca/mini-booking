import { BrowserRouter, Route, Routes } from 'react-router-dom'
import SiteHeader from './components/SiteHeader.jsx'
import { AuthProvider } from './context/AuthContext.jsx'
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
          </Routes>
        </main>
      </BrowserRouter>
    </AuthProvider>
  )
}

export default App

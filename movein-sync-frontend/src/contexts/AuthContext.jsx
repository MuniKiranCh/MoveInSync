import { createContext, useContext, useState, useEffect } from 'react'
import { jwtDecode } from 'jwt-decode'
import api from '../utils/api'
import toast from 'react-hot-toast'

const AuthContext = createContext(null)

export const useAuth = () => {
  const context = useContext(AuthContext)
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider')
  }
  return context
}

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    // Check if user is already logged in
    const token = localStorage.getItem('token')
    if (token) {
      try {
        const decoded = jwtDecode(token)
        // Check if token is expired
        if (decoded.exp * 1000 > Date.now()) {
          setUser({
            id: decoded.userId || decoded.sub,
            email: decoded.sub,
            role: decoded.role,
            username: decoded.name || decoded.username || decoded.sub,
            tenantId: decoded.tenantId,
            vendorId: decoded.vendorId,
          })
        } else {
          localStorage.removeItem('token')
        }
      } catch (error) {
        console.error('Error decoding token:', error)
        localStorage.removeItem('token')
      }
    }
    setLoading(false)
  }, [])

  const login = async (email, password) => {
    try {
      const response = await api.post('/login', { email, password })
      const { token } = response.data
      
      localStorage.setItem('token', token)
      
      const decoded = jwtDecode(token)
      setUser({
        id: decoded.userId,
        email: decoded.sub, // JWT subject contains the email
        role: decoded.role,
        username: decoded.name || decoded.sub, // Use name if available, else email
        tenantId: decoded.tenantId,
        vendorId: decoded.vendorId,
      })
      
      toast.success('Login successful!')
      return true
    } catch (error) {
      toast.error(error.response?.data?.message || 'Login failed')
      console.error('Login error:', error)
      return false
    }
  }

  const register = async (userData) => {
    try {
      const response = await api.post('/register', userData)
      const { token } = response.data
      
      localStorage.setItem('token', token)
      
      const decoded = jwtDecode(token)
      setUser({
        id: decoded.userId,
        email: decoded.sub,
        role: decoded.role,
        username: decoded.name || decoded.sub,
        tenantId: decoded.tenantId,
        vendorId: decoded.vendorId,
      })
      
      toast.success('Account created successfully!')
      return true
    } catch (error) {
      if (error.response?.status === 409) {
        toast.error('An account with this email already exists')
      } else {
        toast.error(error.response?.data?.message || 'Registration failed')
      }
      console.error('Registration error:', error)
      return false
    }
  }

  const logout = () => {
    localStorage.removeItem('token')
    setUser(null)
    toast.success('Logged out successfully')
  }

  const value = {
    user,
    login,
    register,
    logout,
    loading,
  }

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}


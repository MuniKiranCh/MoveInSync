import axios from 'axios'

// Base URLs for different services
const AUTH_BASE_URL = 'http://localhost:4005'
const CLIENT_BASE_URL = 'http://localhost:4010'
const VENDOR_BASE_URL = 'http://localhost:4015'
const TRIP_BASE_URL = 'http://localhost:4020'
const BILLING_BASE_URL = 'http://localhost:4025'
const ANALYTICS_BASE_URL = 'http://localhost:4030'
const EMPLOYEE_BASE_URL = 'http://localhost:4001'

// Create axios instances for each service
export const authApi = axios.create({
  baseURL: AUTH_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
})

export const clientApi = axios.create({
  baseURL: CLIENT_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
})

export const vendorApi = axios.create({
  baseURL: VENDOR_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
})

export const employeeApi = axios.create({
  baseURL: EMPLOYEE_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
})

export const tripApi = axios.create({
  baseURL: TRIP_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
})

export const billingApi = axios.create({
  baseURL: BILLING_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
})

export const analyticsApi = axios.create({
  baseURL: ANALYTICS_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
})

// Add auth token to all requests
const addAuthInterceptor = (instance) => {
  instance.interceptors.request.use(
    (config) => {
      const token = localStorage.getItem('token')
      if (token) {
        config.headers.Authorization = `Bearer ${token}`
      }
      return config
    },
    (error) => Promise.reject(error)
  )

  instance.interceptors.response.use(
    (response) => response,
    (error) => {
      if (error.response?.status === 401) {
        localStorage.removeItem('token')
        window.location.href = '/login'
      }
      return Promise.reject(error)
    }
  )
}

// Apply interceptors to all instances
addAuthInterceptor(authApi)
addAuthInterceptor(clientApi)
addAuthInterceptor(vendorApi)
addAuthInterceptor(employeeApi)
addAuthInterceptor(tripApi)
addAuthInterceptor(billingApi)
addAuthInterceptor(analyticsApi)

// Default export for auth (backward compatibility)
const api = authApi
export default api

import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom'
import { QueryClient, QueryClientProvider } from 'react-query'
import { Toaster } from 'react-hot-toast'
import { AuthProvider } from './contexts/AuthContext'
import { useAuth } from './contexts/AuthContext'
import Login from './pages/Login'
import Register from './pages/Register'
import AdminDashboard from './pages/AdminDashboard'
import ClientDashboard from './pages/ClientDashboard'
import ClientEmployees from './pages/ClientEmployees'
import ClientVendors from './pages/ClientVendors'
import VendorDashboard from './pages/VendorDashboard'
import EmployeeDashboard from './pages/EmployeeDashboard'
import Clients from './pages/Clients'
import Vendors from './pages/Vendors'
import Employees from './pages/Employees'
import Trips from './pages/Trips'
import BillingModels from './pages/BillingModels'
import Reports from './pages/Reports'
import Layout from './components/Layout'

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      refetchOnWindowFocus: false,
      retry: 1,
    },
  },
})

function PrivateRoute({ children, allowedRoles }) {
  const { user, loading } = useAuth()
  
  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary-600"></div>
      </div>
    )
  }
  
  if (!user) {
    return <Navigate to="/login" replace />
  }
  
  if (allowedRoles && !allowedRoles.includes(user.role)) {
    return <Navigate to="/" replace />
  }
  
  return children
}

function AppRoutes() {
  const { user } = useAuth()
  
  return (
    <Routes>
      <Route path="/login" element={<Login />} />
      <Route path="/register" element={<Register />} />
      
      <Route path="/" element={
        <PrivateRoute>
          <Layout />
        </PrivateRoute>
      }>
        {/* Redirect based on role */}
        <Route index element={
          user?.role === 'ADMIN' ? <Navigate to="/admin" replace /> :
          user?.role === 'CLIENT' ? <Navigate to="/client" replace /> :
          user?.role === 'VENDOR' ? <Navigate to="/vendor" replace /> :
          user?.role === 'EMPLOYEE' ? <Navigate to="/employee" replace /> :
          <Navigate to="/login" replace />
        } />
        
        {/* Admin Routes */}
        <Route path="admin" element={
          <PrivateRoute allowedRoles={['ADMIN']}>
            <AdminDashboard />
          </PrivateRoute>
        } />
        <Route path="clients" element={
          <PrivateRoute allowedRoles={['ADMIN']}>
            <Clients />
          </PrivateRoute>
        } />
        <Route path="vendors" element={
          <PrivateRoute allowedRoles={['ADMIN']}>
            <Vendors />
          </PrivateRoute>
        } />
        <Route path="employees" element={
          <PrivateRoute allowedRoles={['ADMIN']}>
            <Employees />
          </PrivateRoute>
        } />
        <Route path="trips" element={
          <PrivateRoute allowedRoles={['ADMIN']}>
            <Trips />
          </PrivateRoute>
        } />
        <Route path="billing-models" element={
          <PrivateRoute allowedRoles={['ADMIN']}>
            <BillingModels />
          </PrivateRoute>
        } />
        <Route path="reports" element={
          <PrivateRoute allowedRoles={['ADMIN']}>
            <Reports />
          </PrivateRoute>
        } />
        
        {/* Client Routes */}
        <Route path="client" element={
          <PrivateRoute allowedRoles={['CLIENT']}>
            <ClientDashboard />
          </PrivateRoute>
        } />
        <Route path="client/employees" element={
          <PrivateRoute allowedRoles={['CLIENT']}>
            <ClientEmployees />
          </PrivateRoute>
        } />
        <Route path="client/vendors" element={
          <PrivateRoute allowedRoles={['CLIENT']}>
            <ClientVendors />
          </PrivateRoute>
        } />
        
        {/* Vendor Routes */}
        <Route path="vendor" element={
          <PrivateRoute allowedRoles={['VENDOR']}>
            <VendorDashboard />
          </PrivateRoute>
        } />
        
        {/* Employee Routes */}
        <Route path="employee" element={
          <PrivateRoute allowedRoles={['EMPLOYEE']}>
            <EmployeeDashboard />
          </PrivateRoute>
        } />
      </Route>
      
      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  )
}

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <AuthProvider>
        <Router>
          <AppRoutes />
          <Toaster position="top-right" />
        </Router>
      </AuthProvider>
    </QueryClientProvider>
  )
}

export default App


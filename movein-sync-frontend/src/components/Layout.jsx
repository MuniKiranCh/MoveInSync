import { Outlet, Link, useNavigate, useLocation } from 'react-router-dom'
import { useAuth } from '../contexts/AuthContext'
import { 
  LayoutDashboard, 
  Users, 
  Truck, 
  UserCircle, 
  MapPin, 
  FileText, 
  Settings,
  LogOut,
  Menu,
  X,
  Building2
} from 'lucide-react'
import { useState } from 'react'

const Layout = () => {
  const { user, logout } = useAuth()
  const navigate = useNavigate()
  const location = useLocation()
  const [sidebarOpen, setSidebarOpen] = useState(false)

  const handleLogout = () => {
    logout()
    navigate('/login')
  }

  const adminNavItems = [
    { name: 'Dashboard', path: '/admin', icon: LayoutDashboard },
    { name: 'Clients', path: '/clients', icon: Building2 },
    { name: 'Vendors', path: '/vendors', icon: Truck },
    { name: 'Employees', path: '/employees', icon: UserCircle },
    { name: 'Trips', path: '/trips', icon: MapPin },
    { name: 'Billing Models', path: '/billing-models', icon: Settings },
    { name: 'Reports', path: '/reports', icon: FileText },
  ]

  const clientNavItems = [
    { name: 'Dashboard', path: '/client', icon: LayoutDashboard },
    { name: 'My Employees', path: '/client/employees', icon: Users },
    { name: 'Vendors', path: '/client/vendors', icon: Truck },
    { name: 'Trips', path: '/client/trips', icon: MapPin },
    { name: 'Reports', path: '/client/reports', icon: FileText },
  ]

  const vendorNavItems = [
    { name: 'Dashboard', path: '/vendor', icon: LayoutDashboard },
  ]

  const employeeNavItems = [
    { name: 'Dashboard', path: '/employee', icon: LayoutDashboard },
  ]

  const navItems = user?.role === 'ADMIN' ? adminNavItems :
                   user?.role === 'CLIENT' ? clientNavItems :
                   user?.role === 'VENDOR' ? vendorNavItems :
                   employeeNavItems

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Top Navigation Bar */}
      <nav className="bg-white shadow-sm border-b border-gray-200 fixed w-full z-30">
        <div className="px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between h-16">
            <div className="flex items-center">
              <button
                onClick={() => setSidebarOpen(!sidebarOpen)}
                className="mr-4 lg:hidden"
              >
                {sidebarOpen ? <X size={24} /> : <Menu size={24} />}
              </button>
              <h1 className="text-xl font-bold text-primary-600">
                MoveInSync Billing
              </h1>
            </div>
            
            <div className="flex items-center space-x-4">
              <div className="text-right">
                <p className="text-sm font-medium text-gray-900">{user?.username}</p>
                <p className="text-xs text-gray-500">{user?.role}</p>
              </div>
              <button
                onClick={handleLogout}
                className="p-2 text-gray-600 hover:text-gray-900 hover:bg-gray-100 rounded-lg"
                title="Logout"
              >
                <LogOut size={20} />
              </button>
            </div>
          </div>
        </div>
      </nav>

      {/* Sidebar */}
      <aside
        className={`
          fixed top-16 left-0 z-20 w-64 h-[calc(100vh-4rem)] bg-white border-r border-gray-200
          transition-transform duration-300 ease-in-out
          ${sidebarOpen ? 'translate-x-0' : '-translate-x-full'}
          lg:translate-x-0
        `}
      >
        <div className="flex flex-col h-full">
          <nav className="flex-1 px-4 py-6 space-y-1 overflow-y-auto">
            {navItems.map((item) => {
              const Icon = item.icon
              const isActive = location.pathname === item.path
              
              return (
                <Link
                  key={item.path}
                  to={item.path}
                  onClick={() => setSidebarOpen(false)}
                  className={`
                    flex items-center px-4 py-3 text-sm font-medium rounded-lg
                    transition-colors duration-200
                    ${isActive
                      ? 'bg-primary-50 text-primary-700'
                      : 'text-gray-700 hover:bg-gray-50'
                    }
                  `}
                >
                  <Icon size={20} className="mr-3" />
                  {item.name}
                </Link>
              )
            })}
          </nav>
        </div>
      </aside>

      {/* Main Content */}
      <main className="lg:pl-64 pt-16">
        <div className="px-4 sm:px-6 lg:px-8 py-8">
          <Outlet />
        </div>
      </main>

      {/* Overlay for mobile */}
      {sidebarOpen && (
        <div
          className="fixed inset-0 bg-black bg-opacity-50 z-10 lg:hidden"
          onClick={() => setSidebarOpen(false)}
        ></div>
      )}
    </div>
  )
}

export default Layout


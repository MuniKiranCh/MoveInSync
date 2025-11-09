import { useState } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import { useAuth } from '../contexts/AuthContext'
import { LogIn } from 'lucide-react'

const Login = () => {
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [loading, setLoading] = useState(false)
  const { login } = useAuth()
  const navigate = useNavigate()

  const handleSubmit = async (e) => {
    e.preventDefault()
    setLoading(true)
    
    const success = await login(email, password)
    
    setLoading(false)
    
    if (success) {
      navigate('/')
    }
  }

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-primary-50 to-primary-100">
      <div className="max-w-md w-full mx-4">
        <div className="bg-white rounded-2xl shadow-xl p-8">
          <div className="text-center mb-8">
            <div className="inline-flex items-center justify-center w-16 h-16 bg-primary-600 rounded-full mb-4">
              <LogIn className="text-white" size={32} />
            </div>
            <h2 className="text-3xl font-bold text-gray-900">Welcome Back</h2>
            <p className="text-gray-600 mt-2">Sign in to MoveInSync Billing Portal</p>
          </div>

          <form onSubmit={handleSubmit} className="space-y-6">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Email
              </label>
              <input
                type="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                className="input"
                placeholder="Enter your email"
                required
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Password
              </label>
              <input
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                className="input"
                placeholder="Enter your password"
                required
              />
            </div>

            <button
              type="submit"
              disabled={loading}
              className="w-full btn btn-primary py-3 text-lg disabled:opacity-50 disabled:cursor-not-allowed"
            >
              {loading ? 'Signing in...' : 'Sign In'}
            </button>
          </form>

          <div className="mt-6 text-center">
            <p className="text-sm text-gray-600">
              Don't have an account?{' '}
              <Link to="/register" className="text-primary-600 hover:text-primary-700 font-medium">
                Register Your Company
              </Link>
            </p>
          </div>

            <div className="mt-8 pt-6 border-t border-gray-200">
              <p className="text-sm text-gray-600 text-center mb-3">
                Demo Credentials:
              </p>
              <div className="space-y-3 text-xs">
                <div className="bg-blue-50 p-3 rounded">
                  <p className="font-semibold text-blue-900 mb-1">Super Admin (MoveInSync)</p>
                  <p className="text-blue-700">admin@moveinsync.com</p>
                  <p className="text-blue-700">Password: password</p>
                </div>
                <div className="bg-indigo-50 p-3 rounded">
                  <p className="font-semibold text-indigo-900 mb-1">Client (Amazon India)</p>
                  <p className="text-indigo-700">admin@amazon.in</p>
                  <p className="text-indigo-700">Password: password</p>
                </div>
                <div className="bg-purple-50 p-3 rounded">
                  <p className="font-semibold text-purple-900 mb-1">Vendor (Ola Cabs)</p>
                  <p className="text-purple-700">vendor@ola.com</p>
                  <p className="text-purple-700">Password: password</p>
                </div>
              </div>
              <div className="mt-4 text-center">
                <p className="text-xs text-gray-500">
                  💡 Employees: Create accounts via company admin
                </p>
              </div>
            </div>
        </div>
      </div>
    </div>
  )
}

export default Login


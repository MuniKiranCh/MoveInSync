import { useState, useEffect } from 'react'
import { 
  TrendingUp, 
  Users, 
  Truck, 
  MapPin, 
  DollarSign,
  Calendar,
  AlertCircle
} from 'lucide-react'
import { 
  LineChart, 
  Line, 
  BarChart, 
  Bar, 
  PieChart, 
  Pie, 
  Cell,
  XAxis, 
  YAxis, 
  CartesianGrid, 
  Tooltip, 
  Legend, 
  ResponsiveContainer 
} from 'recharts'
import { authApi, clientApi, vendorApi, tripApi } from '../utils/api'
import toast from 'react-hot-toast'

const StatCard = ({ title, value, change, icon: Icon, color }) => (
  <div className="card hover:shadow-lg transition-shadow duration-200">
    <div className="flex items-center justify-between">
      <div>
        <p className="text-sm font-medium text-gray-600">{title}</p>
        <p className="text-3xl font-bold text-gray-900 mt-2">{value}</p>
        {change && (
          <p className={`text-sm mt-2 ${change > 0 ? 'text-green-600' : 'text-red-600'}`}>
            {change > 0 ? '+' : ''}{change}% from last month
          </p>
        )}
      </div>
      <div className={`p-4 rounded-full ${color}`}>
        <Icon size={28} className="text-white" />
      </div>
    </div>
  </div>
)

const AdminDashboard = () => {
  const [stats, setStats] = useState({
    totalClients: 0,
    totalVendors: 0,
    totalEmployees: 0,
    totalTrips: 0,
    totalRevenue: 0,
    activeTrips: 0,
  })

  const [loading, setLoading] = useState(true)

  useEffect(() => {
    fetchDashboardData()
  }, [])

  const fetchDashboardData = async () => {
    try {
      setLoading(true)
      // Call the correct microservices
      const [clients, vendors, employees, trips] = await Promise.all([
        clientApi.get('/clients').catch(() => ({ data: [] })),
        vendorApi.get('/vendors').catch(() => ({ data: [] })),
        authApi.get('/users').catch(() => ({ data: [] })),
        tripApi.get('/trips').catch(() => ({ data: [] })),
      ])

      // Filter employees from users
      const employeesList = employees.data.filter(u => u.role === 'EMPLOYEE')

      setStats({
        totalClients: clients.data?.length || 0,
        totalVendors: vendors.data?.length || 0,
        totalEmployees: employeesList?.length || 0,
        totalTrips: trips.data?.length || 0,
        totalRevenue: trips.data?.reduce((sum, trip) => sum + (trip.amount || 0), 0) || 0,
        activeTrips: trips.data?.filter(t => t.status === 'ACTIVE')?.length || 0,
      })
    } catch (error) {
      console.error('Error fetching dashboard data:', error)
      toast.error('Failed to load dashboard data')
    } finally {
      setLoading(false)
    }
  }

  // Sample data for charts
  const monthlyTripsData = [
    { month: 'Jan', trips: 650, revenue: 185000 },
    { month: 'Feb', trips: 720, revenue: 205000 },
    { month: 'Mar', trips: 680, revenue: 195000 },
    { month: 'Apr', trips: 750, revenue: 215000 },
    { month: 'May', trips: 820, revenue: 235000 },
    { month: 'Jun', trips: 800, revenue: 230000 },
  ]

  const billingModelData = [
    { name: 'Package Model', value: 45, color: '#0ea5e9' },
    { name: 'Trip Model', value: 35, color: '#8b5cf6' },
    { name: 'Hybrid Model', value: 20, color: '#10b981' },
  ]

  const topVendorsData = [
    { name: 'FastCabs', trips: 1250, revenue: 350000 },
    { name: 'QuickRide', trips: 1100, revenue: 320000 },
    { name: 'CityTransport', trips: 980, revenue: 285000 },
    { name: 'MetroWheels', trips: 850, revenue: 245000 },
    { name: 'UrbanCommute', trips: 720, revenue: 210000 },
  ]

  if (loading) {
    return (
      <div className="flex items-center justify-center h-96">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary-600"></div>
      </div>
    )
  }

  return (
    <div className="space-y-6">
      {/* Header */}
      <div>
        <h1 className="text-3xl font-bold text-gray-900">Dashboard</h1>
        <p className="text-gray-600 mt-2">Welcome to MoveInSync Billing & Reporting</p>
      </div>

      {/* Stats Cards */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        <StatCard
          title="Total Clients"
          value={stats.totalClients}
          change={12}
          icon={Users}
          color="bg-blue-500"
        />
        <StatCard
          title="Total Vendors"
          value={stats.totalVendors}
          change={8}
          icon={Truck}
          color="bg-purple-500"
        />
        <StatCard
          title="Total Employees"
          value={stats.totalEmployees.toLocaleString()}
          change={15}
          icon={Users}
          color="bg-green-500"
        />
        <StatCard
          title="Total Trips"
          value={stats.totalTrips.toLocaleString()}
          change={22}
          icon={MapPin}
          color="bg-orange-500"
        />
      </div>

      {/* Revenue and Active Trips */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        <div className="card">
          <div className="flex items-center justify-between mb-4">
            <h3 className="text-lg font-semibold text-gray-900">Total Revenue</h3>
            <DollarSign className="text-green-600" />
          </div>
          <p className="text-4xl font-bold text-gray-900">
            ₹{(stats.totalRevenue / 1000000).toFixed(2)}M
          </p>
          <p className="text-sm text-gray-600 mt-2">Last 6 months</p>
        </div>

        <div className="card">
          <div className="flex items-center justify-between mb-4">
            <h3 className="text-lg font-semibold text-gray-900">Active Trips Today</h3>
            <TrendingUp className="text-blue-600" />
          </div>
          <p className="text-4xl font-bold text-gray-900">{stats.activeTrips}</p>
          <p className="text-sm text-gray-600 mt-2">In progress</p>
        </div>
      </div>

      {/* Charts */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* Monthly Trips Trend */}
        <div className="card">
          <h3 className="text-lg font-semibold text-gray-900 mb-4">Monthly Trips & Revenue</h3>
          <ResponsiveContainer width="100%" height={300}>
            <LineChart data={monthlyTripsData}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="month" />
              <YAxis yAxisId="left" />
              <YAxis yAxisId="right" orientation="right" />
              <Tooltip />
              <Legend />
              <Line 
                yAxisId="left"
                type="monotone" 
                dataKey="trips" 
                stroke="#0ea5e9" 
                strokeWidth={2}
                name="Trips"
              />
              <Line 
                yAxisId="right"
                type="monotone" 
                dataKey="revenue" 
                stroke="#10b981" 
                strokeWidth={2}
                name="Revenue (₹)"
              />
            </LineChart>
          </ResponsiveContainer>
        </div>

        {/* Billing Model Distribution */}
        <div className="card">
          <h3 className="text-lg font-semibold text-gray-900 mb-4">Billing Model Distribution</h3>
          <ResponsiveContainer width="100%" height={300}>
            <PieChart>
              <Pie
                data={billingModelData}
                cx="50%"
                cy="50%"
                labelLine={false}
                label={({ name, percent }) => `${name}: ${(percent * 100).toFixed(0)}%`}
                outerRadius={100}
                fill="#8884d8"
                dataKey="value"
              >
                {billingModelData.map((entry, index) => (
                  <Cell key={`cell-${index}`} fill={entry.color} />
                ))}
              </Pie>
              <Tooltip />
            </PieChart>
          </ResponsiveContainer>
        </div>

        {/* Top Vendors */}
        <div className="card lg:col-span-2">
          <h3 className="text-lg font-semibold text-gray-900 mb-4">Top Vendors by Revenue</h3>
          <ResponsiveContainer width="100%" height={300}>
            <BarChart data={topVendorsData}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="name" />
              <YAxis />
              <Tooltip />
              <Legend />
              <Bar dataKey="trips" fill="#0ea5e9" name="Trips" />
              <Bar dataKey="revenue" fill="#8b5cf6" name="Revenue (₹)" />
            </BarChart>
          </ResponsiveContainer>
        </div>
      </div>

      {/* Recent Alerts */}
      <div className="card">
        <h3 className="text-lg font-semibold text-gray-900 mb-4 flex items-center">
          <AlertCircle className="mr-2 text-orange-500" />
          Recent Alerts & Notifications
        </h3>
        <div className="space-y-3">
          <div className="flex items-center justify-between p-3 bg-orange-50 rounded-lg">
            <div className="flex items-center">
              <div className="w-2 h-2 bg-orange-500 rounded-full mr-3"></div>
              <div>
                <p className="text-sm font-medium text-gray-900">High overtime detected for Vendor: FastCabs</p>
                <p className="text-xs text-gray-600">2 hours ago</p>
              </div>
            </div>
          </div>
          <div className="flex items-center justify-between p-3 bg-blue-50 rounded-lg">
            <div className="flex items-center">
              <div className="w-2 h-2 bg-blue-500 rounded-full mr-3"></div>
              <div>
                <p className="text-sm font-medium text-gray-900">New billing model configured for Client: TechCorp</p>
                <p className="text-xs text-gray-600">5 hours ago</p>
              </div>
            </div>
          </div>
          <div className="flex items-center justify-between p-3 bg-green-50 rounded-lg">
            <div className="flex items-center">
              <div className="w-2 h-2 bg-green-500 rounded-full mr-3"></div>
              <div>
                <p className="text-sm font-medium text-gray-900">Monthly report generated successfully</p>
                <p className="text-xs text-gray-600">1 day ago</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}

export default AdminDashboard


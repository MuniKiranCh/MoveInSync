import { useState, useEffect } from 'react'
import { 
  MapPin, 
  DollarSign,
  Calendar,
  TrendingUp,
  Award
} from 'lucide-react'
import { 
  LineChart, 
  Line, 
  BarChart, 
  Bar,
  XAxis, 
  YAxis, 
  CartesianGrid, 
  Tooltip, 
  Legend, 
  ResponsiveContainer 
} from 'recharts'
import { format } from 'date-fns'
import toast from 'react-hot-toast'

const StatCard = ({ title, value, subtitle, icon: Icon, color }) => (
  <div className="card hover:shadow-lg transition-shadow duration-200">
    <div className="flex items-center justify-between">
      <div>
        <p className="text-sm font-medium text-gray-600">{title}</p>
        <p className="text-3xl font-bold text-gray-900 mt-2">{value}</p>
        {subtitle && (
          <p className="text-sm text-gray-500 mt-2">{subtitle}</p>
        )}
      </div>
      <div className={`p-4 rounded-full ${color}`}>
        <Icon size={28} className="text-white" />
      </div>
    </div>
  </div>
)

const EmployeeDashboard = () => {
  const [stats, setStats] = useState({
    totalTrips: 0,
    regularTrips: 0,
    extraTrips: 0,
    totalIncentives: 0,
    pendingIncentives: 0,
    totalDistance: 0,
  })

  const [recentTrips, setRecentTrips] = useState([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    fetchEmployeeData()
  }, [])

  const fetchEmployeeData = async () => {
    try {
      setLoading(true)
      // Simulate API call
      await new Promise(resolve => setTimeout(resolve, 500))
      
      setStats({
        totalTrips: 42,
        regularTrips: 38,
        extraTrips: 4,
        totalIncentives: 2500,
        pendingIncentives: 500,
        totalDistance: 525,
      })

      setRecentTrips([
        {
          id: 1,
          date: '2024-01-20',
          pickup: 'Home (Whitefield)',
          dropoff: 'Office (MG Road)',
          distance: 15.2,
          duration: 45,
          extraHours: 0,
          incentive: 0,
          vendor: 'FastCabs',
        },
        {
          id: 2,
          date: '2024-01-19',
          pickup: 'Home (Whitefield)',
          dropoff: 'Office (MG Road)',
          distance: 15.2,
          duration: 45,
          extraHours: 2,
          incentive: 500,
          vendor: 'FastCabs',
        },
        {
          id: 3,
          date: '2024-01-18',
          pickup: 'Home (Whitefield)',
          dropoff: 'Office (MG Road)',
          distance: 15.2,
          duration: 45,
          extraHours: 1.5,
          incentive: 375,
          vendor: 'QuickRide',
        },
        {
          id: 4,
          date: '2024-01-17',
          pickup: 'Home (Whitefield)',
          dropoff: 'Office (MG Road)',
          distance: 15.2,
          duration: 45,
          extraHours: 0,
          incentive: 0,
          vendor: 'FastCabs',
        },
      ])
    } catch (error) {
      toast.error('Failed to load employee data')
    } finally {
      setLoading(false)
    }
  }

  // Sample data for charts
  const weeklyTripsData = [
    { day: 'Mon', trips: 2, incentive: 0 },
    { day: 'Tue', trips: 2, incentive: 500 },
    { day: 'Wed', trips: 2, incentive: 375 },
    { day: 'Thu', trips: 2, incentive: 0 },
    { day: 'Fri', trips: 2, incentive: 625 },
  ]

  const monthlyIncentivesData = [
    { month: 'Aug', incentive: 1800 },
    { month: 'Sep', incentive: 2200 },
    { month: 'Oct', incentive: 1950 },
    { month: 'Nov', incentive: 2400 },
    { month: 'Dec', incentive: 2100 },
    { month: 'Jan', incentive: 2500 },
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
        <h1 className="text-3xl font-bold text-gray-900">Employee Dashboard</h1>
        <p className="text-gray-600 mt-2">Track your trips and earned incentives</p>
      </div>

      {/* Stats Cards */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        <StatCard
          title="Total Trips"
          value={stats.totalTrips}
          subtitle={`${stats.regularTrips} regular, ${stats.extraTrips} extra`}
          icon={MapPin}
          color="bg-blue-500"
        />
        <StatCard
          title="Total Incentives"
          value={`₹${stats.totalIncentives.toLocaleString()}`}
          subtitle="This month"
          icon={DollarSign}
          color="bg-green-500"
        />
        <StatCard
          title="Total Distance"
          value={`${stats.totalDistance} km`}
          subtitle="This month"
          icon={TrendingUp}
          color="bg-purple-500"
        />
      </div>

      {/* Incentives Highlight */}
      <div className="card bg-gradient-to-r from-green-500 to-green-600 text-white">
        <div className="flex items-center justify-between">
          <div>
            <p className="text-sm font-medium opacity-90">Pending Incentives</p>
            <p className="text-4xl font-bold mt-2">₹{stats.pendingIncentives.toLocaleString()}</p>
            <p className="text-sm opacity-90 mt-2">Will be credited in next cycle</p>
          </div>
          <Award size={48} className="opacity-80" />
        </div>
      </div>

      {/* Charts */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* Weekly Activity */}
        <div className="card">
          <h3 className="text-lg font-semibold text-gray-900 mb-4">This Week's Activity</h3>
          <ResponsiveContainer width="100%" height={300}>
            <BarChart data={weeklyTripsData}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="day" />
              <YAxis />
              <Tooltip />
              <Legend />
              <Bar dataKey="trips" fill="#0ea5e9" name="Trips" />
              <Bar dataKey="incentive" fill="#10b981" name="Incentive (₹)" />
            </BarChart>
          </ResponsiveContainer>
        </div>

        {/* Monthly Incentives Trend */}
        <div className="card">
          <h3 className="text-lg font-semibold text-gray-900 mb-4">Monthly Incentives Trend</h3>
          <ResponsiveContainer width="100%" height={300}>
            <LineChart data={monthlyIncentivesData}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="month" />
              <YAxis />
              <Tooltip />
              <Legend />
              <Line 
                type="monotone" 
                dataKey="incentive" 
                stroke="#10b981" 
                strokeWidth={3}
                name="Incentive (₹)"
              />
            </LineChart>
          </ResponsiveContainer>
        </div>
      </div>

      {/* Recent Trips */}
      <div className="card">
        <h3 className="text-lg font-semibold text-gray-900 mb-4">Recent Trips</h3>
        <div className="overflow-x-auto">
          <table className="min-w-full divide-y divide-gray-200">
            <thead className="bg-gray-50">
              <tr>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Date
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Route
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Distance
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Duration
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Extra Hours
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Incentive
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Vendor
                </th>
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-200">
              {recentTrips.map((trip) => (
                <tr key={trip.id} className="hover:bg-gray-50">
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                    {format(new Date(trip.date), 'MMM dd')}
                  </td>
                  <td className="px-6 py-4 text-sm text-gray-900">
                    {trip.pickup} → {trip.dropoff}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                    {trip.distance} km
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                    {trip.duration} min
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                    {trip.extraHours > 0 ? (
                      <span className="text-orange-600 font-medium">
                        {trip.extraHours} hrs
                      </span>
                    ) : (
                      <span className="text-gray-400">None</span>
                    )}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm font-medium">
                    {trip.incentive > 0 ? (
                      <span className="text-green-600">₹{trip.incentive}</span>
                    ) : (
                      <span className="text-gray-400">₹0</span>
                    )}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                    {trip.vendor}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      {/* Info Card */}
      <div className="card bg-blue-50 border-2 border-blue-200">
        <div className="flex items-start">
          <div className="flex-shrink-0">
            <Award className="text-blue-600" size={24} />
          </div>
          <div className="ml-3">
            <h3 className="text-sm font-medium text-blue-800">Incentive Information</h3>
            <div className="mt-2 text-sm text-blue-700">
              <p>• Incentives are calculated based on extra hours worked beyond regular commute time</p>
              <p className="mt-1">• Rate: ₹250 per extra hour</p>
              <p className="mt-1">• Incentives are credited at the end of each month</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}

export default EmployeeDashboard


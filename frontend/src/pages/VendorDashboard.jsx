import { useState, useEffect } from 'react'
import { useAuth } from '../contexts/AuthContext'
import { tripApi, vendorApi } from '../utils/api'
import { 
  TrendingUp, MapPin, DollarSign, Calendar, 
  CheckCircle, Clock, Download, FileText, AlertCircle
} from 'lucide-react'
import { 
  LineChart, Line, BarChart, Bar, PieChart, Pie, Cell,
  XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer 
} from 'recharts'
import * as XLSX from 'xlsx'
import toast from 'react-hot-toast'

const VendorDashboard = () => {
  const { user } = useAuth()
  const [loading, setLoading] = useState(true)
  const [selectedMonth, setSelectedMonth] = useState(new Date().toISOString().slice(0, 7))
  const [stats, setStats] = useState({
    totalTrips: 0,
    completedTrips: 0,
    pendingTrips: 0,
    totalRevenue: 0,
    pendingPayments: 0,
    incentives: 0,
    extraKmIncentives: 0,
    extraHourIncentives: 0,
  })
  const [trips, setTrips] = useState([])
  const [billingDetails, setBillingDetails] = useState(null)
  const [monthlyData, setMonthlyData] = useState([])

  useEffect(() => {
    if (user?.vendorId) {
      fetchVendorData()
    }
  }, [selectedMonth, user?.vendorId])

  const fetchVendorData = async () => {
    setLoading(true)
    try {
      // Fetch vendor's billing models
      const billingModelsResponse = await tripApi.get(`/billing-models/vendor/${user.vendorId}/active`)
      const models = billingModelsResponse.data
      
      if (models && models.length > 0) {
        // For now, use the first billing model (most common scenario)
        // In future, we can let vendor select which client's data to view
        const primaryModel = models[0]
        setBillingDetails(primaryModel)
      }

      // TODO: Fetch real trip data from trip-service
      // const tripsResponse = await tripApi.get(`/trips/vendor/${user.vendorId}?month=${selectedMonth}`)
      // setTrips(tripsResponse.data)
      
      // TODO: Fetch monthly revenue trends
      // const monthlyResponse = await tripApi.get(`/trips/vendor/${user.vendorId}/monthly-stats`)
      // setMonthlyData(monthlyResponse.data)

      // Start with empty data - will populate when employees take trips
      setTrips([])
      setMonthlyData([])

      // Calculate stats (all zeros initially)
      setStats({
        totalTrips: 0,
        completedTrips: 0,
        pendingTrips: 0,
        totalRevenue: 0,
        pendingPayments: 0,
        incentives: 0,
        extraKmIncentives: 0,
        extraHourIncentives: 0,
      })

    } catch (error) {
      console.error('Error fetching vendor data:', error)
      toast.error('Failed to load vendor data')
    } finally {
      setLoading(false)
    }
  }

  const exportBillingReport = () => {
    // Trip details
    const tripData = trips.map(trip => ({
      'Date': trip.date,
      'Client': trip.client,
      'Employee': trip.employee,
      'Employee ID': trip.employeeId,
      'Pickup': trip.pickup,
      'Dropoff': trip.dropoff,
      'Distance (km)': trip.distance,
      'Duration (min)': trip.duration,
      'Base Amount': `₹${trip.baseAmount}`,
      'Extra Km': trip.extraKm,
      'Extra Km Amount': `₹${trip.extraKmAmount}`,
      'Extra Hours': trip.extraHours,
      'Extra Hour Amount': `₹${trip.extraHourAmount}`,
      'Total Amount': `₹${trip.totalAmount}`,
      'Status': trip.status,
      'Payment Status': trip.paymentStatus,
    }))

    // Summary
    const summaryData = [{
      'Total Trips': stats.totalTrips,
      'Completed Trips': stats.completedTrips,
      'Total Revenue': `₹${stats.totalRevenue}`,
      'Total Incentives': `₹${stats.incentives}`,
      'Extra Km Incentives': `₹${stats.extraKmIncentives}`,
      'Extra Hour Incentives': `₹${stats.extraHourIncentives}`,
      'Pending Payments': `₹${stats.pendingPayments}`,
    }]

    // Create workbook
    const wb = XLSX.utils.book_new()
    const ws1 = XLSX.utils.json_to_sheet(summaryData)
    const ws2 = XLSX.utils.json_to_sheet(tripData)
    
    XLSX.utils.book_append_sheet(wb, ws1, 'Summary')
    XLSX.utils.book_append_sheet(wb, ws2, 'Trip Details')
    
    const filename = `Vendor_Billing_Report_${selectedMonth}.xlsx`
    XLSX.writeFile(wb, filename)
    toast.success('Report exported successfully!')
  }

  const COLORS = ['#3B82F6', '#10B981', '#F59E0B', '#EF4444']

  if (loading) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary-600"></div>
      </div>
    )
  }

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex flex-col md:flex-row md:items-center md:justify-between">
        <div>
          <h1 className="text-3xl font-bold text-gray-900">Vendor Dashboard</h1>
          <p className="text-gray-600 mt-1">
            Welcome back, {user?.username}
          </p>
        </div>
        <div className="flex items-center gap-4 mt-4 md:mt-0">
          <div className="flex items-center gap-2">
            <Calendar className="text-gray-400" size={20} />
            <input
              type="month"
              value={selectedMonth}
              onChange={(e) => setSelectedMonth(e.target.value)}
              className="input text-sm"
            />
          </div>
          <button
            onClick={exportBillingReport}
            className="btn btn-primary flex items-center gap-2"
          >
            <Download size={18} />
            Export Report
          </button>
        </div>
      </div>

      {/* Stats Cards */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        <div className="card bg-gradient-to-br from-blue-500 to-blue-600 text-white">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-blue-100 text-sm font-medium">Total Trips</p>
              <p className="text-3xl font-bold mt-2">{stats.totalTrips}</p>
              <p className="text-blue-100 text-xs mt-1">
                {stats.completedTrips} completed
              </p>
            </div>
            <MapPin size={32} className="opacity-80" />
          </div>
        </div>

        <div className="card bg-gradient-to-br from-green-500 to-green-600 text-white">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-green-100 text-sm font-medium">Total Revenue</p>
              <p className="text-3xl font-bold mt-2">₹{stats.totalRevenue.toLocaleString()}</p>
              <p className="text-green-100 text-xs mt-1">
                This month
              </p>
            </div>
            <DollarSign size={32} className="opacity-80" />
          </div>
        </div>

        <div className="card bg-gradient-to-br from-orange-500 to-orange-600 text-white">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-orange-100 text-sm font-medium">Incentives Earned</p>
              <p className="text-3xl font-bold mt-2">₹{stats.incentives.toLocaleString()}</p>
              <p className="text-orange-100 text-xs mt-1">
                Extra km & hours
              </p>
            </div>
            <TrendingUp size={32} className="opacity-80" />
          </div>
        </div>

        <div className="card bg-gradient-to-br from-purple-500 to-purple-600 text-white">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-purple-100 text-sm font-medium">Pending Payments</p>
              <p className="text-3xl font-bold mt-2">₹{stats.pendingPayments.toLocaleString()}</p>
              <p className="text-purple-100 text-xs mt-1">
                Awaiting settlement
              </p>
            </div>
            <Clock size={32} className="opacity-80" />
          </div>
        </div>
      </div>

      {/* Billing Model Details */}
      {billingDetails && (
        <div className="card bg-gradient-to-br from-indigo-50 to-blue-50 border-2 border-indigo-200">
          <div className="flex items-start justify-between mb-4">
            <div>
              <h3 className="text-lg font-semibold text-gray-900 flex items-center gap-2">
                <FileText className="text-indigo-600" size={20} />
                Your Billing Model: {billingDetails.modelType}
              </h3>
              <p className="text-sm text-gray-600 mt-1">
                Current month billing breakdown
              </p>
            </div>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
            {billingDetails.modelType === 'PACKAGE' || billingDetails.modelType === 'HYBRID' ? (
              <>
                <div className="bg-white rounded-lg p-4 border border-indigo-200">
                  <p className="text-xs text-gray-600">Package Rate</p>
                  <p className="text-2xl font-bold text-indigo-600">₹{billingDetails.packageMonthlyRate?.toLocaleString()}</p>
                  <p className="text-xs text-gray-500 mt-1">Monthly fixed</p>
                </div>
                <div className="bg-white rounded-lg p-4 border border-indigo-200">
                  <p className="text-xs text-gray-600">Included Trips</p>
                  <p className="text-2xl font-bold text-gray-900">
                    {billingDetails.tripsCompleted}/{billingDetails.packageTripsIncluded}
                  </p>
                  <p className="text-xs text-gray-500 mt-1">
                    {billingDetails.tripsOverLimit > 0 ? `${billingDetails.tripsOverLimit} over limit` : 'Within limit'}
                  </p>
                </div>
                <div className="bg-white rounded-lg p-4 border border-indigo-200">
                  <p className="text-xs text-gray-600">Included Kms</p>
                  <p className="text-2xl font-bold text-gray-900">
                    {billingDetails.totalKmsCompleted}/{billingDetails.packageKmsIncluded}
                  </p>
                  <p className="text-xs text-gray-500 mt-1">
                    {billingDetails.kmsOverLimit > 0 ? `${billingDetails.kmsOverLimit} km over` : 'Within limit'}
                  </p>
                </div>
              </>
            ) : null}
            
            <div className="bg-white rounded-lg p-4 border border-indigo-200">
              <p className="text-xs text-gray-600">Extra Charges</p>
              <p className="text-2xl font-bold text-green-600">₹{stats.incentives.toLocaleString()}</p>
              <p className="text-xs text-gray-500 mt-1">Km: ₹{stats.extraKmIncentives} | Hr: ₹{stats.extraHourIncentives}</p>
            </div>
          </div>

          <div className="mt-4 bg-white rounded-lg p-4 border border-indigo-200">
            <div className="grid grid-cols-2 md:grid-cols-4 gap-4 text-sm">
              <div>
                <span className="text-gray-600">Rate/Trip:</span>
                <span className="font-semibold ml-2">₹{billingDetails.ratePerTrip}</span>
              </div>
              <div>
                <span className="text-gray-600">Rate/Km:</span>
                <span className="font-semibold ml-2">₹{billingDetails.ratePerKm}</span>
              </div>
              <div>
                <span className="text-gray-600">Extra Km:</span>
                <span className="font-semibold ml-2">₹{billingDetails.extraKmRate}/km</span>
              </div>
              <div>
                <span className="text-gray-600">Extra Hour:</span>
                <span className="font-semibold ml-2">₹{billingDetails.extraHourRate}/hr</span>
              </div>
            </div>
          </div>
        </div>
      )}

      {/* Charts */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* Revenue Trend */}
        <div className="card">
          <h3 className="text-lg font-semibold text-gray-900 mb-4 flex items-center gap-2">
            <TrendingUp size={20} className="text-primary-600" />
            Revenue & Incentives Trend
          </h3>
          <ResponsiveContainer width="100%" height={300}>
            <LineChart data={monthlyData}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="month" />
              <YAxis />
              <Tooltip />
              <Legend />
              <Line type="monotone" dataKey="revenue" stroke="#3B82F6" strokeWidth={2} name="Revenue" />
              <Line type="monotone" dataKey="incentives" stroke="#10B981" strokeWidth={2} name="Incentives" />
            </LineChart>
          </ResponsiveContainer>
        </div>

        {/* Trip Status Distribution */}
        <div className="card">
          <h3 className="text-lg font-semibold text-gray-900 mb-4 flex items-center gap-2">
            <MapPin size={20} className="text-primary-600" />
            Trip Statistics
          </h3>
          <ResponsiveContainer width="100%" height={300}>
            <BarChart data={monthlyData}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="month" />
              <YAxis />
              <Tooltip />
              <Legend />
              <Bar dataKey="trips" fill="#3B82F6" name="Trips" />
            </BarChart>
          </ResponsiveContainer>
        </div>
      </div>

      {/* Recent Trips Table */}
      <div className="card">
        <div className="flex items-center justify-between mb-4">
          <h3 className="text-lg font-semibold text-gray-900 flex items-center gap-2">
            <MapPin size={20} className="text-primary-600" />
            Recent Trips with Incentive Details
          </h3>
        </div>
        
        <div className="overflow-x-auto">
          <table className="min-w-full divide-y divide-gray-200">
            <thead className="bg-gray-50">
              <tr>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Date</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Client/Employee</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Route</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Distance/Time</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Base</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Extra Charges</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Total</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Status</th>
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-200">
              {trips.map((trip) => (
                <tr key={trip.id} className="hover:bg-gray-50">
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-700">
                    {trip.date}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <div className="text-sm font-medium text-gray-900">{trip.client}</div>
                    <div className="text-xs text-gray-500">{trip.employee} ({trip.employeeId})</div>
                  </td>
                  <td className="px-6 py-4">
                    <div className="text-xs">
                      <div className="text-gray-900">📍 {trip.pickup}</div>
                      <div className="text-gray-600">📍 {trip.dropoff}</div>
                    </div>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm">
                    <div>{trip.distance} km</div>
                    <div className="text-xs text-gray-500">{trip.duration} min</div>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <span className="font-semibold text-gray-900">₹{trip.baseAmount}</span>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    {trip.extraKmAmount > 0 || trip.extraHourAmount > 0 ? (
                      <div className="text-xs">
                        {trip.extraKmAmount > 0 && (
                          <div className="text-green-600 font-medium">
                            +₹{trip.extraKmAmount} ({trip.extraKm} km)
                          </div>
                        )}
                        {trip.extraHourAmount > 0 && (
                          <div className="text-blue-600 font-medium">
                            +₹{trip.extraHourAmount} ({trip.extraHours} hr)
                          </div>
                        )}
                      </div>
                    ) : (
                      <span className="text-gray-400">-</span>
                    )}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <span className="font-bold text-green-600">₹{trip.totalAmount}</span>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <div className="flex flex-col gap-1">
                      <span className={`px-2 py-1 text-xs font-medium rounded-full ${
                        trip.status === 'completed' 
                          ? 'bg-green-100 text-green-800' 
                          : 'bg-yellow-100 text-yellow-800'
                      }`}>
                        {trip.status}
                      </span>
                      <span className={`px-2 py-1 text-xs font-medium rounded-full ${
                        trip.paymentStatus === 'paid' 
                          ? 'bg-blue-100 text-blue-800' 
                          : 'bg-orange-100 text-orange-800'
                      }`}>
                        {trip.paymentStatus}
                      </span>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  )
}

export default VendorDashboard

import { useState, useEffect } from 'react'
import { useAuth } from '../contexts/AuthContext'
import { authApi } from '../utils/api'
import api from '../utils/api'
import { 
  Building2, Users, Car, TrendingUp, FileText, 
  Calendar, DollarSign, Download, Filter, Search
} from 'lucide-react'
import toast from 'react-hot-toast'
import * as XLSX from 'xlsx'
import { BarChart, Bar, LineChart, Line, PieChart, Pie, Cell, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts'

const ClientDashboard = () => {
  const { user } = useAuth()
  const [loading, setLoading] = useState(true)
  const [stats, setStats] = useState({
    totalEmployees: 0,
    activeVendors: 0,
    monthlySpending: 0,
    totalTrips: 0,
  })
  const [trips, setTrips] = useState([])
  const [vendorPayments, setVendorPayments] = useState([])
  const [employees, setEmployees] = useState([])
  const [monthlyData, setMonthlyData] = useState([])
  const [selectedMonth, setSelectedMonth] = useState(new Date().toISOString().slice(0, 7))
  const [searchTerm, setSearchTerm] = useState('')

  useEffect(() => {
    if (user?.tenantId) {
      fetchDashboardData()
    }
  }, [selectedMonth, user?.tenantId])

  const fetchDashboardData = async () => {
    setLoading(true)
    try {
      // Fetch employees for this tenant
      const employeesResponse = await authApi.get(`/users/tenant/${user.tenantId}`)
      const employeesList = employeesResponse.data.filter(u => u.role === 'EMPLOYEE')
      
      // Fetch all trips for the client
      const tripsResponse = await api.get(`http://localhost:4020/trips/client/${user.tenantId}`)
      const allTrips = tripsResponse.data || []
      
      // Fetch all vendors to get names
      const allVendorsResponse = await api.get(`http://localhost:4015/vendors`)
      const allVendors = allVendorsResponse.data || []
      const vendorMap = {}
      allVendors.forEach(v => {
        vendorMap[v.id] = v.companyName
      })
      
      // Fetch active vendors with packages for this client
      const vendorsResponse = await api.get(`http://localhost:4015/vendors/client/${user.tenantId}/with-packages`)
      const activeVendorsList = vendorsResponse.data || []
      
      // Calculate employee spending
      const employeeSpendingMap = {}
      let totalSpending = 0
      
      allTrips.forEach(trip => {
        const employeeId = trip.employeeId
        if (!employeeSpendingMap[employeeId]) {
          employeeSpendingMap[employeeId] = {
            trips: 0,
            spending: 0
          }
        }
        employeeSpendingMap[employeeId].trips++
        employeeSpendingMap[employeeId].spending += parseFloat(trip.totalCost || 0)
        totalSpending += parseFloat(trip.totalCost || 0)
      })
      
      // Map employees with their spending data
      const employeesWithSpending = employeesList.map(emp => ({
        id: emp.id,
        name: `${emp.firstName || ''} ${emp.lastName || ''}`.trim() || emp.email,
        email: emp.email,
        department: emp.department || 'N/A',
        trips: employeeSpendingMap[emp.id]?.trips || 0,
        spending: employeeSpendingMap[emp.id]?.spending || 0,
      }))
      
      setEmployees(employeesWithSpending)
      
      // Calculate vendor payment distribution
      const vendorPaymentMap = {}
      allTrips.forEach(trip => {
        const vendorId = trip.vendorId
        if (!vendorPaymentMap[vendorId]) {
          vendorPaymentMap[vendorId] = {
            vendorId: vendorId,
            vendor: vendorMap[vendorId] || 'Unknown Vendor',
            trips: 0,
            amount: 0
          }
        }
        vendorPaymentMap[vendorId].trips++
        vendorPaymentMap[vendorId].amount += parseFloat(trip.totalCost || 0)
      })
      
      const vendorPaymentsArray = Object.values(vendorPaymentMap).map(vendor => ({
        ...vendor,
        percentage: totalSpending > 0 ? Math.round((vendor.amount / totalSpending) * 100) : 0
      }))
      
      setVendorPayments(vendorPaymentsArray)
      
      // Calculate monthly data (last 6 months)
      const monthlyMap = {}
      allTrips.forEach(trip => {
        if (trip.tripStartTime) {
          const month = trip.tripStartTime.substring(0, 7) // YYYY-MM
          if (!monthlyMap[month]) {
            monthlyMap[month] = { month, trips: 0, spending: 0 }
          }
          monthlyMap[month].trips++
          monthlyMap[month].spending += parseFloat(trip.totalCost || 0)
        }
      })
      
      const monthlyArray = Object.values(monthlyMap).sort((a, b) => a.month.localeCompare(b.month))
      setMonthlyData(monthlyArray.slice(-6)) // Last 6 months
      
      // Calculate stats
      setStats({
        totalEmployees: employeesList.length,
        activeVendors: activeVendorsList.length,
        monthlySpending: totalSpending,
        totalTrips: allTrips.length,
      })
      
    } catch (error) {
      console.error('Error fetching dashboard data:', error)
      toast.error('Failed to fetch dashboard data')
      // Set empty state on error
      setEmployees([])
      setStats({
        totalEmployees: 0,
        activeVendors: 0,
        monthlySpending: 0,
        totalTrips: 0,
      })
    } finally {
      setLoading(false)
    }
  }

  const exportToExcel = () => {
    // Employee spending report
    const employeeData = employees.map(emp => ({
      'Employee Name': emp.name,
      'Email': emp.email,
      'Department': emp.department,
      'Total Trips': emp.trips,
      'Total Spending': `₹${emp.spending}`,
    }))
    
    // Vendor payments report
    const vendorData = vendorPayments.map(vendor => ({
      'Vendor Name': vendor.vendor,
      'Total Trips': vendor.trips,
      'Amount Paid': `₹${vendor.amount}`,
      'Percentage': `${vendor.percentage}%`,
    }))
    
    // Create workbook
    const wb = XLSX.utils.book_new()
    const ws1 = XLSX.utils.json_to_sheet(employeeData)
    const ws2 = XLSX.utils.json_to_sheet(vendorData)
    
    XLSX.utils.book_append_sheet(wb, ws1, 'Employee Spending')
    XLSX.utils.book_append_sheet(wb, ws2, 'Vendor Payments')
    
    // Generate filename with current date
    const filename = `Company_Report_${selectedMonth}.xlsx`
    XLSX.writeFile(wb, filename)
  }

  const filteredEmployees = employees.filter(emp =>
    emp.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
    emp.email.toLowerCase().includes(searchTerm.toLowerCase()) ||
    emp.department.toLowerCase().includes(searchTerm.toLowerCase())
  )

  const COLORS = ['#3B82F6', '#10B981', '#F59E0B', '#EF4444', '#8B5CF6']

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
          <h1 className="text-3xl font-bold text-gray-900">Company Dashboard</h1>
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
            onClick={exportToExcel}
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
              <p className="text-blue-100 text-sm font-medium">Total Employees</p>
              <p className="text-3xl font-bold mt-2">{stats.totalEmployees}</p>
            </div>
            <div className="bg-white/20 p-3 rounded-lg">
              <Users size={28} />
            </div>
          </div>
        </div>

        <div className="card bg-gradient-to-br from-green-500 to-green-600 text-white">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-green-100 text-sm font-medium">Active Vendors</p>
              <p className="text-3xl font-bold mt-2">{stats.activeVendors}</p>
              <p className="text-xs text-green-100 mt-1">Available for booking</p>
            </div>
            <div className="bg-white/20 p-3 rounded-lg">
              <Car size={28} />
            </div>
          </div>
        </div>

        <div className="card bg-gradient-to-br from-purple-500 to-purple-600 text-white">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-purple-100 text-sm font-medium">Monthly Spending</p>
              <p className="text-3xl font-bold mt-2">₹{stats.monthlySpending.toLocaleString()}</p>
            </div>
            <div className="bg-white/20 p-3 rounded-lg">
              <DollarSign size={28} />
            </div>
          </div>
        </div>

        <div className="card bg-gradient-to-br from-orange-500 to-orange-600 text-white">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-orange-100 text-sm font-medium">Total Trips</p>
              <p className="text-3xl font-bold mt-2">{stats.totalTrips}</p>
            </div>
            <div className="bg-white/20 p-3 rounded-lg">
              <TrendingUp size={28} />
            </div>
          </div>
        </div>
      </div>

      {/* Charts */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* Monthly Trend */}
        <div className="card">
          <h3 className="text-lg font-semibold text-gray-900 mb-4 flex items-center gap-2">
            <TrendingUp size={20} className="text-primary-600" />
            Monthly Spending Trend
          </h3>
          <ResponsiveContainer width="100%" height={300}>
            <LineChart data={monthlyData}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="month" />
              <YAxis />
              <Tooltip 
                formatter={(value, name) => {
                  if (name === 'spending') return [`₹${value.toLocaleString()}`, 'Spending']
                  return [value, name]
                }}
              />
              <Legend />
              <Line type="monotone" dataKey="spending" stroke="#3B82F6" strokeWidth={2} name="Spending" />
              <Line type="monotone" dataKey="trips" stroke="#10B981" strokeWidth={2} name="Trips" />
            </LineChart>
          </ResponsiveContainer>
        </div>

        {/* Vendor Distribution */}
        <div className="card">
          <h3 className="text-lg font-semibold text-gray-900 mb-4 flex items-center gap-2">
            <Car size={20} className="text-primary-600" />
            Vendor Payment Distribution
          </h3>
          <ResponsiveContainer width="100%" height={300}>
            <PieChart>
              <Pie
                data={vendorPayments}
                cx="50%"
                cy="50%"
                labelLine={false}
                label={({ vendor, percentage }) => `${vendor}: ${percentage}%`}
                outerRadius={100}
                fill="#8884d8"
                dataKey="amount"
              >
                {vendorPayments.map((entry, index) => (
                  <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                ))}
              </Pie>
              <Tooltip formatter={(value) => `₹${value.toLocaleString()}`} />
            </PieChart>
          </ResponsiveContainer>
        </div>
      </div>

      {/* Vendor Payments Table */}
      <div className="card">
        <div className="flex items-center justify-between mb-4">
          <h3 className="text-lg font-semibold text-gray-900 flex items-center gap-2">
            <Car size={20} className="text-primary-600" />
            Vendor Payments Summary
          </h3>
        </div>
        <div className="overflow-x-auto">
          <table className="min-w-full divide-y divide-gray-200">
            <thead className="bg-gray-50">
              <tr>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Vendor
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Total Trips
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Amount Paid
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Percentage
                </th>
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-200">
              {vendorPayments.length === 0 ? (
                <tr>
                  <td colSpan="4" className="px-6 py-12 text-center text-gray-500">
                    No vendor payments recorded yet
                  </td>
                </tr>
              ) : (
                vendorPayments.map((vendor, index) => (
                  <tr key={index} className="hover:bg-gray-50">
                    <td className="px-6 py-4 whitespace-nowrap">
                      <div className="font-medium text-gray-900">{vendor.vendor}</div>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-gray-700">
                      {vendor.trips}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <span className="font-semibold text-green-600">
                        ₹{vendor.amount.toLocaleString()}
                      </span>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <div className="flex items-center">
                        <div className="w-full bg-gray-200 rounded-full h-2 mr-2" style={{ maxWidth: '100px' }}>
                          <div 
                            className="bg-primary-600 h-2 rounded-full" 
                            style={{ width: `${vendor.percentage}%` }}
                          ></div>
                        </div>
                        <span className="text-sm text-gray-600">{vendor.percentage}%</span>
                      </div>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </div>

      {/* Employee Spending Table */}
      <div className="card">
        <div className="flex items-center justify-between mb-4">
          <h3 className="text-lg font-semibold text-gray-900 flex items-center gap-2">
            <Users size={20} className="text-primary-600" />
            Employee Spending Details
          </h3>
          <div className="relative">
            <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400" size={18} />
            <input
              type="text"
              placeholder="Search employees..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary-500 focus:border-primary-500"
            />
          </div>
        </div>
        <div className="overflow-x-auto">
          <table className="min-w-full divide-y divide-gray-200">
            <thead className="bg-gray-50">
              <tr>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Employee
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Department
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Total Trips
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Total Spending
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Actions
                </th>
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-200">
              {filteredEmployees.length === 0 ? (
                <tr>
                  <td colSpan="5" className="px-6 py-12 text-center text-gray-500">
                    No employees found
                  </td>
                </tr>
              ) : (
                filteredEmployees.map((employee) => (
                  <tr key={employee.id} className="hover:bg-gray-50">
                    <td className="px-6 py-4 whitespace-nowrap">
                      <div>
                        <div className="font-medium text-gray-900">{employee.name}</div>
                        <div className="text-sm text-gray-500">{employee.email}</div>
                      </div>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <span className="px-2 py-1 text-xs font-medium bg-blue-100 text-blue-800 rounded-full">
                        {employee.department}
                      </span>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-gray-700">
                      {employee.trips}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <span className={`font-semibold ${employee.spending > 0 ? 'text-green-600' : 'text-gray-400'}`}>
                        ₹{employee.spending.toLocaleString()}
                      </span>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm">
                      <button className="text-primary-600 hover:text-primary-700 font-medium">
                        View Details
                      </button>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  )
}

export default ClientDashboard


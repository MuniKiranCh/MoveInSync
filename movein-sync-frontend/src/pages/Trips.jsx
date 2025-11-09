import { useState, useEffect } from 'react'
import { Plus, Search, Filter, MapPin, Calendar, Clock } from 'lucide-react'
import { format } from 'date-fns'
import api from '../utils/api'
import toast from 'react-hot-toast'

const Trips = () => {
  const [trips, setTrips] = useState([])
  const [loading, setLoading] = useState(false)
  const [searchTerm, setSearchTerm] = useState('')
  const [filterStatus, setFilterStatus] = useState('all')
  const [filterClient, setFilterClient] = useState('all')
  const [showModal, setShowModal] = useState(false)
  const [formData, setFormData] = useState({
    employeeName: '',
    clientName: '',
    vendorName: '',
    pickupLocation: '',
    dropoffLocation: '',
    distance: '',
    duration: '',
    extraHours: '',
    date: format(new Date(), 'yyyy-MM-dd'),
  })

  useEffect(() => {
    fetchTrips()
  }, [])

  const fetchTrips = async () => {
    setLoading(true)
    try {
      const mockData = [
        {
          id: 1,
          date: '2024-01-20',
          time: '09:15',
          employeeName: 'Rajesh Kumar',
          clientName: 'TechCorp India',
          vendorName: 'FastCabs',
          pickup: 'Whitefield',
          dropoff: 'MG Road',
          distance: 15.2,
          duration: 45,
          extraHours: 0,
          cost: 450,
          incentive: 0,
          status: 'completed',
        },
        {
          id: 2,
          date: '2024-01-20',
          time: '09:30',
          employeeName: 'Priya Sharma',
          clientName: 'Global Solutions',
          vendorName: 'QuickRide',
          pickup: 'Koramangala',
          dropoff: 'Electronic City',
          distance: 12.8,
          duration: 38,
          extraHours: 2,
          cost: 380,
          incentive: 500,
          status: 'completed',
        },
        {
          id: 3,
          date: '2024-01-20',
          time: '09:45',
          employeeName: 'Amit Patel',
          clientName: 'Startup Hub',
          vendorName: 'CityTransport',
          pickup: 'Indiranagar',
          dropoff: 'Whitefield',
          distance: 18.5,
          duration: 52,
          extraHours: 0,
          cost: 550,
          incentive: 0,
          status: 'in_progress',
        },
        {
          id: 4,
          date: '2024-01-19',
          time: '18:30',
          employeeName: 'Rajesh Kumar',
          clientName: 'TechCorp India',
          vendorName: 'FastCabs',
          pickup: 'MG Road',
          dropoff: 'Whitefield',
          distance: 15.2,
          duration: 50,
          extraHours: 1.5,
          cost: 450,
          incentive: 375,
          status: 'completed',
        },
      ]
      setTrips(mockData)
    } catch (error) {
      toast.error('Failed to load trips')
    } finally {
      setLoading(false)
    }
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    try {
      await api.post('/trips', formData)
      toast.success('Trip created successfully')
      setShowModal(false)
      resetForm()
      fetchTrips()
    } catch (error) {
      toast.error(error.response?.data?.message || 'Operation failed')
    }
  }

  const resetForm = () => {
    setFormData({
      employeeName: '',
      clientName: '',
      vendorName: '',
      pickupLocation: '',
      dropoffLocation: '',
      distance: '',
      duration: '',
      extraHours: '',
      date: format(new Date(), 'yyyy-MM-dd'),
    })
  }

  const filteredTrips = trips.filter(trip => {
    const matchesSearch = 
      trip.employeeName.toLowerCase().includes(searchTerm.toLowerCase()) ||
      trip.clientName.toLowerCase().includes(searchTerm.toLowerCase()) ||
      trip.vendorName.toLowerCase().includes(searchTerm.toLowerCase()) ||
      trip.pickup.toLowerCase().includes(searchTerm.toLowerCase()) ||
      trip.dropoff.toLowerCase().includes(searchTerm.toLowerCase())
    
    const matchesStatus = filterStatus === 'all' || trip.status === filterStatus
    const matchesClient = filterClient === 'all' || trip.clientName === filterClient
    
    return matchesSearch && matchesStatus && matchesClient
  })

  const totalDistance = filteredTrips.reduce((sum, trip) => sum + trip.distance, 0)
  const totalCost = filteredTrips.reduce((sum, trip) => sum + trip.cost, 0)
  const totalIncentives = filteredTrips.reduce((sum, trip) => sum + trip.incentive, 0)

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-3xl font-bold text-gray-900">Trips</h1>
          <p className="text-gray-600 mt-2">Manage and track all trips</p>
        </div>
        <button
          onClick={() => {
            resetForm()
            setShowModal(true)
          }}
          className="btn btn-primary flex items-center"
        >
          <Plus size={20} className="mr-2" />
          Add Trip
        </button>
      </div>

      {/* Summary Cards */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
        <div className="card">
          <p className="text-sm text-gray-600">Total Trips</p>
          <p className="text-2xl font-bold text-gray-900">{filteredTrips.length}</p>
        </div>
        <div className="card">
          <p className="text-sm text-gray-600">Total Distance</p>
          <p className="text-2xl font-bold text-gray-900">{totalDistance.toFixed(1)} km</p>
        </div>
        <div className="card">
          <p className="text-sm text-gray-600">Total Cost</p>
          <p className="text-2xl font-bold text-gray-900">₹{totalCost.toLocaleString()}</p>
        </div>
        <div className="card">
          <p className="text-sm text-gray-600">Total Incentives</p>
          <p className="text-2xl font-bold text-green-600">₹{totalIncentives.toLocaleString()}</p>
        </div>
      </div>

      {/* Filters */}
      <div className="card">
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          <div className="relative">
            <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400" size={20} />
            <input
              type="text"
              placeholder="Search trips..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="input pl-10"
            />
          </div>

          <div className="relative">
            <Filter className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400" size={20} />
            <select
              value={filterStatus}
              onChange={(e) => setFilterStatus(e.target.value)}
              className="input pl-10"
            >
              <option value="all">All Statuses</option>
              <option value="completed">Completed</option>
              <option value="in_progress">In Progress</option>
              <option value="cancelled">Cancelled</option>
            </select>
          </div>

          <div className="relative">
            <Filter className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400" size={20} />
            <select
              value={filterClient}
              onChange={(e) => setFilterClient(e.target.value)}
              className="input pl-10"
            >
              <option value="all">All Clients</option>
              <option value="TechCorp India">TechCorp India</option>
              <option value="Global Solutions">Global Solutions</option>
              <option value="Startup Hub">Startup Hub</option>
            </select>
          </div>
        </div>
      </div>

      {/* Trips Table */}
      <div className="card overflow-hidden">
        <div className="overflow-x-auto">
          <table className="min-w-full divide-y divide-gray-200">
            <thead className="bg-gray-50">
              <tr>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Date & Time
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Employee
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Client
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Vendor
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
                  Cost
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Status
                </th>
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-200">
              {loading ? (
                <tr>
                  <td colSpan="10" className="px-6 py-12 text-center">
                    <div className="flex justify-center">
                      <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary-600"></div>
                    </div>
                  </td>
                </tr>
              ) : filteredTrips.length === 0 ? (
                <tr>
                  <td colSpan="10" className="px-6 py-12 text-center text-gray-500">
                    No trips found
                  </td>
                </tr>
              ) : (
                filteredTrips.map((trip) => (
                  <tr key={trip.id} className="hover:bg-gray-50">
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                      <div className="flex items-center">
                        <Calendar size={16} className="mr-2 text-gray-400" />
                        {format(new Date(trip.date), 'MMM dd, yyyy')}
                        <Clock size={14} className="ml-3 mr-1 text-gray-400" />
                        {trip.time}
                      </div>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                      {trip.employeeName}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                      {trip.clientName}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                      {trip.vendorName}
                    </td>
                    <td className="px-6 py-4 text-sm text-gray-900">
                      <div className="flex items-center">
                        <MapPin size={14} className="mr-1 text-gray-400" />
                        {trip.pickup} → {trip.dropoff}
                      </div>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                      {trip.distance} km
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                      {trip.duration} min
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm">
                      {trip.extraHours > 0 ? (
                        <span className="text-orange-600 font-medium">
                          {trip.extraHours} hrs
                          <br />
                          <span className="text-xs text-green-600">
                            +₹{trip.incentive}
                          </span>
                        </span>
                      ) : (
                        <span className="text-gray-400">None</span>
                      )}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                      ₹{trip.cost}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <span className={`px-2 py-1 inline-flex text-xs leading-5 font-semibold rounded-full ${
                        trip.status === 'completed' 
                          ? 'bg-green-100 text-green-800'
                          : trip.status === 'in_progress'
                          ? 'bg-blue-100 text-blue-800'
                          : 'bg-red-100 text-red-800'
                      }`}>
                        {trip.status === 'completed' ? 'Completed' : 
                         trip.status === 'in_progress' ? 'In Progress' : 'Cancelled'}
                      </span>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </div>

      {/* Modal */}
      {showModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 z-50 flex items-center justify-center p-4">
          <div className="bg-white rounded-lg max-w-2xl w-full">
            <div className="p-6">
              <h2 className="text-2xl font-bold text-gray-900 mb-6">Add Trip</h2>
              
              <form onSubmit={handleSubmit} className="space-y-4">
                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2">
                      Date
                    </label>
                    <input
                      type="date"
                      value={formData.date}
                      onChange={(e) => setFormData({ ...formData, date: e.target.value })}
                      className="input"
                      required
                    />
                  </div>

                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2">
                      Employee
                    </label>
                    <select
                      value={formData.employeeName}
                      onChange={(e) => setFormData({ ...formData, employeeName: e.target.value })}
                      className="input"
                      required
                    >
                      <option value="">Select employee...</option>
                      <option value="Rajesh Kumar">Rajesh Kumar</option>
                      <option value="Priya Sharma">Priya Sharma</option>
                      <option value="Amit Patel">Amit Patel</option>
                    </select>
                  </div>
                </div>

                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2">
                      Client
                    </label>
                    <select
                      value={formData.clientName}
                      onChange={(e) => setFormData({ ...formData, clientName: e.target.value })}
                      className="input"
                      required
                    >
                      <option value="">Select client...</option>
                      <option value="TechCorp India">TechCorp India</option>
                      <option value="Global Solutions">Global Solutions</option>
                      <option value="Startup Hub">Startup Hub</option>
                    </select>
                  </div>

                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2">
                      Vendor
                    </label>
                    <select
                      value={formData.vendorName}
                      onChange={(e) => setFormData({ ...formData, vendorName: e.target.value })}
                      className="input"
                      required
                    >
                      <option value="">Select vendor...</option>
                      <option value="FastCabs">FastCabs</option>
                      <option value="QuickRide">QuickRide</option>
                      <option value="CityTransport">CityTransport</option>
                    </select>
                  </div>
                </div>

                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2">
                      Pickup Location
                    </label>
                    <input
                      type="text"
                      value={formData.pickupLocation}
                      onChange={(e) => setFormData({ ...formData, pickupLocation: e.target.value })}
                      className="input"
                      required
                    />
                  </div>

                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2">
                      Dropoff Location
                    </label>
                    <input
                      type="text"
                      value={formData.dropoffLocation}
                      onChange={(e) => setFormData({ ...formData, dropoffLocation: e.target.value })}
                      className="input"
                      required
                    />
                  </div>
                </div>

                <div className="grid grid-cols-3 gap-4">
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2">
                      Distance (km)
                    </label>
                    <input
                      type="number"
                      step="0.1"
                      value={formData.distance}
                      onChange={(e) => setFormData({ ...formData, distance: e.target.value })}
                      className="input"
                      required
                    />
                  </div>

                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2">
                      Duration (min)
                    </label>
                    <input
                      type="number"
                      value={formData.duration}
                      onChange={(e) => setFormData({ ...formData, duration: e.target.value })}
                      className="input"
                      required
                    />
                  </div>

                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2">
                      Extra Hours
                    </label>
                    <input
                      type="number"
                      step="0.5"
                      value={formData.extraHours}
                      onChange={(e) => setFormData({ ...formData, extraHours: e.target.value })}
                      className="input"
                      placeholder="0"
                    />
                  </div>
                </div>

                <div className="flex justify-end space-x-3 pt-4">
                  <button
                    type="button"
                    onClick={() => {
                      setShowModal(false)
                      resetForm()
                    }}
                    className="btn btn-secondary"
                  >
                    Cancel
                  </button>
                  <button type="submit" className="btn btn-primary">
                    Create Trip
                  </button>
                </div>
              </form>
            </div>
          </div>
        </div>
      )}
    </div>
  )
}

export default Trips


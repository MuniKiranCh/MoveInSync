import { useState, useEffect, Fragment } from 'react'
import { Plus, Edit2, Trash2, Search, UserCircle, ChevronDown, ChevronRight, Package } from 'lucide-react'
import api from '../utils/api'
import toast from 'react-hot-toast'

const Employees = () => {
  const [employees, setEmployees] = useState([])
  const [loading, setLoading] = useState(false)
  const [searchTerm, setSearchTerm] = useState('')
  const [showModal, setShowModal] = useState(false)
  const [editingEmployee, setEditingEmployee] = useState(null)
  const [expandedEmployee, setExpandedEmployee] = useState(null)
  const [employeePackages, setEmployeePackages] = useState({})
  const [loadingPackages, setLoadingPackages] = useState({})
  const [formData, setFormData] = useState({
    name: '',
    email: '',
    phone: '',
    clientName: '',
    department: '',
    homeAddress: '',
  })

  useEffect(() => {
    fetchEmployees()
  }, [])

  const fetchEmployees = async () => {
    setLoading(true)
    try {
      // Fetch all users with EMPLOYEE role from auth service
      const response = await api.get('/users')
      const employeesData = response.data
        .filter(u => u.role === 'EMPLOYEE')
        .map(emp => ({
          id: emp.id,
          name: `${emp.firstName || ''} ${emp.lastName || ''}`.trim() || emp.email,
          email: emp.email,
          phone: emp.phone || 'N/A',
          clientName: emp.tenantName || 'N/A',
          department: emp.department || 'N/A',
          homeAddress: emp.homeAddress || 'N/A',
          totalTrips: 0, // TODO: fetch from trip service
          incentivesEarned: 0, // TODO: fetch from billing service
        }))
      setEmployees(employeesData)
    } catch (error) {
      console.error('Error fetching employees:', error)
      toast.error('Failed to load employees')
    } finally {
      setLoading(false)
    }
  }

  const fetchEmployeePackages = async (employeeId) => {
    // Show immediately if already cached
    if (employeePackages[employeeId]) {
      setExpandedEmployee(employeeId)
      return
    }

    // Show loading and fetch
    setExpandedEmployee(employeeId)
    setLoadingPackages({ ...loadingPackages, [employeeId]: true })
    try {
      // Fetch from client-service
      const response = await api.get(`http://localhost:4010/api/package-assignments/employee/${employeeId}`)
      setEmployeePackages({ ...employeePackages, [employeeId]: response.data || [] })
    } catch (error) {
      console.error('Error fetching employee packages:', error)
      setEmployeePackages({ ...employeePackages, [employeeId]: [] })
    } finally {
      setLoadingPackages({ ...loadingPackages, [employeeId]: false })
    }
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    try {
      if (editingEmployee) {
        // TODO: Update employee endpoint when available
        await api.put(`/users/${editingEmployee.id}`, formData)
        toast.success('Employee updated successfully')
      } else {
        // Create new employee through auth service
        const employeeData = {
          email: formData.email,
          password: '12345678', // Default password
          firstName: formData.name.split(' ')[0],
          lastName: formData.name.split(' ').slice(1).join(' ') || '',
          phone: formData.phone,
          department: formData.department,
          role: 'EMPLOYEE',
        }
        await api.post('/create-user', employeeData)
        toast.success('Employee created successfully! Password: 12345678')
      }
      setShowModal(false)
      resetForm()
      fetchEmployees()
    } catch (error) {
      console.error('Error saving employee:', error)
      if (error.response?.status === 409) {
        toast.error('An employee with this email already exists')
      } else {
        toast.error(error.response?.data?.message || 'Operation failed')
      }
    }
  }

  const handleDelete = async (id) => {
    if (!confirm('Are you sure you want to delete this employee?')) return
    
    try {
      await api.delete(`/employees/${id}`)
      toast.success('Employee deleted successfully')
      fetchEmployees()
    } catch (error) {
      toast.error('Failed to delete employee')
    }
  }

  const resetForm = () => {
    setFormData({
      name: '',
      email: '',
      phone: '',
      clientName: '',
      department: '',
      homeAddress: '',
    })
    setEditingEmployee(null)
  }

  const filteredEmployees = employees.filter(employee =>
    employee.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
    employee.email.toLowerCase().includes(searchTerm.toLowerCase()) ||
    employee.clientName.toLowerCase().includes(searchTerm.toLowerCase())
  )

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-3xl font-bold text-gray-900">Employees</h1>
          <p className="text-gray-600 mt-2">Manage employee records</p>
        </div>
        <button
          onClick={() => {
            resetForm()
            setShowModal(true)
          }}
          className="btn btn-primary flex items-center"
        >
          <Plus size={20} className="mr-2" />
          Add Employee
        </button>
      </div>

      <div className="relative">
        <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400" size={20} />
        <input
          type="text"
          placeholder="Search employees..."
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          className="input pl-10"
        />
      </div>

      <div className="card overflow-hidden">
        <div className="overflow-x-auto">
          <table className="min-w-full divide-y divide-gray-200">
            <thead className="bg-gray-50">
              <tr>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Employee
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Client
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Department
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Status
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Contact
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Address
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Trips
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Incentives
                </th>
                <th className="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Actions
                </th>
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-200">
              {loading ? (
                <tr>
                  <td colSpan="9" className="px-6 py-12 text-center">
                    <div className="flex justify-center">
                      <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary-600"></div>
                    </div>
                  </td>
                </tr>
              ) : filteredEmployees.length === 0 ? (
                <tr>
                  <td colSpan="9" className="px-6 py-12 text-center text-gray-500">
                    No employees found
                  </td>
                </tr>
              ) : (
                filteredEmployees.map((employee) => (
                  <Fragment key={employee.id}>
                    <tr 
                      className="hover:bg-gray-50 cursor-pointer"
                      onMouseEnter={() => fetchEmployeePackages(employee.id)}
                      onMouseLeave={() => setExpandedEmployee(null)}
                    >
                      <td className="px-6 py-4 whitespace-nowrap">
                        <div className="flex items-center">
                          <div className="flex-shrink-0 h-10 w-10">
                            <div className="h-10 w-10 rounded-full bg-green-100 flex items-center justify-center">
                              <UserCircle className="text-green-600" size={24} />
                            </div>
                          </div>
                          <div className="ml-4">
                            <div className="text-sm font-medium text-gray-900 flex items-center gap-2">
                              {employee.name}
                              <Package size={14} className="text-gray-400" title="Hover to view packages" />
                            </div>
                            <div className="text-sm text-gray-500">{employee.email}</div>
                          </div>
                        </div>
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                        {employee.clientName}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                        {employee.department}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap">
                        {employeePackages[employee.id] && employeePackages[employee.id].length > 0 ? (
                          <span className="inline-flex items-center gap-1 px-3 py-1 rounded-full text-xs font-medium bg-green-100 text-green-800">
                            <svg className="w-3 h-3" fill="currentColor" viewBox="0 0 20 20">
                              <path fillRule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clipRule="evenodd" />
                            </svg>
                            Subscribed
                          </span>
                        ) : employeePackages[employee.id] && employeePackages[employee.id].length === 0 ? (
                          <span className="inline-flex items-center px-3 py-1 rounded-full text-xs font-medium bg-gray-100 text-gray-600">
                            Not Subscribed
                          </span>
                        ) : (
                          <span className="inline-flex items-center px-3 py-1 rounded-full text-xs font-medium bg-gray-100 text-gray-400">
                            Unknown
                          </span>
                        )}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                        {employee.phone}
                      </td>
                      <td className="px-6 py-4 text-sm text-gray-900">
                        {employee.homeAddress}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                        {employee.totalTrips}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-green-600">
                        ₹{employee.incentivesEarned?.toLocaleString()}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                        <button
                          onClick={() => {
                            setEditingEmployee(employee)
                            setFormData({
                              name: employee.name,
                              email: employee.email,
                              phone: employee.phone,
                              clientName: employee.clientName,
                              department: employee.department,
                              homeAddress: employee.homeAddress,
                            })
                            setShowModal(true)
                          }}
                          className="text-primary-600 hover:text-primary-900 mr-4"
                        >
                          <Edit2 size={18} />
                        </button>
                        <button
                          onClick={() => handleDelete(employee.id)}
                          className="text-red-600 hover:text-red-900"
                        >
                          <Trash2 size={18} />
                        </button>
                      </td>
                    </tr>
                    {expandedEmployee === employee.id && (
                      <tr 
                        key={`${employee.id}-packages`}
                        onMouseEnter={() => setExpandedEmployee(employee.id)}
                        onMouseLeave={() => setExpandedEmployee(null)}
                      >
                        <td colSpan="9" className="px-6 py-4 bg-blue-50 border-l-4 border-blue-400">
                          <div className="pl-4">
                            <h4 className="text-sm font-semibold text-gray-700 mb-3 flex items-center gap-2">
                              <Package size={16} className="text-blue-600" />
                              Assigned Packages
                            </h4>
                            {loadingPackages[employee.id] ? (
                              <div className="flex justify-center py-4">
                                <div className="animate-spin rounded-full h-6 w-6 border-b-2 border-primary-600"></div>
                              </div>
                            ) : employeePackages[employee.id]?.length === 0 ? (
                              <p className="text-sm text-gray-500 italic">No packages assigned yet</p>
                            ) : (
                              <div className="space-y-2">
                                {employeePackages[employee.id]?.map((pkg) => (
                                  <div
                                    key={pkg.id}
                                    className="flex items-center justify-between p-3 bg-white border border-gray-200 rounded-lg shadow-sm"
                                  >
                                    <div className="flex-1">
                                      <div className="flex items-center gap-2">
                                        <span className="font-medium text-sm text-gray-900">{pkg.packageName}</span>
                                        <span className={`px-2 py-1 text-xs font-medium rounded-full ${
                                          pkg.packageType === 'PACKAGE' ? 'bg-blue-100 text-blue-800' :
                                          pkg.packageType === 'TRIP' ? 'bg-purple-100 text-purple-800' :
                                          'bg-green-100 text-green-800'
                                        }`}>
                                          {pkg.packageType}
                                        </span>
                                      </div>
                                      <div className="text-xs text-gray-500 mt-1">
                                        Valid: {pkg.validFrom ? new Date(pkg.validFrom).toLocaleDateString() : 'N/A'} 
                                        {pkg.validUntil && ` - ${new Date(pkg.validUntil).toLocaleDateString()}`}
                                      </div>
                                      {pkg.notes && (
                                        <div className="text-xs text-gray-600 mt-1">Note: {pkg.notes}</div>
                                      )}
                                    </div>
                                    <div className="text-xs text-gray-500">
                                      Assigned: {pkg.assignedDate ? new Date(pkg.assignedDate).toLocaleDateString() : 'N/A'}
                                    </div>
                                  </div>
                                ))}
                              </div>
                            )}
                          </div>
                        </td>
                      </tr>
                    )}
                  </Fragment>
                ))
              )}
            </tbody>
          </table>
        </div>
      </div>

      {/* Modal */}
      {showModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 z-50 flex items-center justify-center p-4">
          <div className="bg-white rounded-lg max-w-md w-full">
            <div className="p-6">
              <h2 className="text-2xl font-bold text-gray-900 mb-6">
                {editingEmployee ? 'Edit Employee' : 'Add Employee'}
              </h2>
              
              <form onSubmit={handleSubmit} className="space-y-4">
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Full Name
                  </label>
                  <input
                    type="text"
                    value={formData.name}
                    onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                    className="input"
                    required
                  />
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Email
                  </label>
                  <input
                    type="email"
                    value={formData.email}
                    onChange={(e) => setFormData({ ...formData, email: e.target.value })}
                    className="input"
                    required
                  />
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Phone
                  </label>
                  <input
                    type="tel"
                    value={formData.phone}
                    onChange={(e) => setFormData({ ...formData, phone: e.target.value })}
                    className="input"
                    required
                  />
                </div>

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
                    <option value="">Select a client...</option>
                    <option value="TechCorp India">TechCorp India</option>
                    <option value="Global Solutions">Global Solutions</option>
                    <option value="Startup Hub">Startup Hub</option>
                  </select>
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Department
                  </label>
                  <input
                    type="text"
                    value={formData.department}
                    onChange={(e) => setFormData({ ...formData, department: e.target.value })}
                    className="input"
                    required
                  />
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Home Address
                  </label>
                  <textarea
                    value={formData.homeAddress}
                    onChange={(e) => setFormData({ ...formData, homeAddress: e.target.value })}
                    className="input"
                    rows="2"
                    required
                  ></textarea>
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
                    {editingEmployee ? 'Update' : 'Create'}
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

export default Employees


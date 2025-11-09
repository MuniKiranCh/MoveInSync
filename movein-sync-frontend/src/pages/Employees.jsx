import { useState, useEffect } from 'react'
import { Plus, Edit2, Trash2, Search, UserCircle } from 'lucide-react'
import api from '../utils/api'
import toast from 'react-hot-toast'

const Employees = () => {
  const [employees, setEmployees] = useState([])
  const [loading, setLoading] = useState(false)
  const [searchTerm, setSearchTerm] = useState('')
  const [showModal, setShowModal] = useState(false)
  const [editingEmployee, setEditingEmployee] = useState(null)
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
                  <td colSpan="8" className="px-6 py-12 text-center">
                    <div className="flex justify-center">
                      <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary-600"></div>
                    </div>
                  </td>
                </tr>
              ) : filteredEmployees.length === 0 ? (
                <tr>
                  <td colSpan="8" className="px-6 py-12 text-center text-gray-500">
                    No employees found
                  </td>
                </tr>
              ) : (
                filteredEmployees.map((employee) => (
                  <tr key={employee.id} className="hover:bg-gray-50">
                    <td className="px-6 py-4 whitespace-nowrap">
                      <div className="flex items-center">
                        <div className="flex-shrink-0 h-10 w-10">
                          <div className="h-10 w-10 rounded-full bg-green-100 flex items-center justify-center">
                            <UserCircle className="text-green-600" size={24} />
                          </div>
                        </div>
                        <div className="ml-4">
                          <div className="text-sm font-medium text-gray-900">{employee.name}</div>
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


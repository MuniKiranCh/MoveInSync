import { useState, useEffect } from 'react'
import { Package, Users, Check, X, Calendar } from 'lucide-react'
import { vendorApi, employeeApi } from '../utils/api'
import { useAuth } from '../contexts/AuthContext'
import toast from 'react-hot-toast'

const ClientVendors = () => {
  const { user } = useAuth()
  const [vendors, setVendors] = useState([])
  const [employees, setEmployees] = useState([])
  const [loading, setLoading] = useState(true)
  const [showAssignModal, setShowAssignModal] = useState(false)
  const [selectedPackage, setSelectedPackage] = useState(null)
  const [assignmentForm, setAssignmentForm] = useState({
    employeeId: '',
    validFrom: new Date().toISOString().split('T')[0],
    validUntil: '',
    notes: '',
  })

  useEffect(() => {
    if (user?.tenantId) {
      fetchVendorsAndEmployees()
    }
  }, [user?.tenantId])

  const fetchVendorsAndEmployees = async () => {
    setLoading(true)
    try {
      // Fetch all active vendors with their subscription packages from Vendor Service
      const vendorsResponse = await vendorApi.get(`/vendors/with-packages`)
      setVendors(vendorsResponse.data || [])

      // Fetch employees for this client from Auth Service
      try {
        const authResponse = await vendorApi.get(`http://localhost:4005/users/tenant/${user.tenantId}`)
        const employeesData = authResponse.data
          .filter(u => u.role === 'EMPLOYEE')
          .map(emp => ({
            id: emp.id,
            firstName: emp.firstName || '',
            lastName: emp.lastName || '',
            email: emp.email,
            employeeId: emp.employeeId || emp.id
          }))
        setEmployees(employeesData)
      } catch (authError) {
        console.error('Could not fetch employees:', authError.message)
        toast.error('Could not load employees. You can still view vendor packages.')
        setEmployees([])
      }
    } catch (error) {
      console.error('Error fetching vendors:', error)
      toast.error('Failed to load vendors')
    } finally {
      setLoading(false)
    }
  }

  const handleAssignPackage = (vendor, pkg) => {
    setSelectedPackage({
      vendorId: vendor.id,
      vendorName: vendor.name,
      packageId: pkg.id,
      packageName: pkg.packageName,
      packageType: pkg.packageType,
    })
    setShowAssignModal(true)
  }

  const handleSubmitAssignment = async (e) => {
    e.preventDefault()
    
    if (!assignmentForm.employeeId) {
      toast.error('Please select an employee')
      return
    }

    try {
      // Use client-service for package assignments
      const response = await vendorApi.post('http://localhost:4010/api/package-assignments', {
        employeeId: assignmentForm.employeeId,
        clientId: user.tenantId,  // Pass the client's tenant ID
        vendorId: selectedPackage.vendorId,
        packageId: selectedPackage.packageId,
        packageName: selectedPackage.packageName,
        packageType: selectedPackage.packageType,
        validFrom: assignmentForm.validFrom,
        validUntil: assignmentForm.validUntil || null,
        notes: assignmentForm.notes,
      })

      toast.success(`Package "${selectedPackage.packageName}" assigned successfully!`)
      setShowAssignModal(false)
      resetForm()
      
      // Refresh data to show updated assignments
      fetchVendorsAndEmployees()
    } catch (error) {
      console.error('Assignment error:', error)
      const errorMessage = error.response?.data?.message || error.message || 'Failed to assign package'
      toast.error(errorMessage)
    }
  }

  const resetForm = () => {
    setAssignmentForm({
      employeeId: '',
      validFrom: new Date().toISOString().split('T')[0],
      validUntil: '',
      notes: '',
    })
    setSelectedPackage(null)
  }

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
      <div>
        <h1 className="text-3xl font-bold text-gray-900">Vendor Packages</h1>
        <p className="text-gray-600 mt-2">
          Browse available transportation packages and assign them to your employees
        </p>
      </div>

      {/* Vendors List */}
      {vendors.length === 0 ? (
        <div className="card text-center py-12">
          <p className="text-gray-500">No vendors available for your organization</p>
        </div>
      ) : (
        vendors.map((vendor) => (
          <div key={vendor.id} className="card">
            <div className="flex items-start justify-between mb-6">
              <div>
                <h2 className="text-2xl font-bold text-gray-900">{vendor.name}</h2>
                <p className="text-gray-600 mt-1">{vendor.serviceType}</p>
                <p className="text-sm text-gray-500 mt-1">
                  Contact: {vendor.contactPerson} • {vendor.contactPhone}
                </p>
              </div>
              <span className="px-3 py-1 bg-primary-100 text-primary-700 rounded-full text-sm font-medium">
                {vendor.packages?.length || 0} Packages Available
              </span>
            </div>

            {/* Packages Grid */}
            {vendor.packages && vendor.packages.length > 0 ? (
              <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                {vendor.packages.map((pkg) => (
                  <div
                    key={pkg.id}
                    className={`border-2 rounded-lg p-4 hover:shadow-md transition-shadow ${
                      pkg.packageType === 'PACKAGE' ? 'border-blue-200 bg-blue-50' :
                      pkg.packageType === 'TRIP' ? 'border-purple-200 bg-purple-50' :
                      'border-green-200 bg-green-50'
                    }`}
                  >
                    <div className="flex items-start justify-between mb-3">
                      <div className="flex-1">
                        <h4 className="font-semibold text-gray-900">{pkg.packageName}</h4>
                        <span className={`inline-block mt-1 px-2 py-1 text-xs font-medium rounded-full ${
                          pkg.packageType === 'PACKAGE' ? 'bg-blue-100 text-blue-800' :
                          pkg.packageType === 'TRIP' ? 'bg-purple-100 text-purple-800' :
                          'bg-green-100 text-green-800'
                        }`}>
                          {pkg.packageType}
                        </span>
                      </div>
                      <Package size={20} className="text-gray-400" />
                    </div>

                    {pkg.description && (
                      <p className="text-xs text-gray-600 mb-3 line-clamp-2">
                        {pkg.description}
                      </p>
                    )}

                    <div className="space-y-2 text-sm mb-4">
                      {pkg.packageType === 'PACKAGE' && (
                        <>
                          <div className="flex justify-between items-center py-1 border-b border-gray-200">
                            <span className="text-gray-600">Monthly Rate:</span>
                            <span className="font-bold text-blue-700">₹{pkg.monthlyRate?.toLocaleString()}</span>
                          </div>
                          <div className="flex justify-between items-center py-1">
                            <span className="text-gray-600">Includes:</span>
                            <span className="font-medium">{pkg.tripsIncluded} trips, {pkg.kmsIncluded} km</span>
                          </div>
                        </>
                      )}

                      {pkg.packageType === 'TRIP' && (
                        <>
                          <div className="flex justify-between items-center py-1 border-b border-gray-200">
                            <span className="text-gray-600">Per Trip:</span>
                            <span className="font-bold text-purple-700">₹{pkg.ratePerTrip}</span>
                          </div>
                          <div className="flex justify-between items-center py-1">
                            <span className="text-gray-600">Per Km:</span>
                            <span className="font-medium">₹{pkg.ratePerKm}</span>
                          </div>
                        </>
                      )}

                      {pkg.packageType === 'HYBRID' && (
                        <>
                          <div className="flex justify-between items-center py-1 border-b border-gray-200">
                            <span className="text-gray-600">Base Rate:</span>
                            <span className="font-bold text-green-700">₹{pkg.monthlyRate?.toLocaleString()}</span>
                          </div>
                          <div className="flex justify-between items-center py-1">
                            <span className="text-gray-600">Includes:</span>
                            <span className="font-medium">{pkg.tripsIncluded} trips</span>
                          </div>
                          <div className="flex justify-between items-center py-1">
                            <span className="text-gray-600">Extra Trip:</span>
                            <span>₹{pkg.ratePerTrip}</span>
                          </div>
                        </>
                      )}
                    </div>

                    <button
                      onClick={() => handleAssignPackage(vendor, pkg)}
                      className="btn btn-primary w-full flex items-center justify-center gap-2"
                    >
                      <Users size={16} />
                      Assign to Employee
                    </button>
                  </div>
                ))}
              </div>
            ) : (
              <p className="text-gray-500 text-center py-8">No packages available for this vendor</p>
            )}
          </div>
        ))
      )}

      {/* Assignment Modal */}
      {showAssignModal && selectedPackage && (
        <div className="fixed inset-0 bg-black bg-opacity-50 z-50 flex items-center justify-center p-4">
          <div className="bg-white rounded-lg max-w-md w-full">
            <div className="p-6">
              <div className="flex items-center justify-between mb-4">
                <h2 className="text-2xl font-bold text-gray-900">Assign Package</h2>
                <button
                  onClick={() => {
                    setShowAssignModal(false)
                    resetForm()
                  }}
                  className="text-gray-400 hover:text-gray-600"
                >
                  <X size={24} />
                </button>
              </div>

              <div className="bg-primary-50 border border-primary-200 rounded-lg p-4 mb-6">
                <p className="text-sm text-gray-600">Package:</p>
                <p className="font-semibold text-gray-900">{selectedPackage.packageName}</p>
                <p className="text-xs text-gray-600 mt-1">
                  {selectedPackage.vendorName} • {selectedPackage.packageType}
                </p>
              </div>

              <form onSubmit={handleSubmitAssignment} className="space-y-4">
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Select Employee *
                  </label>
                  <select
                    value={assignmentForm.employeeId}
                    onChange={(e) => setAssignmentForm({ ...assignmentForm, employeeId: e.target.value })}
                    className="input"
                    required
                  >
                    <option value="">Choose an employee</option>
                    {employees.map((emp) => (
                      <option key={emp.id} value={emp.id}>
                        {emp.firstName} {emp.lastName} ({emp.employeeId})
                      </option>
                    ))}
                  </select>
                </div>

                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2">
                      Valid From
                    </label>
                    <input
                      type="date"
                      value={assignmentForm.validFrom}
                      onChange={(e) => setAssignmentForm({ ...assignmentForm, validFrom: e.target.value })}
                      className="input"
                    />
                  </div>
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2">
                      Valid Until (Optional)
                    </label>
                    <input
                      type="date"
                      value={assignmentForm.validUntil}
                      onChange={(e) => setAssignmentForm({ ...assignmentForm, validUntil: e.target.value })}
                      className="input"
                    />
                  </div>
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Notes (Optional)
                  </label>
                  <textarea
                    value={assignmentForm.notes}
                    onChange={(e) => setAssignmentForm({ ...assignmentForm, notes: e.target.value })}
                    className="input"
                    rows="3"
                    placeholder="Add any notes about this assignment..."
                  ></textarea>
                </div>

                <div className="flex justify-end space-x-3 pt-4">
                  <button
                    type="button"
                    onClick={() => {
                      setShowAssignModal(false)
                      resetForm()
                    }}
                    className="btn btn-secondary"
                  >
                    Cancel
                  </button>
                  <button type="submit" className="btn btn-primary flex items-center gap-2">
                    <Check size={18} />
                    Assign Package
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

export default ClientVendors

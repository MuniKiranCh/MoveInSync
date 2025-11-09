import { useState, useEffect, useCallback } from 'react'
import { useAuth } from '../contexts/AuthContext'
import { vendorApi, tripApi } from '../utils/api'
import { Truck, Users, Check, DollarSign, Package, Info } from 'lucide-react'
import toast from 'react-hot-toast'

const ClientVendors = () => {
  const { user } = useAuth()
  const [vendors, setVendors] = useState([])
  const [loading, setLoading] = useState(true)
  const [showAssignModal, setShowAssignModal] = useState(false)
  const [selectedPackage, setSelectedPackage] = useState(null)
  const [employees, setEmployees] = useState([])

  const fetchVendorPackages = useCallback(async () => {
    if (!user?.tenantId) return
    
    setLoading(true)
    try {
      // Fetch ALL active vendors from vendor-service
      const vendorsResponse = await vendorApi.get('/vendors/active')
      const allVendors = vendorsResponse.data
      
      // Fetch billing models for this client
      const billingModelsResponse = await tripApi.get(`/billing-models/client/${user.tenantId}/active`)
        .catch(err => {
          console.log('No billing models yet:', err)
          return { data: [] }
        })
      const models = billingModelsResponse.data || []

      // Group billing models by vendor
      const vendorPackagesMap = new Map()
      
      // Initialize all vendors
      allVendors.forEach(vendor => {
        vendorPackagesMap.set(vendor.id, {
          vendorId: vendor.id,
          vendorName: vendor.name,
          vendorCode: vendor.code,
          serviceType: vendor.serviceType,
          contactEmail: vendor.contactEmail,
          contactPhone: vendor.contactPhone,
          packages: []
        })
      })
      
      // Add packages to vendors that have them
      models.forEach(model => {
        if (vendorPackagesMap.has(model.vendorId)) {
          const packageDetails = {
            id: model.id,
            name: getPackageName(vendorPackagesMap.get(model.vendorId).vendorName, model.modelType),
            modelType: model.modelType,
            monthlyRate: model.packageMonthlyRate,
            ratePerTrip: model.ratePerTrip,
            ratePerKm: model.ratePerKm,
            tripsIncluded: model.packageTripsIncluded,
            kmsIncluded: model.packageKmsIncluded,
            extraTripRate: model.extraTripRate,
            extraKmRate: model.extraKmRate,
            extraHourRate: model.extraHourRate,
            standardTripKm: model.standardTripKm,
            standardTripHours: model.standardTripHours,
            description: generatePackageDescription(model)
          }
          
          vendorPackagesMap.get(model.vendorId).packages.push(packageDetails)
        }
      })
      
      setVendors(Array.from(vendorPackagesMap.values()))
    } catch (error) {
      console.error('Error fetching vendor packages:', error)
      toast.error('Failed to fetch vendor packages')
    } finally {
      setLoading(false)
    }
  }, [user?.tenantId])

  const fetchEmployees = useCallback(async () => {
    if (!user?.tenantId) return
    
    try {
      // TODO: Fetch actual employees from API
      // const response = await employeeApi.get(`/tenant/${user.tenantId}`)
      // setEmployees(response.data)
      
      // Start with empty list - employees will be created by admin
      setEmployees([])
    } catch (error) {
      console.error('Error fetching employees:', error)
    }
  }, [user?.tenantId])

  useEffect(() => {
    fetchVendorPackages()
    fetchEmployees()
  }, [fetchVendorPackages, fetchEmployees])

  const getPackageName = (vendorName, modelType) => {
    const typeNames = {
      TRIP: 'Trip-Based Billing',
      PACKAGE: 'Monthly Package',
      HYBRID: 'Hybrid Package'
    }
    return `${vendorName} - ${typeNames[modelType] || modelType}`
  }

  const generatePackageDescription = (model) => {
    if (model.modelType === 'TRIP') {
      return `₹${model.ratePerTrip} base per trip + ₹${model.ratePerKm}/km. Standard: ${model.standardTripKm}km, ${model.standardTripHours}hr included. Extra charges apply beyond limits.`
    } else if (model.modelType === 'PACKAGE') {
      return `₹${model.packageMonthlyRate?.toLocaleString()}/month for ${model.packageTripsIncluded} trips and ${model.packageKmsIncluded}km. Extra charges for usage beyond package.`
    } else if (model.modelType === 'HYBRID') {
      return `₹${model.packageMonthlyRate?.toLocaleString()} base + ${model.packageTripsIncluded} trips/${model.packageKmsIncluded}km included. Extra trips charged separately.`
    }
    return 'Custom billing model'
  }

  const handleAssignPackage = (pkg, vendor) => {
    setSelectedPackage({ ...pkg, vendorName: vendor.vendorName, vendorId: vendor.vendorId })
    setShowAssignModal(true)
  }

  const handleAssignToEmployee = async (employeeId) => {
    try {
      // TODO: API call to create employee subscription
      toast.success('Package assigned successfully!')
      setShowAssignModal(false)
      // Refresh data
      fetchEmployees()
    } catch (error) {
      toast.error('Failed to assign package')
    }
  }

  const getModelTypeBadge = (type) => {
    const styles = {
      TRIP: 'bg-blue-100 text-blue-800',
      PACKAGE: 'bg-green-100 text-green-800',
      HYBRID: 'bg-purple-100 text-purple-800'
    }
    return styles[type] || 'bg-gray-100 text-gray-800'
  }

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">Vendor Packages</h1>
          <p className="text-gray-600 mt-1">Browse and assign vendor packages to your employees</p>
        </div>
      </div>

      {/* Info Banner */}
      <div className="bg-blue-50 border border-blue-200 rounded-lg p-4 flex items-start gap-3">
        <Info className="text-blue-600 flex-shrink-0 mt-0.5" size={20} />
        <div className="text-sm text-blue-800">
          <p className="font-medium mb-1">How it works:</p>
          <ul className="list-disc list-inside space-y-1 text-blue-700">
            <li>Vendors set their own package prices</li>
            <li>You can assign packages to your employees</li>
            <li>Employees use assigned packages for commute</li>
            <li>Track usage and billing in real-time</li>
          </ul>
        </div>
      </div>

      {/* Vendor Packages */}
      {loading ? (
        <div className="text-center py-12">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary-600 mx-auto"></div>
          <p className="text-gray-600 mt-4">Loading vendors...</p>
        </div>
      ) : vendors.length === 0 ? (
        <div className="text-center py-12 bg-white rounded-lg shadow">
          <Truck className="mx-auto text-gray-400 mb-4" size={48} />
          <h3 className="text-lg font-medium text-gray-900 mb-2">No Vendors Available</h3>
          <p className="text-gray-600">Contact the platform administrator to add vendors to the system.</p>
        </div>
      ) : (
        <div className="space-y-6">
          {vendors.map((vendor) => (
            <div key={vendor.vendorId} className="bg-white rounded-lg shadow-md overflow-hidden">
              {/* Vendor Header */}
              <div className="bg-gradient-to-r from-primary-600 to-primary-700 px-6 py-4">
                <div className="flex items-center gap-3 text-white">
                  <Truck size={24} />
                  <div>
                    <h2 className="text-xl font-bold">{vendor.vendorName}</h2>
                    <p className="text-primary-100 text-sm">{vendor.packages.length} package(s) available</p>
                  </div>
                </div>
              </div>

              {/* Packages */}
              <div className="p-6 space-y-4">
                {vendor.packages.length === 0 ? (
                  <div className="text-center py-6 bg-gray-50 rounded-lg">
                    <Package className="mx-auto text-gray-400 mb-2" size={32} />
                    <p className="text-gray-600 text-sm">No packages available yet</p>
                    <p className="text-gray-500 text-xs mt-1">Contact vendor to set up billing packages</p>
                    <div className="mt-3 text-xs text-gray-600">
                      <p>📧 {vendor.contactEmail}</p>
                      <p>📞 {vendor.contactPhone}</p>
                    </div>
                  </div>
                ) : (
                  vendor.packages.map((pkg) => (
                  <div key={pkg.id} className="border border-gray-200 rounded-lg p-5 hover:shadow-md transition-shadow">
                    <div className="flex justify-between items-start mb-4">
                      <div className="flex-1">
                        <div className="flex items-center gap-3 mb-2">
                          <Package className="text-primary-600" size={20} />
                          <h3 className="text-lg font-semibold text-gray-900">{pkg.name}</h3>
                          <span className={`px-3 py-1 rounded-full text-xs font-medium ${getModelTypeBadge(pkg.modelType)}`}>
                            {pkg.modelType}
                          </span>
                        </div>
                        <p className="text-gray-600 text-sm mb-3">{pkg.description}</p>
                      </div>
                      <button
                        onClick={() => handleAssignPackage(pkg, vendor)}
                        className="btn btn-primary flex items-center gap-2 whitespace-nowrap ml-4"
                      >
                        <Users size={16} />
                        Assign to Employee
                      </button>
                    </div>

                    {/* Package Details */}
                    <div className="grid grid-cols-2 md:grid-cols-4 gap-4 pt-4 border-t border-gray-100">
                      {pkg.monthlyRate && (
                        <div>
                          <p className="text-xs text-gray-500 mb-1">Monthly Rate</p>
                          <p className="text-lg font-bold text-gray-900">₹{pkg.monthlyRate.toLocaleString()}</p>
                        </div>
                      )}
                      {pkg.ratePerTrip && (
                        <div>
                          <p className="text-xs text-gray-500 mb-1">Per Trip</p>
                          <p className="text-lg font-bold text-gray-900">₹{pkg.ratePerTrip}</p>
                        </div>
                      )}
                      {pkg.tripsIncluded && (
                        <div>
                          <p className="text-xs text-gray-500 mb-1">Trips Included</p>
                          <p className="text-lg font-bold text-gray-900">{pkg.tripsIncluded} trips</p>
                        </div>
                      )}
                      {pkg.kmsIncluded && (
                        <div>
                          <p className="text-xs text-gray-500 mb-1">KMs Included</p>
                          <p className="text-lg font-bold text-gray-900">{pkg.kmsIncluded} km</p>
                        </div>
                      )}
                    </div>
                  </div>
                  ))
                )}
              </div>
            </div>
          ))}
        </div>
      )}

      {/* Assign Package Modal */}
      {showAssignModal && selectedPackage && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
          <div className="bg-white rounded-xl max-w-2xl w-full max-h-[90vh] overflow-y-auto">
            <div className="sticky top-0 bg-white border-b border-gray-200 px-6 py-4">
              <h2 className="text-xl font-bold text-gray-900">Assign Package to Employee</h2>
              <p className="text-sm text-gray-600 mt-1">
                {selectedPackage.vendorName} - {selectedPackage.name}
              </p>
            </div>

            <div className="p-6 space-y-4">
              {employees.length === 0 ? (
                <p className="text-center text-gray-500 py-8">No employees available</p>
              ) : (
                employees.map((employee) => (
                  <div key={employee.id} className="border border-gray-200 rounded-lg p-4 flex justify-between items-center hover:bg-gray-50">
                    <div>
                      <p className="font-medium text-gray-900">{employee.name}</p>
                      <p className="text-sm text-gray-600">{employee.email}</p>
                      <p className="text-xs text-gray-500 mt-1">{employee.department}</p>
                    </div>
                    <button
                      onClick={() => handleAssignToEmployee(employee.id)}
                      disabled={employee.hasSubscription}
                      className={`btn ${employee.hasSubscription ? 'btn-secondary' : 'btn-primary'} flex items-center gap-2`}
                    >
                      {employee.hasSubscription ? (
                        <>
                          <Check size={16} />
                          Assigned
                        </>
                      ) : (
                        <>Assign</>
                      )}
                    </button>
                  </div>
                ))
              )}
            </div>

            <div className="sticky bottom-0 bg-gray-50 px-6 py-4 border-t border-gray-200 flex justify-end gap-3">
              <button
                onClick={() => setShowAssignModal(false)}
                className="btn btn-secondary"
              >
                Close
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  )
}

export default ClientVendors

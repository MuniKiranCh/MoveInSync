import { useState, useEffect } from 'react'
import { Plus, Edit2, Trash2, Search, Truck, Building2 } from 'lucide-react'
import { vendorApi, clientApi } from '../utils/api'
import toast from 'react-hot-toast'

const Vendors = () => {
  const [vendors, setVendors] = useState([])
  const [clients, setClients] = useState([])
  const [loading, setLoading] = useState(false)
  const [searchTerm, setSearchTerm] = useState('')
  const [selectedClient, setSelectedClient] = useState('all')
  const [showModal, setShowModal] = useState(false)
  const [editingVendor, setEditingVendor] = useState(null)
  const [formData, setFormData] = useState({
    name: '',
    code: '',
    clientId: '',
    serviceType: '',
    contactPerson: '',
    contactEmail: '',
    contactPhone: '',
    address: '',
    bankAccountDetails: '',
    taxId: '',
    gstNumber: '',
  })

  useEffect(() => {
    fetchClients()
    fetchVendors()
  }, [])

  useEffect(() => {
    if (selectedClient !== 'all') {
      fetchVendorsByClient(selectedClient)
    } else {
      fetchVendors()
    }
  }, [selectedClient])

  const fetchClients = async () => {
    try {
      const response = await clientApi.get('/clients')
      setClients(response.data)
    } catch (error) {
      console.error('Error fetching clients:', error)
    }
  }

  const fetchVendors = async () => {
    setLoading(true)
    try {
      const response = await vendorApi.get('/vendors')
      setVendors(response.data)
    } catch (error) {
      toast.error('Failed to load vendors')
      console.error('Error fetching vendors:', error)
    } finally {
      setLoading(false)
    }
  }

  const fetchVendorsByClient = async (clientId) => {
    setLoading(true)
    try {
      const response = await vendorApi.get(`/vendors/client/${clientId}`)
      setVendors(response.data)
    } catch (error) {
      toast.error('Failed to load vendors for selected client')
      console.error('Error fetching vendors:', error)
    } finally {
      setLoading(false)
    }
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    try {
      if (editingVendor) {
        await vendorApi.put(`/vendors/${editingVendor.id}`, formData)
        toast.success('Vendor updated successfully')
      } else {
        await vendorApi.post('/vendors', formData)
        toast.success('Vendor created successfully')
      }
      setShowModal(false)
      resetForm()
      fetchVendors()
    } catch (error) {
      toast.error(error.response?.data?.message || 'Operation failed')
    }
  }

  const handleDelete = async (id) => {
    if (!window.confirm('Are you sure you want to delete this vendor?')) return
    
    try {
      await vendorApi.delete(`/vendors/${id}`)
      toast.success('Vendor deleted successfully')
      fetchVendors()
    } catch (error) {
      toast.error('Failed to delete vendor')
    }
  }

  const resetForm = () => {
    setFormData({
      name: '',
      code: '',
      clientId: '',
      serviceType: '',
      contactPerson: '',
      contactEmail: '',
      contactPhone: '',
      address: '',
      bankAccountDetails: '',
      taxId: '',
      gstNumber: '',
    })
    setEditingVendor(null)
  }

  const getClientName = (clientId) => {
    const client = clients.find(c => c.id === clientId)
    return client ? client.name : 'Unknown Client'
  }

  const filteredVendors = vendors.filter(vendor =>
    vendor.name?.toLowerCase().includes(searchTerm.toLowerCase()) ||
    vendor.contactPerson?.toLowerCase().includes(searchTerm.toLowerCase()) ||
    vendor.code?.toLowerCase().includes(searchTerm.toLowerCase()) ||
    vendor.serviceType?.toLowerCase().includes(searchTerm.toLowerCase())
  )

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-3xl font-bold text-gray-900">Vendors</h1>
          <p className="text-gray-600 mt-2">Manage transportation vendors</p>
        </div>
        <button
          onClick={() => {
            resetForm()
            setShowModal(true)
          }}
          className="btn btn-primary flex items-center"
        >
          <Plus size={20} className="mr-2" />
          Add Vendor
        </button>
      </div>

      <div className="flex gap-4">
        <div className="flex-1 relative">
          <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400" size={20} />
          <input
            type="text"
            placeholder="Search vendors..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="input pl-10 w-full"
          />
        </div>
        <select
          value={selectedClient}
          onChange={(e) => setSelectedClient(e.target.value)}
          className="input w-64"
        >
          <option value="all">All Clients</option>
          {clients.map(client => (
            <option key={client.id} value={client.id}>
              {client.name}
            </option>
          ))}
        </select>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {loading ? (
          <div className="col-span-full flex justify-center py-12">
            <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary-600"></div>
          </div>
        ) : filteredVendors.length === 0 ? (
          <div className="col-span-full text-center py-12 text-gray-500">
            No vendors found
          </div>
        ) : (
          filteredVendors.map((vendor) => (
            <div key={vendor.id} className="card hover:shadow-lg transition-shadow duration-200">
              <div className="flex items-start justify-between mb-4">
                <div className="flex items-center">
                  <div className="p-3 bg-purple-100 rounded-lg">
                    <Truck className="text-purple-600" size={24} />
                  </div>
                  <div className="ml-3">
                    <h3 className="text-lg font-semibold text-gray-900">{vendor.name}</h3>
                    <p className="text-sm text-gray-500">{vendor.address}</p>
                  </div>
                </div>
                <div className="flex space-x-2">
                  <button
                    onClick={() => {
                      setEditingVendor(vendor)
                      setFormData({
                        name: vendor.name,
                        code: vendor.code,
                        clientId: vendor.clientId || '',
                        serviceType: vendor.serviceType || '',
                        contactPerson: vendor.contactPerson,
                        contactEmail: vendor.contactEmail,
                        contactPhone: vendor.contactPhone,
                        address: vendor.address,
                        bankAccountDetails: vendor.bankAccountDetails || '',
                        taxId: vendor.taxId || '',
                        gstNumber: vendor.gstNumber || '',
                      })
                      setShowModal(true)
                    }}
                    className="text-primary-600 hover:text-primary-900"
                  >
                    <Edit2 size={16} />
                  </button>
                  <button
                    onClick={() => handleDelete(vendor.id)}
                    className="text-red-600 hover:text-red-900"
                  >
                    <Trash2 size={16} />
                  </button>
                </div>
              </div>
              
              <div className="space-y-2 text-sm">
                <p className="text-gray-700">
                  <span className="font-medium">Code:</span> {vendor.code}
                </p>
                {vendor.clientId && (
                  <p className="text-gray-700 flex items-center">
                    <Building2 size={14} className="mr-1" />
                    <span className="font-medium">Client:</span> 
                    <span className="ml-1">{getClientName(vendor.clientId)}</span>
                  </p>
                )}
                {vendor.serviceType && (
                  <p className="text-gray-700">
                    <span className="font-medium">Service:</span> {vendor.serviceType}
                  </p>
                )}
                <p className="text-gray-700">
                  <span className="font-medium">Contact:</span> {vendor.contactPerson}
                </p>
                <p className="text-gray-700">
                  <span className="font-medium">Email:</span> {vendor.contactEmail}
                </p>
                <p className="text-gray-700">
                  <span className="font-medium">Phone:</span> {vendor.contactPhone}
                </p>
                {vendor.gstNumber && (
                  <p className="text-gray-700">
                    <span className="font-medium">GST:</span> {vendor.gstNumber}
                  </p>
                )}
              </div>

              {vendor.active !== undefined && (
                <div className="mt-4 pt-4 border-t border-gray-200">
                  <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${
                    vendor.active ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'
                  }`}>
                    {vendor.active ? 'Active' : 'Inactive'}
                  </span>
                </div>
              )}
            </div>
          ))
        )}
      </div>

      {/* Modal */}
      {showModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 z-50 flex items-center justify-center p-4">
          <div className="bg-white rounded-lg max-w-md w-full">
            <div className="p-6">
              <h2 className="text-2xl font-bold text-gray-900 mb-6">
                {editingVendor ? 'Edit Vendor' : 'Add Vendor'}
              </h2>
              
              <form onSubmit={handleSubmit} className="space-y-4 max-h-[70vh] overflow-y-auto">
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Vendor Name
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
                    Vendor Code
                  </label>
                  <input
                    type="text"
                    value={formData.code}
                    onChange={(e) => setFormData({ ...formData, code: e.target.value })}
                    className="input"
                    placeholder="e.g., OLA001"
                    required
                    disabled={editingVendor !== null}
                  />
                  {editingVendor && (
                    <p className="text-xs text-gray-500 mt-1">Code cannot be changed</p>
                  )}
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Client
                  </label>
                  <select
                    value={formData.clientId}
                    onChange={(e) => setFormData({ ...formData, clientId: e.target.value })}
                    className="input"
                  >
                    <option value="">Select Client (Optional)</option>
                    {clients.map(client => (
                      <option key={client.id} value={client.id}>
                        {client.name}
                      </option>
                    ))}
                  </select>
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Service Type
                  </label>
                  <input
                    type="text"
                    value={formData.serviceType}
                    onChange={(e) => setFormData({ ...formData, serviceType: e.target.value })}
                    className="input"
                    placeholder="e.g., Cab Service, Bike Taxi"
                  />
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Contact Person
                  </label>
                  <input
                    type="text"
                    value={formData.contactPerson}
                    onChange={(e) => setFormData({ ...formData, contactPerson: e.target.value })}
                    className="input"
                    required
                  />
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Contact Email
                  </label>
                  <input
                    type="email"
                    value={formData.contactEmail}
                    onChange={(e) => setFormData({ ...formData, contactEmail: e.target.value })}
                    className="input"
                    required
                  />
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Contact Phone
                  </label>
                  <input
                    type="tel"
                    value={formData.contactPhone}
                    onChange={(e) => setFormData({ ...formData, contactPhone: e.target.value })}
                    className="input"
                    required
                  />
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Address
                  </label>
                  <textarea
                    value={formData.address}
                    onChange={(e) => setFormData({ ...formData, address: e.target.value })}
                    className="input"
                    rows="2"
                    required
                  ></textarea>
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Bank Account Details
                  </label>
                  <textarea
                    value={formData.bankAccountDetails}
                    onChange={(e) => setFormData({ ...formData, bankAccountDetails: e.target.value })}
                    className="input"
                    rows="2"
                    placeholder="Bank name, Account number, IFSC"
                  ></textarea>
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Tax ID / PAN
                  </label>
                  <input
                    type="text"
                    value={formData.taxId}
                    onChange={(e) => setFormData({ ...formData, taxId: e.target.value })}
                    className="input"
                    placeholder="e.g., AABCO1234D"
                  />
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    GST Number
                  </label>
                  <input
                    type="text"
                    value={formData.gstNumber}
                    onChange={(e) => setFormData({ ...formData, gstNumber: e.target.value })}
                    className="input"
                    placeholder="e.g., 29AABCO1234D1Z5"
                  />
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
                    {editingVendor ? 'Update' : 'Create'}
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

export default Vendors



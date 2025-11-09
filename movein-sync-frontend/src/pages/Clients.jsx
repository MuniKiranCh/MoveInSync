import { useState, useEffect } from 'react'
import { Plus, Edit2, Trash2, Search, Building2 } from 'lucide-react'
import { clientApi } from '../utils/api'
import toast from 'react-hot-toast'

const Clients = () => {
  const [clients, setClients] = useState([])
  const [loading, setLoading] = useState(false)
  const [searchTerm, setSearchTerm] = useState('')
  const [showModal, setShowModal] = useState(false)
  const [editingClient, setEditingClient] = useState(null)
  const [formData, setFormData] = useState({
    name: '',
    code: '',
    industry: '',
    contactPerson: '',
    contactEmail: '',
    contactPhone: '',
    address: '',
  })

  useEffect(() => {
    fetchClients()
  }, [])

  const fetchClients = async () => {
    setLoading(true)
    try {
      const response = await clientApi.get('/clients')
      setClients(response.data)
    } catch (error) {
      toast.error('Failed to load clients')
      console.error('Error fetching clients:', error)
    } finally {
      setLoading(false)
    }
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    try {
      if (editingClient) {
        await clientApi.put(`/clients/${editingClient.id}`, formData)
        toast.success('Client updated successfully')
      } else {
        await clientApi.post('/clients', formData)
        toast.success('Client created successfully')
      }
      setShowModal(false)
      resetForm()
      fetchClients()
    } catch (error) {
      toast.error(error.response?.data?.message || 'Operation failed')
    }
  }

  const handleDelete = async (id) => {
    if (!window.confirm('Are you sure you want to delete this client?')) return
    
    try {
      await clientApi.delete(`/clients/${id}`)
      toast.success('Client deleted successfully')
      fetchClients()
    } catch (error) {
      toast.error('Failed to delete client')
    }
  }

  const resetForm = () => {
    setFormData({
      name: '',
      code: '',
      industry: '',
      contactPerson: '',
      contactEmail: '',
      contactPhone: '',
      address: '',
    })
    setEditingClient(null)
  }

  const filteredClients = clients.filter(client =>
    client.name?.toLowerCase().includes(searchTerm.toLowerCase()) ||
    client.contactPerson?.toLowerCase().includes(searchTerm.toLowerCase()) ||
    client.contactEmail?.toLowerCase().includes(searchTerm.toLowerCase()) ||
    client.code?.toLowerCase().includes(searchTerm.toLowerCase())
  )

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-3xl font-bold text-gray-900">Clients</h1>
          <p className="text-gray-600 mt-2">Manage corporate clients</p>
        </div>
        <button
          onClick={() => {
            resetForm()
            setShowModal(true)
          }}
          className="btn btn-primary flex items-center"
        >
          <Plus size={20} className="mr-2" />
          Add Client
        </button>
      </div>

      <div className="relative">
        <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400" size={20} />
        <input
          type="text"
          placeholder="Search clients..."
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          className="input pl-10"
        />
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {loading ? (
          <div className="col-span-full flex justify-center py-12">
            <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary-600"></div>
          </div>
        ) : filteredClients.length === 0 ? (
          <div className="col-span-full text-center py-12 text-gray-500">
            No clients found
          </div>
        ) : (
          filteredClients.map((client) => (
            <div key={client.id} className="card hover:shadow-lg transition-shadow duration-200">
              <div className="flex items-start justify-between mb-4">
                <div className="flex items-center">
                  <div className="p-3 bg-blue-100 rounded-lg">
                    <Building2 className="text-blue-600" size={24} />
                  </div>
                  <div className="ml-3">
                    <h3 className="text-lg font-semibold text-gray-900">{client.name}</h3>
                    <p className="text-sm text-gray-500">{client.address}</p>
                  </div>
                </div>
                <div className="flex space-x-2">
                  <button
                    onClick={() => {
                      setEditingClient(client)
                      setFormData({
                        name: client.name,
                        code: client.code,
                        industry: client.industry || '',
                        contactPerson: client.contactPerson,
                        contactEmail: client.contactEmail,
                        contactPhone: client.contactPhone,
                        address: client.address,
                      })
                      setShowModal(true)
                    }}
                    className="text-primary-600 hover:text-primary-900"
                  >
                    <Edit2 size={16} />
                  </button>
                  <button
                    onClick={() => handleDelete(client.id)}
                    className="text-red-600 hover:text-red-900"
                  >
                    <Trash2 size={16} />
                  </button>
                </div>
              </div>
              
              <div className="space-y-2 text-sm">
                <p className="text-gray-700">
                  <span className="font-medium">Code:</span> {client.code}
                </p>
                {client.industry && (
                  <p className="text-gray-700">
                    <span className="font-medium">Industry:</span> {client.industry}
                  </p>
                )}
                <p className="text-gray-700">
                  <span className="font-medium">Contact:</span> {client.contactPerson}
                </p>
                <p className="text-gray-700">
                  <span className="font-medium">Email:</span> {client.contactEmail}
                </p>
                <p className="text-gray-700">
                  <span className="font-medium">Phone:</span> {client.contactPhone}
                </p>
              </div>

              <div className="mt-4 pt-4 border-t border-gray-200 grid grid-cols-2 gap-4">
                <div>
                  <p className="text-xs text-gray-500">Employees</p>
                  <p className="text-lg font-semibold text-gray-900">{client.totalEmployees}</p>
                </div>
                <div>
                  <p className="text-xs text-gray-500">Active Vendors</p>
                  <p className="text-lg font-semibold text-gray-900">{client.activeVendors}</p>
                </div>
              </div>
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
                {editingClient ? 'Edit Client' : 'Add Client'}
              </h2>
              
              <form onSubmit={handleSubmit} className="space-y-4">
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Company Name
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
                    Client Code
                  </label>
                  <input
                    type="text"
                    value={formData.code}
                    onChange={(e) => setFormData({ ...formData, code: e.target.value })}
                    className="input"
                    placeholder="e.g., AMZN001"
                    required
                    disabled={editingClient !== null}
                  />
                  {editingClient && (
                    <p className="text-xs text-gray-500 mt-1">Code cannot be changed</p>
                  )}
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Industry
                  </label>
                  <input
                    type="text"
                    value={formData.industry}
                    onChange={(e) => setFormData({ ...formData, industry: e.target.value })}
                    className="input"
                    placeholder="e.g., E-Commerce"
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
                    rows="3"
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
                    {editingClient ? 'Update' : 'Create'}
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

export default Clients


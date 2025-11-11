import { useState, useEffect } from 'react'
import { Plus, Edit2, Trash2, Search } from 'lucide-react'
import api from '../utils/api'
import toast from 'react-hot-toast'

const BillingModels = () => {
  const [models, setModels] = useState([])
  const [loading, setLoading] = useState(false)
  const [searchTerm, setSearchTerm] = useState('')
  const [showModal, setShowModal] = useState(false)
  const [editingModel, setEditingModel] = useState(null)
  const [formData, setFormData] = useState({
    clientName: '',
    vendorName: '',
    modelType: 'PACKAGE',
    baseCost: '',
    tripLimit: '',
    kmLimit: '',
    costPerTrip: '',
    costPerKm: '',
    overtimeRate: '',
    description: '',
  })

  useEffect(() => {
    fetchBillingModels()
  }, [])

  const fetchBillingModels = async () => {
    setLoading(true)
    try {
      // Simulate API call - replace with actual endpoint
      const mockData = [
        {
          id: 1,
          clientName: 'TechCorp India',
          vendorName: 'FastCabs',
          modelType: 'PACKAGE',
          baseCost: 50000,
          tripLimit: 200,
          kmLimit: 5000,
          overtimeRate: 15,
        },
        {
          id: 2,
          clientName: 'Global Solutions',
          vendorName: 'QuickRide',
          modelType: 'TRIP',
          costPerTrip: 250,
          costPerKm: 12,
          overtimeRate: 20,
        },
        {
          id: 3,
          clientName: 'Startup Hub',
          vendorName: 'CityTransport',
          modelType: 'HYBRID',
          baseCost: 30000,
          tripLimit: 100,
          costPerTrip: 200,
          overtimeRate: 18,
        },
      ]
      setModels(mockData)
    } catch (error) {
      toast.error('Failed to load billing models')
    } finally {
      setLoading(false)
    }
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    try {
      if (editingModel) {
        // Update existing model
        await api.put(`/billing-models/${editingModel.id}`, formData)
        toast.success('Billing model updated successfully')
      } else {
        // Create new model
        await api.post('/billing-models', formData)
        toast.success('Billing model created successfully')
      }
      setShowModal(false)
      resetForm()
      fetchBillingModels()
    } catch (error) {
      toast.error(error.response?.data?.message || 'Operation failed')
    }
  }

  const handleEdit = (model) => {
    setEditingModel(model)
    setFormData({
      clientName: model.clientName,
      vendorName: model.vendorName,
      modelType: model.modelType,
      baseCost: model.baseCost || '',
      tripLimit: model.tripLimit || '',
      kmLimit: model.kmLimit || '',
      costPerTrip: model.costPerTrip || '',
      costPerKm: model.costPerKm || '',
      overtimeRate: model.overtimeRate || '',
      description: model.description || '',
    })
    setShowModal(true)
  }

  const handleDelete = async (id) => {
    if (!confirm('Are you sure you want to delete this billing model?')) return
    
    try {
      await api.delete(`/billing-models/${id}`)
      toast.success('Billing model deleted successfully')
      fetchBillingModels()
    } catch (error) {
      toast.error('Failed to delete billing model')
    }
  }

  const resetForm = () => {
    setFormData({
      clientName: '',
      vendorName: '',
      modelType: 'PACKAGE',
      baseCost: '',
      tripLimit: '',
      kmLimit: '',
      costPerTrip: '',
      costPerKm: '',
      overtimeRate: '',
      description: '',
    })
    setEditingModel(null)
  }

  const filteredModels = models.filter(model =>
    model.clientName.toLowerCase().includes(searchTerm.toLowerCase()) ||
    model.vendorName.toLowerCase().includes(searchTerm.toLowerCase()) ||
    model.modelType.toLowerCase().includes(searchTerm.toLowerCase())
  )

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-3xl font-bold text-gray-900">Billing Models</h1>
          <p className="text-gray-600 mt-2">Configure billing models for clients and vendors</p>
        </div>
        <button
          onClick={() => {
            resetForm()
            setShowModal(true)
          }}
          className="btn btn-primary flex items-center"
        >
          <Plus size={20} className="mr-2" />
          Add Model
        </button>
      </div>

      {/* Search */}
      <div className="relative">
        <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400" size={20} />
        <input
          type="text"
          placeholder="Search by client, vendor, or model type..."
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          className="input pl-10"
        />
      </div>

      {/* Models Table */}
      <div className="card overflow-hidden">
        <div className="overflow-x-auto">
          <table className="min-w-full divide-y divide-gray-200">
            <thead className="bg-gray-50">
              <tr>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Client
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Vendor
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Model Type
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Details
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Overtime Rate
                </th>
                <th className="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Actions
                </th>
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-200">
              {loading ? (
                <tr>
                  <td colSpan="6" className="px-6 py-12 text-center">
                    <div className="flex justify-center">
                      <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary-600"></div>
                    </div>
                  </td>
                </tr>
              ) : filteredModels.length === 0 ? (
                <tr>
                  <td colSpan="6" className="px-6 py-12 text-center text-gray-500">
                    No billing models found
                  </td>
                </tr>
              ) : (
                filteredModels.map((model) => (
                  <tr key={model.id} className="hover:bg-gray-50">
                    <td className="px-6 py-4 whitespace-nowrap">
                      <div className="text-sm font-medium text-gray-900">{model.clientName}</div>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <div className="text-sm text-gray-900">{model.vendorName}</div>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <span className={`px-2 py-1 inline-flex text-xs leading-5 font-semibold rounded-full ${
                        model.modelType === 'PACKAGE' ? 'bg-blue-100 text-blue-800' :
                        model.modelType === 'TRIP' ? 'bg-purple-100 text-purple-800' :
                        'bg-green-100 text-green-800'
                      }`}>
                        {model.modelType}
                      </span>
                    </td>
                    <td className="px-6 py-4">
                      <div className="text-sm text-gray-900">
                        {model.modelType === 'PACKAGE' && (
                          <>
                            Base: ₹{model.baseCost?.toLocaleString()}<br />
                            Trips: {model.tripLimit} | KM: {model.kmLimit}
                          </>
                        )}
                        {model.modelType === 'TRIP' && (
                          <>
                            Per Trip: ₹{model.costPerTrip}<br />
                            Per KM: ₹{model.costPerKm}
                          </>
                        )}
                        {model.modelType === 'HYBRID' && (
                          <>
                            Base: ₹{model.baseCost?.toLocaleString()}<br />
                            Per Trip: ₹{model.costPerTrip}
                          </>
                        )}
                      </div>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <div className="text-sm text-gray-900">₹{model.overtimeRate}/hr</div>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                      <button
                        onClick={() => handleEdit(model)}
                        className="text-primary-600 hover:text-primary-900 mr-4"
                      >
                        <Edit2 size={18} />
                      </button>
                      <button
                        onClick={() => handleDelete(model.id)}
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
          <div className="bg-white rounded-lg max-w-2xl w-full max-h-[90vh] overflow-y-auto">
            <div className="p-6">
              <h2 className="text-2xl font-bold text-gray-900 mb-6">
                {editingModel ? 'Edit Billing Model' : 'Add Billing Model'}
              </h2>
              
              <form onSubmit={handleSubmit} className="space-y-4">
                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2">
                      Client Name
                    </label>
                    <input
                      type="text"
                      value={formData.clientName}
                      onChange={(e) => setFormData({ ...formData, clientName: e.target.value })}
                      className="input"
                      required
                    />
                  </div>

                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2">
                      Vendor Name
                    </label>
                    <input
                      type="text"
                      value={formData.vendorName}
                      onChange={(e) => setFormData({ ...formData, vendorName: e.target.value })}
                      className="input"
                      required
                    />
                  </div>
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Model Type
                  </label>
                  <select
                    value={formData.modelType}
                    onChange={(e) => setFormData({ ...formData, modelType: e.target.value })}
                    className="input"
                  >
                    <option value="PACKAGE">Package Model</option>
                    <option value="TRIP">Trip Model</option>
                    <option value="HYBRID">Hybrid Model</option>
                  </select>
                </div>

                {(formData.modelType === 'PACKAGE' || formData.modelType === 'HYBRID') && (
                  <div className="grid grid-cols-3 gap-4">
                    <div>
                      <label className="block text-sm font-medium text-gray-700 mb-2">
                        Base Cost (₹)
                      </label>
                      <input
                        type="number"
                        value={formData.baseCost}
                        onChange={(e) => setFormData({ ...formData, baseCost: e.target.value })}
                        className="input"
                        required
                      />
                    </div>
                    <div>
                      <label className="block text-sm font-medium text-gray-700 mb-2">
                        Trip Limit
                      </label>
                      <input
                        type="number"
                        value={formData.tripLimit}
                        onChange={(e) => setFormData({ ...formData, tripLimit: e.target.value })}
                        className="input"
                        required={formData.modelType === 'PACKAGE'}
                      />
                    </div>
                    <div>
                      <label className="block text-sm font-medium text-gray-700 mb-2">
                        KM Limit
                      </label>
                      <input
                        type="number"
                        value={formData.kmLimit}
                        onChange={(e) => setFormData({ ...formData, kmLimit: e.target.value })}
                        className="input"
                        required={formData.modelType === 'PACKAGE'}
                      />
                    </div>
                  </div>
                )}

                {(formData.modelType === 'TRIP' || formData.modelType === 'HYBRID') && (
                  <div className="grid grid-cols-2 gap-4">
                    <div>
                      <label className="block text-sm font-medium text-gray-700 mb-2">
                        Cost Per Trip (₹)
                      </label>
                      <input
                        type="number"
                        value={formData.costPerTrip}
                        onChange={(e) => setFormData({ ...formData, costPerTrip: e.target.value })}
                        className="input"
                        required
                      />
                    </div>
                    <div>
                      <label className="block text-sm font-medium text-gray-700 mb-2">
                        Cost Per KM (₹)
                      </label>
                      <input
                        type="number"
                        value={formData.costPerKm}
                        onChange={(e) => setFormData({ ...formData, costPerKm: e.target.value })}
                        className="input"
                      />
                    </div>
                  </div>
                )}

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Overtime Rate (₹/hr)
                  </label>
                  <input
                    type="number"
                    value={formData.overtimeRate}
                    onChange={(e) => setFormData({ ...formData, overtimeRate: e.target.value })}
                    className="input"
                    required
                  />
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Description (Optional)
                  </label>
                  <textarea
                    value={formData.description}
                    onChange={(e) => setFormData({ ...formData, description: e.target.value })}
                    className="input"
                    rows="3"
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
                    {editingModel ? 'Update' : 'Create'}
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

export default BillingModels


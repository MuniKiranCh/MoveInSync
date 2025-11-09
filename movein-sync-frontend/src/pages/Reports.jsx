import { useState } from 'react'
import { FileText, Download, Filter, Calendar } from 'lucide-react'
import { format } from 'date-fns'
import * as XLSX from 'xlsx'
import { saveAs } from 'file-saver'
import toast from 'react-hot-toast'
import api from '../utils/api'

const Reports = () => {
  const [reportType, setReportType] = useState('CLIENT')
  const [dateRange, setDateRange] = useState({
    startDate: format(new Date(new Date().setMonth(new Date().getMonth() - 1)), 'yyyy-MM-dd'),
    endDate: format(new Date(), 'yyyy-MM-dd'),
  })
  const [selectedClient, setSelectedClient] = useState('')
  const [selectedVendor, setSelectedVendor] = useState('')
  const [loading, setLoading] = useState(false)
  const [reportData, setReportData] = useState(null)

  const reportTypes = [
    { value: 'CLIENT', label: 'Client Report', description: 'Monthly summary for all trips and vendor payments' },
    { value: 'VENDOR', label: 'Vendor Report', description: 'Detailed statements of payable trips and incentives' },
    { value: 'EMPLOYEE', label: 'Employee Report', description: 'Summary of earned incentives for extra hours/trips' },
    { value: 'COMPREHENSIVE', label: 'Comprehensive Report', description: 'Complete billing analysis across all entities' },
  ]

  const generateReport = async () => {
    setLoading(true)
    try {
      // Simulate API call
      await new Promise(resolve => setTimeout(resolve, 1000))
      
      // Generate mock report data based on type
      const mockData = generateMockReportData(reportType)
      setReportData(mockData)
      toast.success('Report generated successfully')
    } catch (error) {
      toast.error('Failed to generate report')
    } finally {
      setLoading(false)
    }
  }

  const generateMockReportData = (type) => {
    switch (type) {
      case 'CLIENT':
        return {
          summary: {
            totalTrips: 1250,
            totalCost: 425000,
            totalVendors: 5,
            avgCostPerTrip: 340,
          },
          details: [
            { vendor: 'FastCabs', trips: 350, cost: 125000, model: 'Package' },
            { vendor: 'QuickRide', trips: 280, cost: 98000, model: 'Trip' },
            { vendor: 'CityTransport', trips: 250, cost: 87500, model: 'Hybrid' },
            { vendor: 'MetroWheels', trips: 220, cost: 77000, model: 'Package' },
            { vendor: 'UrbanCommute', trips: 150, cost: 37500, model: 'Trip' },
          ],
        }
      case 'VENDOR':
        return {
          summary: {
            totalTrips: 850,
            totalRevenue: 285000,
            basePayment: 250000,
            incentives: 35000,
          },
          details: [
            { date: '2024-01-15', client: 'TechCorp', trips: 45, distance: 850, payment: 15750, incentive: 1250 },
            { date: '2024-01-16', client: 'Global Solutions', trips: 38, distance: 720, payment: 13320, incentive: 920 },
            { date: '2024-01-17', client: 'TechCorp', trips: 52, distance: 980, payment: 18200, incentive: 1800 },
          ],
        }
      case 'EMPLOYEE':
        return {
          summary: {
            totalTrips: 42,
            regularTrips: 38,
            extraTrips: 4,
            totalIncentive: 2500,
          },
          details: [
            { date: '2024-01-15', origin: 'Home', destination: 'Office', distance: 12.5, extraHours: 0, incentive: 0 },
            { date: '2024-01-16', origin: 'Home', destination: 'Office', distance: 12.5, extraHours: 2, incentive: 500 },
            { date: '2024-01-17', origin: 'Home', destination: 'Office', distance: 12.5, extraHours: 1.5, incentive: 375 },
          ],
        }
      default:
        return null
    }
  }

  const exportToExcel = () => {
    if (!reportData) {
      toast.error('No report data to export')
      return
    }

    try {
      const wb = XLSX.utils.book_new()
      
      // Summary sheet
      const summaryData = Object.entries(reportData.summary).map(([key, value]) => ({
        Metric: key.replace(/([A-Z])/g, ' $1').trim(),
        Value: value,
      }))
      const summaryWs = XLSX.utils.json_to_sheet(summaryData)
      XLSX.utils.book_append_sheet(wb, summaryWs, 'Summary')
      
      // Details sheet
      const detailsWs = XLSX.utils.json_to_sheet(reportData.details)
      XLSX.utils.book_append_sheet(wb, detailsWs, 'Details')
      
      // Generate file
      const wbout = XLSX.write(wb, { bookType: 'xlsx', type: 'array' })
      const blob = new Blob([wbout], { type: 'application/octet-stream' })
      const fileName = `${reportType}_Report_${format(new Date(), 'yyyy-MM-dd')}.xlsx`
      saveAs(blob, fileName)
      
      toast.success('Report exported successfully')
    } catch (error) {
      toast.error('Failed to export report')
    }
  }

  const exportToPDF = () => {
    toast.info('PDF export functionality coming soon')
  }

  return (
    <div className="space-y-6">
      {/* Header */}
      <div>
        <h1 className="text-3xl font-bold text-gray-900">Reports</h1>
        <p className="text-gray-600 mt-2">Generate and export billing reports for stakeholders</p>
      </div>

      {/* Report Configuration */}
      <div className="card">
        <h2 className="text-xl font-semibold text-gray-900 mb-4 flex items-center">
          <Filter className="mr-2" />
          Report Configuration
        </h2>
        
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          {/* Report Type Selection */}
          <div className="md:col-span-2">
            <label className="block text-sm font-medium text-gray-700 mb-3">
              Report Type
            </label>
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
              {reportTypes.map((type) => (
                <button
                  key={type.value}
                  onClick={() => setReportType(type.value)}
                  className={`p-4 rounded-lg border-2 text-left transition-all ${
                    reportType === type.value
                      ? 'border-primary-600 bg-primary-50'
                      : 'border-gray-200 hover:border-primary-300'
                  }`}
                >
                  <div className="flex items-center mb-2">
                    <FileText size={20} className={reportType === type.value ? 'text-primary-600' : 'text-gray-600'} />
                    <span className="ml-2 font-medium text-gray-900">{type.label}</span>
                  </div>
                  <p className="text-xs text-gray-600">{type.description}</p>
                </button>
              ))}
            </div>
          </div>

          {/* Date Range */}
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              <Calendar size={16} className="inline mr-2" />
              Start Date
            </label>
            <input
              type="date"
              value={dateRange.startDate}
              onChange={(e) => setDateRange({ ...dateRange, startDate: e.target.value })}
              className="input"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              <Calendar size={16} className="inline mr-2" />
              End Date
            </label>
            <input
              type="date"
              value={dateRange.endDate}
              onChange={(e) => setDateRange({ ...dateRange, endDate: e.target.value })}
              className="input"
            />
          </div>

          {/* Conditional Filters */}
          {reportType === 'CLIENT' && (
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Select Client (Optional)
              </label>
              <select
                value={selectedClient}
                onChange={(e) => setSelectedClient(e.target.value)}
                className="input"
              >
                <option value="">All Clients</option>
                <option value="1">TechCorp India</option>
                <option value="2">Global Solutions</option>
                <option value="3">Startup Hub</option>
              </select>
            </div>
          )}

          {reportType === 'VENDOR' && (
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Select Vendor
              </label>
              <select
                value={selectedVendor}
                onChange={(e) => setSelectedVendor(e.target.value)}
                className="input"
                required
              >
                <option value="">Select a vendor...</option>
                <option value="1">FastCabs</option>
                <option value="2">QuickRide</option>
                <option value="3">CityTransport</option>
              </select>
            </div>
          )}
        </div>

        <div className="mt-6 flex justify-end">
          <button
            onClick={generateReport}
            disabled={loading}
            className="btn btn-primary flex items-center disabled:opacity-50"
          >
            {loading ? 'Generating...' : 'Generate Report'}
          </button>
        </div>
      </div>

      {/* Report Preview */}
      {reportData && (
        <div className="card">
          <div className="flex justify-between items-center mb-6">
            <h2 className="text-xl font-semibold text-gray-900">Report Preview</h2>
            <div className="flex space-x-3">
              <button onClick={exportToExcel} className="btn btn-secondary flex items-center">
                <Download size={18} className="mr-2" />
                Export Excel
              </button>
              <button onClick={exportToPDF} className="btn btn-secondary flex items-center">
                <Download size={18} className="mr-2" />
                Export PDF
              </button>
            </div>
          </div>

          {/* Summary Section */}
          <div className="mb-6">
            <h3 className="text-lg font-semibold text-gray-900 mb-4">Summary</h3>
            <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
              {Object.entries(reportData.summary).map(([key, value]) => (
                <div key={key} className="bg-gray-50 p-4 rounded-lg">
                  <p className="text-sm text-gray-600 mb-1">
                    {key.replace(/([A-Z])/g, ' $1').trim()}
                  </p>
                  <p className="text-2xl font-bold text-gray-900">
                    {typeof value === 'number' && key.toLowerCase().includes('cost') || 
                     key.toLowerCase().includes('payment') || 
                     key.toLowerCase().includes('revenue') || 
                     key.toLowerCase().includes('incentive')
                      ? `₹${value.toLocaleString()}`
                      : value.toLocaleString()}
                  </p>
                </div>
              ))}
            </div>
          </div>

          {/* Details Table */}
          <div>
            <h3 className="text-lg font-semibold text-gray-900 mb-4">Detailed Breakdown</h3>
            <div className="overflow-x-auto">
              <table className="min-w-full divide-y divide-gray-200">
                <thead className="bg-gray-50">
                  <tr>
                    {Object.keys(reportData.details[0]).map((key) => (
                      <th
                        key={key}
                        className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                      >
                        {key.replace(/([A-Z])/g, ' $1').trim()}
                      </th>
                    ))}
                  </tr>
                </thead>
                <tbody className="bg-white divide-y divide-gray-200">
                  {reportData.details.map((row, idx) => (
                    <tr key={idx} className="hover:bg-gray-50">
                      {Object.entries(row).map(([key, value], cellIdx) => (
                        <td key={cellIdx} className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                          {typeof value === 'number' && 
                           (key.toLowerCase().includes('cost') || 
                            key.toLowerCase().includes('payment') || 
                            key.toLowerCase().includes('revenue') || 
                            key.toLowerCase().includes('incentive'))
                            ? `₹${value.toLocaleString()}`
                            : value}
                        </td>
                      ))}
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        </div>
      )}

      {/* Quick Stats */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <div className="card">
          <h3 className="text-sm font-medium text-gray-600 mb-2">Reports Generated This Month</h3>
          <p className="text-3xl font-bold text-gray-900">142</p>
          <p className="text-sm text-green-600 mt-2">+18% from last month</p>
        </div>
        <div className="card">
          <h3 className="text-sm font-medium text-gray-600 mb-2">Total Data Points Processed</h3>
          <p className="text-3xl font-bold text-gray-900">48,520</p>
          <p className="text-sm text-blue-600 mt-2">Across all reports</p>
        </div>
        <div className="card">
          <h3 className="text-sm font-medium text-gray-600 mb-2">Average Processing Time</h3>
          <p className="text-3xl font-bold text-gray-900">2.3s</p>
          <p className="text-sm text-purple-600 mt-2">Fast and efficient</p>
        </div>
      </div>
    </div>
  )
}

export default Reports


# MoveInSync Billing & Reporting System - Frontend

A modern, responsive React frontend for the MoveInSync Billing & Reporting System. This application provides comprehensive billing management and reporting capabilities for employee commute solutions.

## 🚀 Features

### Multi-Tenant Architecture
- **Role-Based Access Control (RBAC)** with three distinct user roles:
  - **Admin**: Full system access with dashboard, client/vendor/employee management, billing models, and comprehensive reporting
  - **Vendor**: Access to trip management, revenue tracking, and payment information
  - **Employee**: View personal trips and earned incentives

### Core Functionality

#### Admin Features
- **Dashboard**: Real-time statistics, charts, and KPIs
  - Total clients, vendors, employees, and trips
  - Revenue tracking and analytics
  - Monthly trends and performance metrics
  - Top vendors analysis
  - Billing model distribution
- **Client Management**: Add, edit, and manage corporate clients
- **Vendor Management**: Manage transportation vendors with ratings
- **Employee Management**: Comprehensive employee records
- **Trip Management**: Track all trips with filtering and search
- **Billing Models Configuration**:
  - Package Model (fixed monthly cost)
  - Trip Model (per-trip billing)
  - Hybrid Model (combination)
  - Overtime rate configuration
- **Report Generation & Export**:
  - Client reports (monthly summaries)
  - Vendor reports (detailed statements)
  - Employee reports (incentive summaries)
  - Excel export functionality

#### Vendor Features
- Performance dashboard with weekly and monthly trends
- Trip history and status tracking
- Revenue and payment tracking
- Incentive monitoring

#### Employee Features
- Personal trip history
- Incentive tracking and calculations
- Monthly trends visualization
- Extra hours monitoring

## 🛠️ Technology Stack

- **React 18** - Modern React with hooks
- **Vite** - Fast build tool and dev server
- **React Router v6** - Client-side routing
- **Tailwind CSS** - Utility-first CSS framework
- **Recharts** - Data visualization and charts
- **Axios** - HTTP client for API calls
- **React Query** - Data fetching and caching
- **JWT Decode** - JWT token handling
- **React Hot Toast** - Toast notifications
- **XLSX** - Excel export functionality
- **Lucide React** - Beautiful icon library
- **date-fns** - Date manipulation

## 📋 Prerequisites

- Node.js (v16 or higher)
- npm or yarn
- Backend API running on `http://localhost:8080`

## 🔧 Installation

1. **Navigate to the frontend directory:**
   ```bash
   cd movein-sync-frontend
   ```

2. **Install dependencies:**
   ```bash
   npm install
   ```

3. **Configure environment variables:**
   Create a `.env` file in the root directory:
   ```bash
   VITE_API_BASE_URL=http://localhost:8080
   VITE_API_GATEWAY_URL=http://localhost:8080/api
   ```

## 🚀 Running the Application

### Development Mode
```bash
npm run dev
```
The application will start on `http://localhost:3000`

### Production Build
```bash
npm run build
```

### Preview Production Build
```bash
npm run preview
```

## 👥 Demo Credentials

### Admin User
- **Username**: `admin`
- **Password**: `admin123`

### Vendor User
- **Username**: `vendor`
- **Password**: `vendor123`

### Employee User
- **Username**: `employee`
- **Password**: `employee123`

## 📁 Project Structure

```
movein-sync-frontend/
├── src/
│   ├── components/          # Reusable components
│   │   └── Layout.jsx      # Main layout with sidebar and navigation
│   ├── contexts/           # React context providers
│   │   └── AuthContext.jsx # Authentication context
│   ├── pages/              # Page components
│   │   ├── Login.jsx
│   │   ├── AdminDashboard.jsx
│   │   ├── VendorDashboard.jsx
│   │   ├── EmployeeDashboard.jsx
│   │   ├── Clients.jsx
│   │   ├── Vendors.jsx
│   │   ├── Employees.jsx
│   │   ├── Trips.jsx
│   │   ├── BillingModels.jsx
│   │   └── Reports.jsx
│   ├── utils/              # Utility functions
│   │   └── api.js         # Axios instance and interceptors
│   ├── App.jsx            # Main app component
│   ├── main.jsx           # Entry point
│   └── index.css          # Global styles
├── public/                # Static assets
├── index.html            # HTML template
├── package.json          # Dependencies and scripts
├── vite.config.js       # Vite configuration
├── tailwind.config.js   # Tailwind CSS configuration
└── postcss.config.js    # PostCSS configuration
```

## 🔐 Authentication & Security

- JWT-based authentication
- Secure token storage in localStorage
- Automatic token expiration handling
- Request/response interceptors for authentication
- Protected routes with role-based access
- Tenant data isolation

## 🎨 UI/UX Features

- **Responsive Design**: Works seamlessly on desktop, tablet, and mobile
- **Modern UI**: Clean, professional interface with Tailwind CSS
- **Interactive Charts**: Real-time data visualization with Recharts
- **Toast Notifications**: User-friendly feedback for all actions
- **Loading States**: Smooth loading indicators
- **Form Validation**: Client-side validation for all forms
- **Modal Dialogs**: Intuitive add/edit interfaces
- **Search & Filters**: Easy data discovery
- **Sidebar Navigation**: Role-based navigation menu
- **Dark Theme Support**: (Can be extended)

## 📊 Key Components

### Dashboard Components
- **StatCard**: Reusable statistic display cards
- **Charts**: Line charts, bar charts, and pie charts
- **Alert System**: Real-time notifications and alerts

### Management Components
- **CRUD Operations**: Full create, read, update, delete functionality
- **Search & Filter**: Advanced filtering capabilities
- **Modal Forms**: Clean form interfaces
- **Data Tables**: Sortable, searchable tables

### Report Components
- **Report Configuration**: Flexible report generation
- **Preview System**: View reports before export
- **Export Functionality**: Excel and PDF export (Excel implemented)

## 🔄 API Integration

The frontend communicates with the backend API gateway at `http://localhost:8080/api`. All API calls include:
- JWT token in Authorization header
- Error handling and retry logic
- Automatic logout on 401 responses
- Request/response transformations

### Sample API Endpoints
```javascript
// Authentication
POST /api/auth/login
POST /api/auth/logout

// Clients
GET /api/clients
POST /api/clients
PUT /api/clients/:id
DELETE /api/clients/:id

// Vendors
GET /api/vendors
POST /api/vendors
PUT /api/vendors/:id
DELETE /api/vendors/:id

// Employees
GET /api/employees
POST /api/employees
PUT /api/employees/:id
DELETE /api/employees/:id

// Trips
GET /api/trips
POST /api/trips

// Billing Models
GET /api/billing-models
POST /api/billing-models
PUT /api/billing-models/:id
DELETE /api/billing-models/:id

// Reports
POST /api/reports/generate
```

## 🧪 Testing

```bash
# Run tests (if configured)
npm test

# Run linting
npm run lint
```

## 📦 Building for Production

1. **Build the application:**
   ```bash
   npm run build
   ```

2. **The build output will be in the `dist` directory**

3. **Deploy to your preferred hosting service:**
   - Netlify
   - Vercel
   - AWS S3 + CloudFront
   - Azure Static Web Apps
   - GitHub Pages

## 🚀 Deployment

### Environment Variables for Production
```bash
VITE_API_BASE_URL=https://your-api-domain.com
VITE_API_GATEWAY_URL=https://your-api-domain.com/api
```

## 🐛 Troubleshooting

### Common Issues

1. **Port already in use:**
   ```bash
   # Change port in vite.config.js or kill the process using port 3000
   ```

2. **API connection issues:**
   - Verify backend is running on port 8080
   - Check CORS configuration on backend
   - Verify API_GATEWAY_URL in .env file

3. **Build errors:**
   ```bash
   # Clear node_modules and reinstall
   rm -rf node_modules package-lock.json
   npm install
   ```

## 📈 Performance Optimization

- Code splitting with React.lazy (can be implemented)
- Image optimization
- Bundle size optimization with Vite
- React Query for efficient data caching
- Memoization for expensive computations

## 🔮 Future Enhancements

- Dark mode toggle
- PDF export for reports
- Real-time updates with WebSocket
- Advanced data analytics
- Bulk operations
- Advanced filtering and sorting
- Notification system
- Activity logs
- Multi-language support
- Mobile app version

## 📄 License

This project is proprietary software developed for MoveInSync.

## 👨‍💻 Development Team

Developed as part of the MoveInSync Billing & Reporting System.

## 📞 Support

For technical support or questions, please contact the development team.

---

**Note**: This frontend application requires the backend API to be running. Please refer to the main project README for complete setup instructions.


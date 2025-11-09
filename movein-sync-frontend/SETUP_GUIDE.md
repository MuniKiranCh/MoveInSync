# MoveInSync Billing & Reporting System - Complete Setup

## 📋 Complete Installation Guide

### Backend Setup (Java Spring Microservices)

The backend is already configured in the `java-spring-microservices-main` directory. Ensure all services are running:

1. **Auth Service** - Port 8081
2. **Patient Service** - Port 8082
3. **Billing Service** - Port 8083
4. **Analytics Service** - Port 8084
5. **API Gateway** - Port 8080

### Frontend Setup (React)

1. **Navigate to frontend directory:**
   ```bash
   cd movein-sync-frontend
   ```

2. **Install dependencies:**
   ```bash
   npm install
   ```

3. **Create `.env` file:**
   ```bash
   VITE_API_BASE_URL=http://localhost:8080
   VITE_API_GATEWAY_URL=http://localhost:8080/api
   ```

4. **Start the development server:**
   ```bash
   npm run dev
   ```

5. **Access the application:**
   Open your browser and navigate to `http://localhost:3000`

## 🎯 Quick Start

### Using Demo Credentials

The application comes with pre-configured demo credentials for testing:

#### Admin Access (Full System Access)
```
Username: admin
Password: admin123
```

**Admin Capabilities:**
- View comprehensive dashboard with statistics and charts
- Manage clients, vendors, and employees
- Configure billing models (Package, Trip, Hybrid)
- Track all trips across the system
- Generate and export reports for all stakeholders
- View revenue analytics and performance metrics

#### Vendor Access (Vendor Portal)
```
Username: vendor
Password: vendor123
```

**Vendor Capabilities:**
- View personal performance dashboard
- Track completed and pending trips
- Monitor revenue and payments
- View incentive calculations
- Access trip history and analytics

#### Employee Access (Employee Portal)
```
Username: employee
Password: employee123
```

**Employee Capabilities:**
- View personal trip history
- Track earned incentives
- Monitor extra hours worked
- View monthly commute patterns
- Access incentive summaries

## 🏗️ Architecture Overview

### Frontend Architecture
```
React (UI) → API Gateway → Microservices
   ↓
Tailwind CSS (Styling)
   ↓
Recharts (Visualization)
   ↓
React Query (Data Management)
```

### Key Technologies
- **React 18**: Modern component-based UI
- **Vite**: Lightning-fast build tool
- **Tailwind CSS**: Utility-first CSS framework
- **Recharts**: Beautiful, composable charts
- **React Router**: Client-side routing
- **Axios**: Promise-based HTTP client
- **JWT**: Secure authentication

## 📊 Feature Walkthrough

### 1. Admin Dashboard
After logging in as admin, you'll see:
- **Statistics Cards**: Total clients, vendors, employees, trips
- **Revenue Metrics**: Total revenue and active trips
- **Charts**: Monthly trends, billing model distribution
- **Top Performers**: Vendor performance analysis
- **Alerts**: Recent system notifications

### 2. Client Management
Navigate to **Clients** to:
- View all corporate clients in card layout
- Add new clients with contact information
- Edit existing client details
- Track employees and active vendors per client
- Search and filter clients

### 3. Vendor Management
Navigate to **Vendors** to:
- View vendor profiles with ratings
- Manage vendor information and fleet size
- Track trip counts and performance
- Add/edit/remove vendors
- Monitor vendor ratings

### 4. Employee Management
Navigate to **Employees** to:
- View employee records in table format
- Associate employees with clients
- Track trip history and incentives
- Add new employees
- Edit employee information

### 5. Trip Management
Navigate to **Trips** to:
- View comprehensive trip history
- Filter by status, client, or search term
- See summary statistics (distance, cost, incentives)
- Add new trips with full details
- Track extra hours and incentive calculations

### 6. Billing Models Configuration
Navigate to **Billing Models** to:
- Configure client-vendor billing agreements
- Set up Package Models (fixed monthly cost)
- Configure Trip Models (per-trip billing)
- Create Hybrid Models (combination)
- Define overtime rates
- View and edit existing models

### 7. Report Generation
Navigate to **Reports** to:
- Select report type (Client/Vendor/Employee/Comprehensive)
- Configure date ranges
- Generate detailed reports
- Preview report data
- Export to Excel
- View summary statistics

## 🎨 UI Components

### Dashboard Components
- **StatCard**: Displays key metrics with icons
- **Line Charts**: Show trends over time
- **Bar Charts**: Compare different metrics
- **Pie Charts**: Show distribution percentages
- **Alert Cards**: Display system notifications

### Forms & Modals
- **Responsive Forms**: Mobile-friendly inputs
- **Validation**: Client-side form validation
- **Modal Dialogs**: Clean add/edit interfaces
- **Date Pickers**: Easy date selection
- **Dropdowns**: Searchable select inputs

### Data Display
- **Tables**: Sortable, filterable data tables
- **Cards**: Information displayed in card layout
- **Status Badges**: Color-coded status indicators
- **Search Bars**: Real-time search functionality
- **Filters**: Multi-criteria filtering

## 🔐 Security Features

1. **JWT Authentication**: Secure token-based auth
2. **Role-Based Access**: Different views per role
3. **Protected Routes**: Authentication required
4. **Tenant Isolation**: Data segregation by client
5. **Token Expiration**: Automatic session management
6. **Secure Storage**: Encrypted localStorage

## 📱 Responsive Design

The application is fully responsive and works on:
- **Desktop**: Full-featured experience (1280px+)
- **Tablet**: Optimized layout (768px - 1279px)
- **Mobile**: Touch-friendly interface (< 768px)

## 🚀 Performance Features

- **Code Splitting**: Faster initial load
- **Lazy Loading**: Load components on demand
- **Data Caching**: React Query caching
- **Optimized Bundling**: Vite optimization
- **Tree Shaking**: Remove unused code

## 🧪 Testing the Application

### Test Scenarios

#### 1. Admin Workflow
1. Login as admin
2. View dashboard statistics
3. Add a new client
4. Add a new vendor
5. Configure billing model for client-vendor pair
6. Add an employee to the client
7. Create a trip
8. Generate a report
9. Export report to Excel

#### 2. Vendor Workflow
1. Login as vendor
2. View dashboard performance metrics
3. Check recent trips
4. View revenue and payments
5. Monitor incentives

#### 3. Employee Workflow
1. Login as employee
2. View trip history
3. Check earned incentives
4. Monitor extra hours
5. View monthly trends

## 🐛 Common Issues & Solutions

### Issue: API Connection Failed
**Solution**: Ensure backend API Gateway is running on port 8080

### Issue: Authentication Failed
**Solution**: Check if auth-service is running and database is configured

### Issue: Charts Not Displaying
**Solution**: Verify Recharts is installed: `npm install recharts`

### Issue: Styling Issues
**Solution**: Rebuild Tailwind: `npm run dev`

## 📈 Data Flow

```
User Action
    ↓
React Component
    ↓
API Call (Axios)
    ↓
API Gateway (Port 8080)
    ↓
Microservices (8081-8084)
    ↓
Database
    ↓
Response
    ↓
React Query Cache
    ↓
Component Update
    ↓
UI Re-render
```

## 🎯 Next Steps

1. **Test the application** with demo credentials
2. **Explore all features** across different roles
3. **Generate sample reports** to verify functionality
4. **Configure real data** by replacing mock data with actual API calls
5. **Customize branding** and theme colors
6. **Deploy to production** environment

## 📞 Getting Help

If you encounter any issues:
1. Check the browser console for errors
2. Verify all services are running
3. Check network tab for API calls
4. Review the README documentation
5. Contact the development team

## 🎓 Learning Resources

- React Documentation: https://react.dev
- Tailwind CSS: https://tailwindcss.com
- Recharts: https://recharts.org
- Vite: https://vitejs.dev

---

**Happy coding! 🚀**


# 🏢 Client Service - Setup Complete!

## ✅ Service Status

**Port**: 4010  
**Database**: MySQL (`unified_billing_client`)  
**Status**: Starting...

Watch the CMD window for: `Started ClientServiceApplication`

---

## 📊 Sample Data Loaded

### Clients Created:
1. **Amazon India** (Code: AMZN001)
   - Industry: E-Commerce & Technology
   - Location: Embassy Tech Village, Bengaluru
   - Contact: Rajesh Kumar (transport@amazon.in)

2. **TechCorp Solutions** (Code: TECH001)
   - Industry: IT Services
   - Location: Whitefield, Bengaluru
   - Contact: Priya Sharma (hr@techcorp.com)

3. **Infosys Limited** (Code: INFO001)
   - Industry: IT Services & Consulting
   - Location: Electronics City, Bengaluru
   - Contact: Anil Verma (facilities@infosys.com)

4. **Flipkart** (Code: FLIP001)
   - Industry: E-Commerce
   - Location: Cessna Business Park, Bengaluru
   - Contact: Sneha Reddy (admin@flipkart.com)

---

## 🔌 API Endpoints

### Base URL: `http://localhost:4010`

### Client Operations:

#### 1. Get All Clients
```http
GET /clients
```
**Response**:
```json
[
  {
    "id": "a1111111-1111-1111-1111-111111111111",
    "name": "Amazon India",
    "code": "AMZN001",
    "industry": "E-Commerce & Technology",
    "address": "Embassy Tech Village, Outer Ring Road, Bengaluru, Karnataka 560103",
    "contactEmail": "transport@amazon.in",
    "contactPhone": "+91-80-4619-2000",
    "contactPerson": "Rajesh Kumar",
    "active": true,
    "createdAt": "2024-11-08T...",
    "updatedAt": "2024-11-08T..."
  }
]
```

#### 2. Get Client by ID
```http
GET /clients/{id}
```

#### 3. Get Client by Code
```http
GET /clients/code/AMZN001
```

#### 4. Get Active Clients Only
```http
GET /clients/active
```

#### 5. Create New Client
```http
POST /clients
Content-Type: application/json

{
  "name": "New Company Ltd",
  "code": "NEW001",
  "industry": "Manufacturing",
  "address": "123 Business Street, City",
  "contactEmail": "contact@newcompany.com",
  "contactPhone": "+91-1234567890",
  "contactPerson": "John Doe"
}
```

#### 6. Update Client
```http
PUT /clients/{id}
Content-Type: application/json

{
  "name": "Updated Company Name",
  "industry": "Updated Industry",
  ...
}
```

#### 7. Deactivate Client
```http
PATCH /clients/{id}/deactivate
```

#### 8. Delete Client
```http
DELETE /clients/{id}
```

---

## 🧪 Test the Service

### Using PowerShell:

```powershell
# Get all clients
Invoke-RestMethod -Uri "http://localhost:4010/clients" -Method Get | ConvertTo-Json -Depth 10

# Get Amazon by code
Invoke-RestMethod -Uri "http://localhost:4010/clients/code/AMZN001" -Method Get | ConvertTo-Json -Depth 10

# Create a new client
$newClient = @{
    name = "Accenture India"
    code = "ACC001"
    industry = "Consulting"
    address = "DLF Cyber City, Gurgaon"
    contactEmail = "hr@accenture.in"
    contactPhone = "+91-124-4641000"
    contactPerson = "Sarah Johnson"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:4010/clients" -Method Post -Body $newClient -ContentType "application/json" | ConvertTo-Json -Depth 10
```

### Using curl:

```bash
# Get all clients
curl http://localhost:4010/clients

# Get Amazon
curl http://localhost:4010/clients/code/AMZN001

# Create new client
curl -X POST http://localhost:4010/clients \
  -H "Content-Type: application/json" \
  -d '{"name":"Accenture India","code":"ACC001","industry":"Consulting","address":"DLF Cyber City","contactEmail":"hr@accenture.in","contactPhone":"+91-124-4641000","contactPerson":"Sarah Johnson"}'
```

---

## 📋 Database Schema

### Table: `clients`

| Column | Type | Description |
|--------|------|-------------|
| id | UUID | Primary key |
| name | VARCHAR | Company name |
| code | VARCHAR | Unique client code (e.g., AMZN001) |
| industry | VARCHAR | Industry sector |
| address | TEXT | Full address |
| contact_email | VARCHAR | Contact email |
| contact_phone | VARCHAR | Contact phone |
| contact_person | VARCHAR | Contact person name |
| active | BOOLEAN | Active status (default: true) |
| created_at | TIMESTAMP | Creation timestamp |
| updated_at | TIMESTAMP | Last update timestamp |

---

## 🎯 Next Steps for Amazon Scenario

Now that we have **Amazon India** as our client, we need to:

1. **✅ Client Service** - Done! (Amazon is created)
2. **🔜 Vendor Service** - Create vendors (Ola, Uber, Rapido)
3. **🔜 Employee Service** - Add Amazon employees
4. **🔜 Trip Service** - Track employee trips
5. **🔜 Billing Service** - Calculate billing based on trips

---

## 🔧 Configuration

### Database Connection:
- **URL**: `jdbc:mysql://localhost:3306/unified_billing_client`
- **User**: root
- **Password**: Qwerty@cs12345
- **Auto-create**: Yes

### CORS:
- Enabled for: `http://localhost:3000`, `http://localhost:3001`
- Methods: GET, POST, PUT, DELETE, PATCH, OPTIONS

### API Documentation:
- Swagger UI: `http://localhost:4010/swagger-ui.html`
- OpenAPI Docs: `http://localhost:4010/v3/api-docs`

---

## 🚀 Service Architecture

```
┌─────────────────────────────────────────────┐
│         CLIENT SERVICE (Port 4010)          │
├─────────────────────────────────────────────┤
│  Database: unified_billing_client (MySQL)   │
│                                             │
│  Manages:                                   │
│  • Client Organizations                     │
│  • Client Contact Information               │
│  • Client Status (Active/Inactive)          │
│                                             │
│  Sample Clients:                            │
│  • Amazon India (AMZN001)                   │
│  • TechCorp Solutions (TECH001)             │
│  • Infosys Limited (INFO001)                │
│  • Flipkart (FLIP001)                       │
└─────────────────────────────────────────────┘
```

---

## ✨ Features

✅ MySQL database integration  
✅ Sample data pre-loaded  
✅ CORS enabled for frontend  
✅ RESTful API endpoints  
✅ Input validation  
✅ Exception handling  
✅ Swagger documentation  
✅ UUID-based IDs  
✅ Timestamps (created/updated)  
✅ Active/Inactive status  

---

## 🐛 Troubleshooting

### Service won't start?
- Check if port 4010 is free
- Verify MySQL is running
- Check password: `Qwerty@cs12345`

### Can't connect to database?
- Ensure MySQL is running on port 3306
- Verify credentials in `application.properties`

### No sample data?
- Check logs for SQL errors
- Verify `data.sql` file exists
- Check `spring.sql.init.mode=always` is set

---

**Status**: Client Service is ready! 🎉  
**Next**: Create Vendor Service for Ola, Uber, Rapido


# Day-by-Day Implementation Guide
## Transform Patient Service → Billing Platform

---

## 🎯 Goal: Working MVP in 3-4 Days

This guide will help you transform the existing patient microservices into a working Unified Billing Platform, focusing on the essentials first.

---

## Day 1: Foundation (6-8 hours)

### Morning Session (3-4 hours): Enhance Auth Service

#### Step 1.1: Update User Model (30 min)

**File**: `auth-service/src/main/java/com/pm/authservice/model/User.java`

Add these fields:
```java
@Column(nullable = false)
private UUID tenantId;

@Column
private UUID vendorId;

@Column
private String firstName;

@Column
private String lastName;
```

#### Step 1.2: Create UserRole Enum (15 min)

**New File**: `auth-service/src/main/java/com/pm/authservice/model/UserRole.java`

```java
package com.pm.authservice.model;

public enum UserRole {
    ADMIN,
    CLIENT_MANAGER,
    VENDOR,
    EMPLOYEE
}
```

Update User.java:
```java
@Enumerated(EnumType.STRING)
@Column(nullable = false)
private UserRole role;
```

#### Step 1.3: Update JWT to Include Tenant Context (45 min)

**File**: `auth-service/src/main/java/com/pm/authservice/util/JwtUtil.java`

Update `generateToken`:
```java
public String generateToken(User user) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("userId", user.getId().toString());
    claims.put("tenantId", user.getTenantId().toString());
    claims.put("role", user.getRole().toString());
    if (user.getVendorId() != null) {
        claims.put("vendorId", user.getVendorId().toString());
    }
    
    return Jwts.builder()
        .setClaims(claims)
        .setSubject(user.getEmail())
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
        .signWith(getSecretKey())
        .compact();
}
```

Add extraction methods:
```java
public UUID extractTenantId(String token) {
    return UUID.fromString(extractClaim(token, claims -> 
        claims.get("tenantId", String.class)));
}

public UserRole extractRole(String token) {
    return UserRole.valueOf(extractClaim(token, claims -> 
        claims.get("role", String.class)));
}
```

#### Step 1.4: Update Sample Data (15 min)

**File**: `auth-service/src/main/resources/data.sql`

```sql
-- Update existing user table creation
CREATE TABLE IF NOT EXISTS "users" (
    id UUID PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    tenant_id UUID NOT NULL,
    vendor_id UUID,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    active BOOLEAN DEFAULT true
);

-- Insert sample users
INSERT INTO "users" (id, email, password, role, tenant_id, first_name, last_name)
SELECT 
    '11111111-1111-1111-1111-111111111111',
    'admin@moveinsync.com',
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', -- password: admin123
    'ADMIN',
    '00000000-0000-0000-0000-000000000000',
    'Admin',
    'User'
WHERE NOT EXISTS (SELECT 1 FROM "users" WHERE email = 'admin@moveinsync.com');

INSERT INTO "users" (id, email, password, role, tenant_id, first_name, last_name)
SELECT 
    '22222222-2222-2222-2222-222222222222',
    'manager@techcorp.com',
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
    'CLIENT_MANAGER',
    'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa',
    'Alice',
    'Manager'
WHERE NOT EXISTS (SELECT 1 FROM "users" WHERE email = 'manager@techcorp.com');
```

#### Step 1.5: Test Auth Service (30 min)

```bash
# Start auth database
cd auth-service
mvn spring-boot:run

# Test login
curl -X POST http://localhost:4005/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@moveinsync.com",
    "password": "admin123"
  }'

# Verify JWT contains tenantId and role
# Use jwt.io to decode the token
```

### Afternoon Session (3-4 hours): Transform Patient → Employee Service

#### Step 2.1: Rename Package (30 min)

Use IDE refactoring (right-click → Refactor → Rename):
1. `patientservice` → `employeeservice`
2. `Patient` → `Employee`
3. `PatientRepository` → `EmployeeRepository`
4. `PatientService` → `EmployeeService`
5. `PatientController` → `EmployeeController`

#### Step 2.2: Update Employee Entity (30 min)

**File**: `employee-service/.../model/Employee.java`

```java
package com.pm.employeeservice.model;

import jakarta.persistence.*;
import java.util.UUID;
import java.time.Instant;

@Entity
@Table(name = "employees")
public class Employee {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
    @Column(nullable = false)
    private UUID clientId; // NEW: Tenant ID
    
    @Column(nullable = false, unique = true)
    private String employeeCode; // NEW: e.g., "EMP001"
    
    @Column(nullable = false)
    private String firstName;
    
    @Column(nullable = false)
    private String lastName;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    @Column
    private String phoneNumber;
    
    @Column
    private String homeAddress;
    
    @Column
    private Boolean active = true;
    
    @Column
    private Instant createdAt;
    
    @Column
    private Instant updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }
    
    // Getters and setters
    // ... (generate with IDE)
}
```

#### Step 2.3: Update DTOs (20 min)

**File**: `employee-service/.../dto/EmployeeRequestDTO.java`

```java
public class EmployeeRequestDTO {
    
    @NotNull
    private UUID clientId;
    
    @NotNull
    @Size(min = 3, max = 20)
    private String employeeCode;
    
    @NotNull
    private String firstName;
    
    @NotNull
    private String lastName;
    
    @NotNull
    @Email
    private String email;
    
    private String phoneNumber;
    private String homeAddress;
    
    // Getters/setters
}
```

**File**: `employee-service/.../dto/EmployeeResponseDTO.java`

```java
public class EmployeeResponseDTO {
    private UUID id;
    private UUID clientId;
    private String employeeCode;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Boolean active;
    private Instant createdAt;
    
    // Getters/setters
}
```

#### Step 2.4: Update application.properties (10 min)

**File**: `employee-service/src/main/resources/application.properties`

```properties
spring.application.name=employee-service
server.port=4001

spring.datasource.url=jdbc:postgresql://localhost:5433/employee_db
spring.datasource.username=admin
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=update
spring.sql.init.mode=always
```

#### Step 2.5: Test Employee Service (30 min)

```bash
# Create PostgreSQL database
docker run -d \
  --name employee-db \
  -e POSTGRES_DB=employee_db \
  -e POSTGRES_USER=admin \
  -e POSTGRES_PASSWORD=password \
  -p 5433:5432 \
  postgres:15

# Start employee service
cd employee-service
mvn spring-boot:run

# Test create employee
curl -X POST http://localhost:4001/api/employees \
  -H "Content-Type: application/json" \
  -d '{
    "clientId": "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa",
    "employeeCode": "EMP001",
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@techcorp.com",
    "phoneNumber": "+1234567890"
  }'
```

---

## Day 2: Core Business Logic (6-8 hours)

### Morning Session (3-4 hours): Create Trip Service

#### Step 3.1: Copy Employee Service as Template (15 min)

```bash
cd java-spring-microservices-main
cp -r employee-service trip-service

# Update pom.xml
# Change artifactId to: trip-service
# Change name to: Trip Service
# Change port in application.properties to: 4002
```

#### Step 3.2: Create Trip Entity (45 min)

**File**: `trip-service/.../model/Trip.java`

```java
package com.pm.tripservice.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.Duration;
import java.util.UUID;

@Entity
@Table(name = "trips", indexes = {
    @Index(name = "idx_trip_client", columnList = "clientId"),
    @Index(name = "idx_trip_employee", columnList = "employeeId"),
    @Index(name = "idx_trip_date", columnList = "tripStartTime")
})
public class Trip {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
    @Column(nullable = false)
    private UUID clientId;
    
    @Column(nullable = false)
    private UUID vendorId;
    
    @Column(nullable = false)
    private UUID employeeId;
    
    @Column(nullable = false)
    private String vehicleNumber;
    
    @Column(nullable = false)
    private Instant tripStartTime;
    
    @Column
    private Instant tripEndTime;
    
    @Column(nullable = false)
    private String pickupLocation;
    
    @Column
    private String dropLocation;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal distanceKm;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal durationHours;
    
    @Enumerated(EnumType.STRING)
    @Column
    private TripType tripType;
    
    @Enumerated(EnumType.STRING)
    @Column
    private TripStatus status;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal totalCost;
    
    @Column
    private Boolean billed = false;
    
    @Column
    private Instant createdAt;
    
    @Column
    private Instant updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
        if (status == null) {
            status = TripStatus.SCHEDULED;
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }
    
    // Business method
    public Duration getDuration() {
        if (tripEndTime == null) {
            return Duration.between(tripStartTime, Instant.now());
        }
        return Duration.between(tripStartTime, tripEndTime);
    }
    
    // Getters/setters
}
```

#### Step 3.3: Create Enums (15 min)

**File**: `trip-service/.../model/TripType.java`

```java
public enum TripType {
    OFFICE_TO_HOME,
    HOME_TO_OFFICE,
    ADHOC
}
```

**File**: `trip-service/.../model/TripStatus.java`

```java
public enum TripStatus {
    SCHEDULED,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED
}
```

#### Step 3.4: Create Repository (15 min)

**File**: `trip-service/.../repository/TripRepository.java`

```java
package com.pm.tripservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface TripRepository extends JpaRepository<Trip, UUID> {
    
    List<Trip> findByClientId(UUID clientId);
    
    List<Trip> findByEmployeeId(UUID employeeId);
    
    @Query("SELECT t FROM Trip t WHERE t.clientId = :clientId " +
           "AND t.vendorId = :vendorId " +
           "AND t.tripStartTime >= :startDate " +
           "AND t.tripStartTime < :endDate")
    List<Trip> findByClientVendorAndDateRange(
        @Param("clientId") UUID clientId,
        @Param("vendorId") UUID vendorId,
        @Param("startDate") Instant startDate,
        @Param("endDate") Instant endDate
    );
    
    @Query("SELECT COUNT(t) FROM Trip t WHERE t.clientId = :clientId " +
           "AND t.tripStartTime >= :startOfMonth")
    Integer countCurrentMonth(
        @Param("clientId") UUID clientId,
        @Param("startOfMonth") Instant startOfMonth
    );
}
```

#### Step 3.5: Create Service (45 min)

**File**: `trip-service/.../service/TripService.java`

```java
package com.pm.tripservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class TripService {
    
    @Autowired
    private TripRepository repository;
    
    public Trip createTrip(TripRequestDTO request) {
        Trip trip = new Trip();
        trip.setClientId(request.getClientId());
        trip.setVendorId(request.getVendorId());
        trip.setEmployeeId(request.getEmployeeId());
        trip.setVehicleNumber(request.getVehicleNumber());
        trip.setTripStartTime(request.getTripStartTime());
        trip.setPickupLocation(request.getPickupLocation());
        trip.setTripType(request.getTripType());
        
        return repository.save(trip);
    }
    
    public Trip updateTrip(UUID id, TripRequestDTO request) {
        Trip trip = repository.findById(id)
            .orElseThrow(() -> new TripNotFoundException(id));
        
        trip.setTripEndTime(request.getTripEndTime());
        trip.setDropLocation(request.getDropLocation());
        trip.setDistanceKm(request.getDistanceKm());
        trip.setStatus(TripStatus.COMPLETED);
        
        return repository.save(trip);
    }
    
    public Trip getTrip(UUID id) {
        return repository.findById(id)
            .orElseThrow(() -> new TripNotFoundException(id));
    }
    
    public List<Trip> getTripsByClient(UUID clientId) {
        return repository.findByClientId(clientId);
    }
    
    public List<Trip> getTripsByEmployee(UUID employeeId) {
        return repository.findByEmployeeId(employeeId);
    }
    
    public void deleteTrip(UUID id) {
        repository.deleteById(id);
    }
}
```

#### Step 3.6: Test Trip Service (30 min)

```bash
# Create database
docker run -d \
  --name trip-db \
  -e POSTGRES_DB=trip_db \
  -e POSTGRES_USER=admin \
  -e POSTGRES_PASSWORD=password \
  -p 5434:5432 \
  postgres:15

# Start service
cd trip-service
mvn spring-boot:run

# Test create trip
curl -X POST http://localhost:4002/api/trips \
  -H "Content-Type: application/json" \
  -d '{
    "clientId": "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa",
    "vendorId": "bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb",
    "employeeId": "cccccccc-cccc-cccc-cccc-cccccccccccc",
    "vehicleNumber": "KA-01-AB-1234",
    "tripStartTime": "2024-11-08T09:00:00Z",
    "pickupLocation": "Home",
    "tripType": "HOME_TO_OFFICE"
  }'
```

### Afternoon Session (3-4 hours): Billing Calculation

#### Step 4.1: Create Billing Configuration Entity (30 min)

**File**: `billing-service/.../model/BillingConfiguration.java`

```java
package com.pm.billingservice.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "billing_configurations")
public class BillingConfiguration {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
    @Column(nullable = false)
    private UUID clientId;
    
    @Column(nullable = false)
    private UUID vendorId;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BillingModel billingModel;
    
    @Column
    private Instant validFrom;
    
    @Column
    private Instant validTo;
    
    // Package Model
    @Column(precision = 10, scale = 2)
    private BigDecimal packageCost;
    
    @Column
    private Integer includedTrips;
    
    @Column
    private Integer includedKilometers;
    
    // Trip Model
    @Column(precision = 10, scale = 2)
    private BigDecimal costPerTrip;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal costPerKilometer;
    
    // Overage rates
    @Column(precision = 10, scale = 2)
    private BigDecimal extraKmRate;
    
    // Getters/setters
}

public enum BillingModel {
    PACKAGE,
    TRIP_BASED,
    HYBRID
}
```

#### Step 4.2: Create Billing Calculation Service (1 hour)

**File**: `billing-service/.../service/BillingCalculationService.java`

```java
package com.pm.billingservice.service;

import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class BillingCalculationService {
    
    public BillingStatement calculateBilling(
        UUID clientId,
        UUID vendorId,
        LocalDate startDate,
        LocalDate endDate
    ) {
        // 1. Get configuration
        BillingConfiguration config = getConfig(clientId, vendorId);
        
        // 2. Get trips (mock for now)
        List<TripData> trips = fetchTrips(clientId, vendorId, startDate, endDate);
        
        // 3. Calculate based on model
        return switch (config.getBillingModel()) {
            case PACKAGE -> calculatePackageModel(trips, config);
            case TRIP_BASED -> calculateTripBasedModel(trips, config);
            case HYBRID -> calculateHybridModel(trips, config);
        };
    }
    
    private BillingStatement calculatePackageModel(
        List<TripData> trips,
        BillingConfiguration config
    ) {
        int totalTrips = trips.size();
        BigDecimal totalKm = trips.stream()
            .map(TripData::getDistanceKm)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal baseCost = config.getPackageCost();
        
        // Calculate overages
        int extraTrips = Math.max(0, totalTrips - config.getIncludedTrips());
        BigDecimal extraKm = totalKm
            .subtract(BigDecimal.valueOf(config.getIncludedKilometers()))
            .max(BigDecimal.ZERO);
        
        BigDecimal overageCost = extraKm.multiply(config.getExtraKmRate());
        BigDecimal totalCost = baseCost.add(overageCost);
        
        return BillingStatement.builder()
            .billingModel(BillingModel.PACKAGE)
            .baseCost(baseCost)
            .overageCost(overageCost)
            .totalCost(totalCost)
            .tripCount(totalTrips)
            .totalKilometers(totalKm)
            .includedTrips(config.getIncludedTrips())
            .includedKilometers(config.getIncludedKilometers())
            .extraTrips(extraTrips)
            .extraKilometers(extraKm)
            .build();
    }
    
    private BillingStatement calculateTripBasedModel(
        List<TripData> trips,
        BillingConfiguration config
    ) {
        BigDecimal totalCost = trips.stream()
            .map(trip -> {
                BigDecimal distanceCost = trip.getDistanceKm()
                    .multiply(config.getCostPerKilometer());
                return distanceCost.add(config.getCostPerTrip());
            })
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        return BillingStatement.builder()
            .billingModel(BillingModel.TRIP_BASED)
            .baseCost(totalCost)
            .totalCost(totalCost)
            .tripCount(trips.size())
            .build();
    }
    
    // Mock method - replace with actual REST call
    private List<TripData> fetchTrips(
        UUID clientId, UUID vendorId, 
        LocalDate startDate, LocalDate endDate
    ) {
        // TODO: Call trip-service API
        return List.of(); // Mock empty for now
    }
}
```

#### Step 4.3: Create Billing Statement Response (15 min)

**File**: `billing-service/.../dto/BillingStatement.java`

```java
@Data
@Builder
public class BillingStatement {
    private BillingModel billingModel;
    private BigDecimal baseCost;
    private BigDecimal overageCost;
    private BigDecimal totalCost;
    private Integer tripCount;
    private BigDecimal totalKilometers;
    private Integer includedTrips;
    private Integer includedKilometers;
    private Integer extraTrips;
    private BigDecimal extraKilometers;
    private LocalDate periodStart;
    private LocalDate periodEnd;
}
```

#### Step 4.4: Test Billing Service (30 min)

```bash
# Start billing service
cd billing-service
mvn spring-boot:run

# Test (mock data for now)
curl http://localhost:4003/api/billing/calculate\
?clientId=aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa\
&vendorId=bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb\
&startDate=2024-11-01\
&endDate=2024-11-30
```

---

## Day 3: Frontend Dashboard (6-8 hours)

### Morning Session (3-4 hours): Setup React

#### Step 5.1: Create React App (30 min)

```bash
cd java-spring-microservices-main
npx create-react-app frontend --template typescript
cd frontend

# Install dependencies
npm install @mui/material @emotion/react @emotion/styled
npm install recharts axios react-router-dom
npm install @types/react-router-dom
```

#### Step 5.2: Create Login Component (1 hour)

**File**: `frontend/src/components/auth/LoginForm.tsx`

```typescript
import React, { useState } from 'react';
import {
  Box,
  Card,
  CardContent,
  TextField,
  Button,
  Typography,
  Alert
} from '@mui/material';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

export const LoginForm: React.FC = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleLogin = async () => {
    try {
      const response = await axios.post('http://localhost:4004/auth/login', {
        email,
        password
      });
      
      localStorage.setItem('token', response.data.token);
      localStorage.setItem('role', response.data.role);
      localStorage.setItem('tenantId', response.data.tenantId);
      
      navigate('/dashboard');
    } catch (err) {
      setError('Login failed. Please check your credentials.');
    }
  };

  return (
    <Box
      display="flex"
      justifyContent="center"
      alignItems="center"
      minHeight="100vh"
      bgcolor="#f5f5f5"
    >
      <Card sx={{ width: 400, padding: 2 }}>
        <CardContent>
          <Typography variant="h5" gutterBottom align="center">
            MoveInSync Billing Platform
          </Typography>
          
          {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}
          
          <TextField
            fullWidth
            label="Email"
            margin="normal"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
          />
          
          <TextField
            fullWidth
            label="Password"
            type="password"
            margin="normal"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            onKeyPress={(e) => e.key === 'Enter' && handleLogin()}
          />
          
          <Button
            fullWidth
            variant="contained"
            color="primary"
            size="large"
            sx={{ mt: 3 }}
            onClick={handleLogin}
          >
            Login
          </Button>
          
          <Box mt={2}>
            <Typography variant="caption" color="textSecondary">
              Demo Credentials:<br/>
              admin@moveinsync.com / admin123
            </Typography>
          </Box>
        </CardContent>
      </Card>
    </Box>
  );
};
```

#### Step 5.3: Create Dashboard Component (1.5 hours)

**File**: `frontend/src/components/dashboard/AdminDashboard.tsx`

```typescript
import React, { useEffect, useState } from 'react';
import {
  Box,
  Grid,
  Card,
  CardContent,
  Typography,
  Paper
} from '@mui/material';
import {
  LineChart,
  Line,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  ResponsiveContainer
} from 'recharts';
import axios from 'axios';

interface DashboardStats {
  totalClients: number;
  totalEmployees: number;
  totalTrips: number;
  totalRevenue: number;
  tripTrendData: Array<{ date: string; trips: number }>;
}

export const AdminDashboard: React.FC = () => {
  const [stats, setStats] = useState<DashboardStats>({
    totalClients: 0,
    totalEmployees: 0,
    totalTrips: 0,
    totalRevenue: 0,
    tripTrendData: []
  });
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchDashboardStats();
  }, []);

  const fetchDashboardStats = async () => {
    try {
      const token = localStorage.getItem('token');
      
      // For now, use mock data
      // TODO: Replace with actual API call
      // const response = await axios.get('http://localhost:4004/api/analytics/dashboard', {
      //   headers: { Authorization: `Bearer ${token}` }
      // });
      
      // Mock data for demo
      setStats({
        totalClients: 25,
        totalEmployees: 450,
        totalTrips: 1250,
        totalRevenue: 125000,
        tripTrendData: [
          { date: 'Nov 1', trips: 35 },
          { date: 'Nov 2', trips: 42 },
          { date: 'Nov 3', trips: 38 },
          { date: 'Nov 4', trips: 50 },
          { date: 'Nov 5', trips: 45 },
          { date: 'Nov 6', trips: 55 },
          { date: 'Nov 7', trips: 48 },
          { date: 'Nov 8', trips: 52 }
        ]
      });
      
      setLoading(false);
    } catch (error) {
      console.error('Failed to fetch dashboard stats', error);
      setLoading(false);
    }
  };

  if (loading) {
    return <Typography>Loading...</Typography>;
  }

  return (
    <Box sx={{ padding: 3 }}>
      <Typography variant="h4" gutterBottom>
        Admin Dashboard
      </Typography>
      
      <Grid container spacing={3}>
        {/* KPI Cards */}
        <Grid item xs={12} md={3}>
          <Card>
            <CardContent>
              <Typography color="textSecondary" gutterBottom>
                Total Clients
              </Typography>
              <Typography variant="h3">
                {stats.totalClients}
              </Typography>
            </CardContent>
          </Card>
        </Grid>
        
        <Grid item xs={12} md={3}>
          <Card>
            <CardContent>
              <Typography color="textSecondary" gutterBottom>
                Total Employees
              </Typography>
              <Typography variant="h3">
                {stats.totalEmployees}
              </Typography>
            </CardContent>
          </Card>
        </Grid>
        
        <Grid item xs={12} md={3}>
          <Card>
            <CardContent>
              <Typography color="textSecondary" gutterBottom>
                Total Trips
              </Typography>
              <Typography variant="h3">
                {stats.totalTrips}
              </Typography>
            </CardContent>
          </Card>
        </Grid>
        
        <Grid item xs={12} md={3}>
          <Card>
            <CardContent>
              <Typography color="textSecondary" gutterBottom>
                Revenue (₹)
              </Typography>
              <Typography variant="h3">
                {stats.totalRevenue.toLocaleString()}
              </Typography>
            </CardContent>
          </Card>
        </Grid>

        {/* Trip Trend Chart */}
        <Grid item xs={12}>
          <Paper sx={{ padding: 2 }}>
            <Typography variant="h6" gutterBottom>
              Trip Trend (Last 7 Days)
            </Typography>
            <ResponsiveContainer width="100%" height={300}>
              <LineChart data={stats.tripTrendData}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="date" />
                <YAxis />
                <Tooltip />
                <Line 
                  type="monotone" 
                  dataKey="trips" 
                  stroke="#8884d8" 
                  strokeWidth={2}
                />
              </LineChart>
            </ResponsiveContainer>
          </Paper>
        </Grid>
      </Grid>
    </Box>
  );
};
```

#### Step 5.4: Setup Routing (30 min)

**File**: `frontend/src/App.tsx`

```typescript
import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { LoginForm } from './components/auth/LoginForm';
import { AdminDashboard } from './components/dashboard/AdminDashboard';

const PrivateRoute: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const token = localStorage.getItem('token');
  return token ? <>{children}</> : <Navigate to="/login" />;
};

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<LoginForm />} />
        <Route 
          path="/dashboard" 
          element={
            <PrivateRoute>
              <AdminDashboard />
            </PrivateRoute>
          } 
        />
        <Route path="/" element={<Navigate to="/dashboard" />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
```

#### Step 5.5: Test Frontend (15 min)

```bash
# Start frontend
cd frontend
npm start

# Open browser: http://localhost:3000
# Login with: admin@moveinsync.com / admin123
# Should see dashboard with charts
```

### Afternoon Session (3-4 hours): Integration & Polish

#### Step 6.1: Update API Gateway (30 min)

**File**: `api-gateway/src/main/resources/application.yml`

```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: auth-service-route
          uri: http://localhost:4005
          predicates:
            - Path=/auth/**
          filters:
            - StripPrefix=1

        - id: employee-service-route
          uri: http://localhost:4001
          predicates:
            - Path=/api/employees/**
          filters:
            - StripPrefix=1
            - JwtValidation

        - id: trip-service-route
          uri: http://localhost:4002
          predicates:
            - Path=/api/trips/**
          filters:
            - StripPrefix=1
            - JwtValidation

        - id: billing-service-route
          uri: http://localhost:4003
          predicates:
            - Path=/api/billing/**
          filters:
            - StripPrefix=1
            - JwtValidation
```

#### Step 6.2: Add CORS Configuration (30 min)

**File**: `api-gateway/.../config/CorsConfig.java`

```java
@Configuration
public class CorsConfig {
    
    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:3000");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        
        UrlBasedCorsConfigurationSource source = 
            new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        
        return new CorsWebFilter(source);
    }
}
```

#### Step 6.3: End-to-End Test (1 hour)

```bash
# Start all services in different terminals:

# Terminal 1: Auth service
cd auth-service && mvn spring-boot:run

# Terminal 2: Employee service
cd employee-service && mvn spring-boot:run

# Terminal 3: Trip service
cd trip-service && mvn spring-boot:run

# Terminal 4: Billing service
cd billing-service && mvn spring-boot:run

# Terminal 5: API Gateway
cd api-gateway && mvn spring-boot:run

# Terminal 6: Frontend
cd frontend && npm start
```

**Test Flow**:
1. Open http://localhost:3000
2. Login with admin@moveinsync.com / admin123
3. See dashboard
4. Test API endpoints with Postman

---

## Day 4: Documentation & Demo Prep (4-6 hours)

### Morning Session: Documentation

#### Step 7.1: Create README.md (1 hour)

**File**: `README.md`

```markdown
# Unified Billing & Reporting Platform

## Overview
Multi-tenant microservices platform for managing billing across multiple clients and vendors.

## Features
- ✅ Multi-tenant architecture with RBAC
- ✅ Multiple billing models (Package, Trip-based)
- ✅ Real-time dashboard with analytics
- ✅ JWT authentication
- ✅ Microservices architecture

## Architecture
[Insert diagram here]

## Services
- **API Gateway** (Port 4004): Entry point
- **Auth Service** (Port 4005): Authentication
- **Employee Service** (Port 4001): Employee management
- **Trip Service** (Port 4002): Trip tracking
- **Billing Service** (Port 4003): Billing calculations

## Setup
```bash
# Start databases
docker-compose up -d

# Start each service
cd auth-service && mvn spring-boot:run
cd employee-service && mvn spring-boot:run
cd trip-service && mvn spring-boot:run
cd billing-service && mvn spring-boot:run
cd api-gateway && mvn spring-boot:run

# Start frontend
cd frontend && npm start
```

## Demo Credentials
- Admin: admin@moveinsync.com / admin123
- Manager: manager@techcorp.com / admin123

## API Documentation
Visit: http://localhost:4004/swagger-ui.html
```

#### Step 7.2: Create Architecture Diagram (1 hour)

Use draw.io or similar tool to create:
1. System architecture diagram
2. Database schema diagram
3. Authentication flow diagram

#### Step 7.3: Record Demo Video (1-2 hours)

Record 10-15 minute video showing:
1. Login
2. Dashboard overview
3. Create employee
4. Create trip
5. Generate billing
6. Different user views

### Afternoon Session: Final Polish

#### Step 8.1: Add Sample Data (30 min)

Create SQL scripts to populate:
- 5 clients
- 10 vendors
- 50 employees
- 200 trips

#### Step 8.2: Add Error Handling (30 min)

Review all controllers and add:
- Try-catch blocks
- Meaningful error messages
- HTTP status codes

#### Step 8.3: Code Cleanup (30 min)

- Remove unused imports
- Add JavaDoc comments
- Format code consistently
- Remove debug logs

#### Step 8.4: Final Testing (1 hour)

Test all scenarios:
- ✅ Login with different roles
- ✅ Create/read/update/delete operations
- ✅ Billing calculation
- ✅ Error cases
- ✅ Cross-origin requests

---

## Pre-Interview Checklist

### 1 Day Before:
- [ ] All services start without errors
- [ ] Frontend displays correctly
- [ ] Sample data loaded
- [ ] Demo video ready
- [ ] Documentation complete
- [ ] Code clean and commented
- [ ] Architecture diagrams ready

### Interview Day:
- [ ] Start all services
- [ ] Open browser to dashboard
- [ ] Open IDE with key files
- [ ] Have diagram handy
- [ ] Practice demo once more

---

## Quick Start Commands

```bash
# One-line start (if using Docker Compose)
docker-compose up -d

# Or manually:
# Terminal 1-5: Start services
cd auth-service && mvn spring-boot:run
cd employee-service && mvn spring-boot:run
cd trip-service && mvn spring-boot:run
cd billing-service && mvn spring-boot:run
cd api-gateway && mvn spring-boot:run

# Terminal 6: Start frontend
cd frontend && npm start

# Open browser
open http://localhost:3000
```

---

## Troubleshooting

### Services not starting:
- Check if ports are available
- Verify database connections
- Check application.properties

### CORS errors:
- Verify CorsConfig in API Gateway
- Check allowed origins

### JWT errors:
- Verify secret key matches across services
- Check token expiration

---

Good luck with your implementation! 🚀

Follow this guide step-by-step, and you'll have a working MVP in 3-4 days!


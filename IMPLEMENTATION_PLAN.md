# Unified Billing & Reporting Platform - Implementation Plan
## Interview-Ready Project Transformation Guide

---

## 📋 Executive Summary

This document provides a systematic plan to transform the existing patient microservices project into a comprehensive **Unified Billing & Reporting Platform** for MoveInSync case study.

### Current Architecture:
- ✅ API Gateway with JWT validation
- ✅ Auth Service with JWT tokens
- ✅ Basic microservices structure (Patient, Billing, Analytics)
- ✅ gRPC inter-service communication
- ✅ Kafka event streaming
- ✅ PostgreSQL databases

### Target System:
Multi-tenant billing platform with multi-client, multi-vendor support, multiple billing models, and comprehensive reporting.

---

## 🎯 Phase 1: Foundation & Multi-Tenancy (Days 1-2)

### 1.1 Enhance Auth Service (Multi-tenant + RBAC)

#### Current State:
- Basic JWT authentication
- Single role field in User model
- No tenant isolation

#### Required Changes:

**A. Update User Model** (`auth-service/model/User.java`)
```java
@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    @Column(nullable = false)
    private String password;
    
    // NEW: Multi-tenant support
    @Column(nullable = false)
    private UUID tenantId; // Client organization ID
    
    // NEW: Role-based access control
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role; // ADMIN, VENDOR, EMPLOYEE, CLIENT_MANAGER
    
    // NEW: Additional fields
    @Column
    private String firstName;
    
    @Column
    private String lastName;
    
    @Column
    private UUID vendorId; // For VENDOR role users
    
    @Column
    private Boolean active = true;
    
    @Column
    private Instant createdAt;
    
    @Column
    private Instant updatedAt;
}
```

**B. Create UserRole Enum**
```java
public enum UserRole {
    ADMIN,              // MoveInSync internal admin - full access
    CLIENT_MANAGER,     // Client company manager - view their data
    VENDOR,            // Vendor user - view vendor-specific data
    EMPLOYEE,          // Employee - view personal incentives
    FINANCE_TEAM       // Internal finance - generate reports
}
```

**C. Update JWT to Include Tenant Context**
```java
// JwtUtil.java - generateToken method
public String generateToken(User user) {
    return Jwts.builder()
        .subject(user.getEmail())
        .claim("userId", user.getId().toString())
        .claim("tenantId", user.getTenantId().toString())
        .claim("role", user.getRole().toString())
        .claim("vendorId", user.getVendorId() != null ? 
               user.getVendorId().toString() : null)
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
        .signWith(getSecretKey())
        .compact();
}
```

**D. Create Tenant Isolation Filter** (New: `TenantContext.java`)
```java
@Component
public class TenantContext {
    private static final ThreadLocal<UUID> currentTenant = new ThreadLocal<>();
    private static final ThreadLocal<UserRole> currentRole = new ThreadLocal<>();
    
    public static void setTenantId(UUID tenantId) {
        currentTenant.set(tenantId);
    }
    
    public static UUID getTenantId() {
        return currentTenant.get();
    }
    
    public static void setRole(UserRole role) {
        currentRole.set(role);
    }
    
    public static UserRole getRole() {
        return currentRole.get();
    }
    
    public static void clear() {
        currentTenant.remove();
        currentRole.remove();
    }
}
```

### 1.2 Create New Domain Entities

#### A. Client Entity (New Service: `client-service`)
```
client-service/
├── src/main/java/com/pm/clientservice/
│   ├── ClientServiceApplication.java
│   ├── model/
│   │   ├── Client.java
│   │   └── ClientVendorAssociation.java
│   ├── repository/
│   │   ├── ClientRepository.java
│   │   └── ClientVendorAssociationRepository.java
│   ├── service/
│   │   └── ClientService.java
│   ├── controller/
│   │   └── ClientController.java
│   └── dto/
│       ├── ClientRequestDTO.java
│       └── ClientResponseDTO.java
```

**Client.java**
```java
@Entity
@Table(name = "clients")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(unique = true, nullable = false)
    private String code; // e.g., "CORP001"
    
    @Column
    private String address;
    
    @Column
    private String contactEmail;
    
    @Column
    private String contactPhone;
    
    @Column
    private Boolean active = true;
    
    @Column
    private Instant createdAt;
    
    @Column
    private Instant updatedAt;
}
```

#### B. Vendor Entity (New Service: `vendor-service`)
```java
@Entity
@Table(name = "vendors")
public class Vendor {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(unique = true, nullable = false)
    private String code; // e.g., "VEN001"
    
    @Column
    private String contactEmail;
    
    @Column
    private String contactPhone;
    
    @Column
    private String bankAccountDetails; // Encrypted
    
    @Column
    private Boolean active = true;
    
    @Column
    private Instant createdAt;
}
```

#### C. Employee Entity (Transform `patient-service` → `employee-service`)
```java
@Entity
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
    @Column(nullable = false)
    private UUID clientId; // Which company they work for
    
    @Column(nullable = false)
    private String employeeCode;
    
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
}
```

---

## 🎯 Phase 2: Core Billing Logic (Days 3-5)

### 2.1 Billing Configuration Service

#### A. Billing Model Entity
```java
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
    private BillingModel billingModel; // PACKAGE, TRIP_BASED, HYBRID
    
    @Column
    private Instant validFrom;
    
    @Column
    private Instant validTo;
    
    // Package Model Fields
    @Column
    private BigDecimal packageCost;
    
    @Column
    private Integer includedTrips;
    
    @Column
    private Integer includedKilometers;
    
    // Trip Model Fields
    @Column
    private BigDecimal costPerTrip;
    
    @Column
    private BigDecimal costPerKilometer;
    
    @Column
    private BigDecimal costPerHour;
    
    // Incentive Configuration
    @Column
    private BigDecimal extraKmRate; // Rate for km beyond limit
    
    @Column
    private BigDecimal extraHourRate; // Rate for hours beyond limit
    
    @Column
    private BigDecimal employeeIncentivePercentage;
}

public enum BillingModel {
    PACKAGE,        // Fixed monthly cost
    TRIP_BASED,     // Per trip/km billing
    HYBRID          // Combination
}
```

### 2.2 Trip Management Service (New: `trip-service`)

#### Trip Entity
```java
@Entity
@Table(name = "trips")
@Indexed // For search optimization
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
    @Column(nullable = false)
    private UUID clientId; // Tenant isolation
    
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
    
    @Column
    private BigDecimal distanceKm;
    
    @Column
    private BigDecimal durationHours;
    
    @Enumerated(EnumType.STRING)
    @Column
    private TripType tripType; // OFFICE_TO_HOME, HOME_TO_OFFICE, ADHOC
    
    @Enumerated(EnumType.STRING)
    @Column
    private TripStatus status; // SCHEDULED, IN_PROGRESS, COMPLETED, CANCELLED
    
    // Billing-related
    @Column
    private BigDecimal baseCost;
    
    @Column
    private BigDecimal extraKmCost;
    
    @Column
    private BigDecimal extraHourCost;
    
    @Column
    private BigDecimal totalCost;
    
    @Column
    private UUID billingCycleId; // Link to billing period
    
    @Column
    private Boolean billed = false;
}
```

### 2.3 Billing Calculation Engine (Update `billing-service`)

```java
@Service
public class BillingCalculationService {
    
    /**
     * Calculate billing for a specific client-vendor pair for a billing cycle
     * Time Complexity: O(n) where n = number of trips
     * Space Complexity: O(1) for calculation
     */
    public BillingStatement calculateBilling(
        UUID clientId, 
        UUID vendorId, 
        LocalDate startDate, 
        LocalDate endDate
    ) {
        // 1. Fetch billing configuration
        BillingConfiguration config = getBillingConfig(clientId, vendorId);
        
        // 2. Fetch all trips in period
        List<Trip> trips = tripRepository.findByClientAndVendorAndDateRange(
            clientId, vendorId, startDate, endDate
        );
        
        // 3. Calculate based on model
        return switch (config.getBillingModel()) {
            case PACKAGE -> calculatePackageModel(trips, config);
            case TRIP_BASED -> calculateTripBasedModel(trips, config);
            case HYBRID -> calculateHybridModel(trips, config);
        };
    }
    
    private BillingStatement calculatePackageModel(
        List<Trip> trips, 
        BillingConfiguration config
    ) {
        BigDecimal baseCost = config.getPackageCost();
        int totalTrips = trips.size();
        BigDecimal totalKm = trips.stream()
            .map(Trip::getDistanceKm)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
            
        // Calculate overages
        int extraTrips = Math.max(0, totalTrips - config.getIncludedTrips());
        BigDecimal extraKm = totalKm.subtract(
            BigDecimal.valueOf(config.getIncludedKilometers())
        ).max(BigDecimal.ZERO);
        
        BigDecimal extraCost = extraKm.multiply(config.getExtraKmRate());
        
        return BillingStatement.builder()
            .baseCost(baseCost)
            .extraCost(extraCost)
            .totalCost(baseCost.add(extraCost))
            .totalTrips(totalTrips)
            .totalKilometers(totalKm)
            .build();
    }
    
    // Similar methods for other models...
}
```

### 2.4 Incentive Calculation Service

```java
@Service
public class IncentiveService {
    
    /**
     * Calculate employee incentives for extra hours/trips
     * Time Complexity: O(n) where n = trips per employee
     */
    public EmployeeIncentive calculateEmployeeIncentive(
        UUID employeeId,
        UUID clientId,
        LocalDate startDate,
        LocalDate endDate
    ) {
        List<Trip> employeeTrips = tripRepository
            .findByEmployeeAndDateRange(employeeId, startDate, endDate);
            
        BigDecimal totalIncentive = BigDecimal.ZERO;
        List<IncentiveDetail> details = new ArrayList<>();
        
        for (Trip trip : employeeTrips) {
            // Calculate if trip qualifies for incentive
            // E.g., trips after 8 PM, weekend trips, extra km
            if (qualifiesForIncentive(trip)) {
                BigDecimal incentive = calculateTripIncentive(trip);
                totalIncentive = totalIncentive.add(incentive);
                
                details.add(IncentiveDetail.builder()
                    .tripId(trip.getId())
                    .reason(getIncentiveReason(trip))
                    .amount(incentive)
                    .build());
            }
        }
        
        return EmployeeIncentive.builder()
            .employeeId(employeeId)
            .totalIncentive(totalIncentive)
            .details(details)
            .build();
    }
}
```

---

## 🎯 Phase 3: Reporting Engine (Days 6-7)

### 3.1 Report Service (New: `reporting-service`)

```java
@Service
public class ReportGenerationService {
    
    // Client Report
    public ClientReport generateClientReport(
        UUID clientId, 
        LocalDate startDate, 
        LocalDate endDate
    ) {
        // Aggregate all vendor billings for this client
        List<VendorBilling> vendorBillings = billingService
            .getAllVendorBillingsForClient(clientId, startDate, endDate);
            
        BigDecimal totalCost = vendorBillings.stream()
            .map(VendorBilling::getTotalCost)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
            
        return ClientReport.builder()
            .clientId(clientId)
            .period(new DateRange(startDate, endDate))
            .vendorBillings(vendorBillings)
            .totalCost(totalCost)
            .tripCount(calculateTotalTrips(vendorBillings))
            .employeeBreakdown(getEmployeeBreakdown(clientId, startDate, endDate))
            .build();
    }
    
    // Vendor Report
    public VendorReport generateVendorReport(
        UUID vendorId,
        LocalDate startDate,
        LocalDate endDate
    ) {
        // Aggregate across all clients this vendor serves
        List<ClientBilling> clientBillings = billingService
            .getAllClientBillingsForVendor(vendorId, startDate, endDate);
            
        return VendorReport.builder()
            .vendorId(vendorId)
            .period(new DateRange(startDate, endDate))
            .clientBillings(clientBillings)
            .totalRevenue(calculateTotalRevenue(clientBillings))
            .tripCount(calculateTotalTrips(clientBillings))
            .build();
    }
    
    // Employee Report
    public EmployeeReport generateEmployeeReport(
        UUID employeeId,
        LocalDate startDate,
        LocalDate endDate
    ) {
        List<Trip> trips = tripService.getEmployeeTrips(
            employeeId, startDate, endDate
        );
        
        EmployeeIncentive incentive = incentiveService
            .calculateEmployeeIncentive(employeeId, startDate, endDate);
            
        return EmployeeReport.builder()
            .employeeId(employeeId)
            .period(new DateRange(startDate, endDate))
            .trips(trips)
            .totalIncentive(incentive.getTotalIncentive())
            .incentiveDetails(incentive.getDetails())
            .build();
    }
    
    // Export functionality
    public byte[] exportReportAsPDF(Report report) {
        // Use Apache PDFBox or iText
        return pdfGenerator.generate(report);
    }
    
    public byte[] exportReportAsExcel(Report report) {
        // Use Apache POI
        return excelGenerator.generate(report);
    }
}
```

### 3.2 Analytics Service Enhancement

Update existing `analytics-service` to provide:

```java
@Service
public class AnalyticsService {
    
    // Dashboard Statistics
    public DashboardStats getDashboardStats(UUID tenantId, UserRole role) {
        return switch (role) {
            case ADMIN -> getAdminDashboard(tenantId);
            case CLIENT_MANAGER -> getClientDashboard(tenantId);
            case VENDOR -> getVendorDashboard(tenantId);
            case EMPLOYEE -> getEmployeeDashboard(tenantId);
            default -> throw new UnauthorizedAccessException();
        };
    }
    
    // KPIs for Admin
    private DashboardStats getAdminDashboard(UUID tenantId) {
        return DashboardStats.builder()
            .totalClients(clientRepository.countActive())
            .totalVendors(vendorRepository.countActive())
            .totalTripsThisMonth(tripRepository.countCurrentMonth())
            .totalRevenueThisMonth(calculateMonthlyRevenue())
            .avgTripCost(calculateAvgTripCost())
            .topVendorsByRevenue(getTopVendors(5))
            .topClientsBySpend(getTopClients(5))
            .tripTrendData(getTripTrend(30)) // Last 30 days
            .build();
    }
    
    // Time-series data for charts
    public List<TripTrendData> getTripTrend(int days) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(days);
        
        // Efficient query with grouping
        return tripRepository.getTripsGroupedByDate(startDate, endDate);
    }
}
```

---

## 🎯 Phase 4: Frontend Dashboard (Days 8-9)

### 4.1 Technology Stack
- **Framework**: React + TypeScript
- **UI Library**: Material-UI or Ant Design
- **Charts**: Recharts or Chart.js
- **State Management**: Redux Toolkit or Zustand
- **API Client**: Axios with interceptors for JWT

### 4.2 Dashboard Structure

```
frontend/
├── src/
│   ├── components/
│   │   ├── auth/
│   │   │   ├── LoginForm.tsx
│   │   │   └── PrivateRoute.tsx
│   │   ├── dashboard/
│   │   │   ├── AdminDashboard.tsx
│   │   │   ├── ClientDashboard.tsx
│   │   │   ├── VendorDashboard.tsx
│   │   │   └── EmployeeDashboard.tsx
│   │   ├── billing/
│   │   │   ├── BillingConfigForm.tsx
│   │   │   └── BillingList.tsx
│   │   ├── reports/
│   │   │   ├── ReportGenerator.tsx
│   │   │   └── ReportViewer.tsx
│   │   ├── trips/
│   │   │   ├── TripList.tsx
│   │   │   └── TripDetails.tsx
│   │   └── shared/
│   │       ├── StatCard.tsx
│   │       ├── DataTable.tsx
│   │       └── ChartWidget.tsx
│   ├── services/
│   │   ├── authService.ts
│   │   ├── billingService.ts
│   │   ├── reportService.ts
│   │   └── analyticsService.ts
│   ├── store/
│   │   ├── authSlice.ts
│   │   └── store.ts
│   └── types/
│       ├── user.ts
│       ├── billing.ts
│       └── report.ts
```

### 4.3 Key Dashboard Features

#### Admin Dashboard
```typescript
// AdminDashboard.tsx
const AdminDashboard: React.FC = () => {
  const [stats, setStats] = useState<DashboardStats>();
  
  useEffect(() => {
    analyticsService.getDashboardStats().then(setStats);
  }, []);
  
  return (
    <Grid container spacing={3}>
      {/* KPI Cards */}
      <Grid item xs={12} md={3}>
        <StatCard 
          title="Total Clients" 
          value={stats?.totalClients} 
          icon={<BusinessIcon />}
        />
      </Grid>
      <Grid item xs={12} md={3}>
        <StatCard 
          title="Total Vendors" 
          value={stats?.totalVendors}
          icon={<LocalShippingIcon />}
        />
      </Grid>
      <Grid item xs={12} md={3}>
        <StatCard 
          title="Trips This Month" 
          value={stats?.totalTripsThisMonth}
          icon={<DirectionsCarIcon />}
        />
      </Grid>
      <Grid item xs={12} md={3}>
        <StatCard 
          title="Revenue This Month" 
          value={formatCurrency(stats?.totalRevenueThisMonth)}
          icon={<AttachMoneyIcon />}
        />
      </Grid>
      
      {/* Trip Trend Chart */}
      <Grid item xs={12} md={8}>
        <Paper>
          <Typography variant="h6">Trip Trend (Last 30 Days)</Typography>
          <LineChart data={stats?.tripTrendData}>
            <Line type="monotone" dataKey="tripCount" stroke="#8884d8" />
            <XAxis dataKey="date" />
            <YAxis />
            <Tooltip />
          </LineChart>
        </Paper>
      </Grid>
      
      {/* Top Vendors */}
      <Grid item xs={12} md={4}>
        <Paper>
          <Typography variant="h6">Top Vendors by Revenue</Typography>
          <List>
            {stats?.topVendors.map(v => (
              <ListItem key={v.id}>
                <ListItemText 
                  primary={v.name} 
                  secondary={formatCurrency(v.revenue)} 
                />
              </ListItem>
            ))}
          </List>
        </Paper>
      </Grid>
      
      {/* Billing Configuration */}
      <Grid item xs={12}>
        <BillingConfigManager />
      </Grid>
      
      {/* Report Generation */}
      <Grid item xs={12}>
        <ReportGenerator />
      </Grid>
    </Grid>
  );
};
```

---

## 🎯 Phase 5: Performance & Optimization (Days 10-11)

### 5.1 Caching Strategy

#### Redis Integration
```java
// Add to billing-service
@Configuration
@EnableCaching
public class CacheConfig {
    
    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory factory) {
        RedisCacheConfiguration config = RedisCacheConfiguration
            .defaultCacheConfig()
            .entryTtl(Duration.ofMinutes(10))
            .serializeKeysWith(
                RedisSerializationContext.SerializationPair
                    .fromSerializer(new StringRedisSerializer())
            )
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair
                    .fromSerializer(new GenericJackson2JsonRedisSerializer())
            );
            
        return RedisCacheManager.builder(factory)
            .cacheDefaults(config)
            .build();
    }
}

@Service
public class BillingService {
    
    // Cache billing configurations (rarely change)
    @Cacheable(value = "billingConfigs", key = "#clientId + '_' + #vendorId")
    public BillingConfiguration getBillingConfig(UUID clientId, UUID vendorId) {
        return configRepository.findByClientAndVendor(clientId, vendorId)
            .orElseThrow();
    }
    
    // Cache dashboard stats for short period
    @Cacheable(value = "dashboardStats", key = "#tenantId + '_' + #role")
    public DashboardStats getDashboardStats(UUID tenantId, UserRole role) {
        return analyticsService.generateStats(tenantId, role);
    }
    
    // Evict cache when billing config changes
    @CacheEvict(value = "billingConfigs", key = "#config.clientId + '_' + #config.vendorId")
    public BillingConfiguration updateBillingConfig(BillingConfiguration config) {
        return configRepository.save(config);
    }
}
```

### 5.2 Database Optimization

#### Indexes
```sql
-- trips table
CREATE INDEX idx_trips_client_vendor ON trips(client_id, vendor_id);
CREATE INDEX idx_trips_date_range ON trips(trip_start_time, trip_end_time);
CREATE INDEX idx_trips_employee ON trips(employee_id);
CREATE INDEX idx_trips_billing_cycle ON trips(billing_cycle_id);

-- Composite index for common query
CREATE INDEX idx_trips_tenant_date 
ON trips(client_id, vendor_id, trip_start_time);

-- billing_configurations
CREATE INDEX idx_billing_config_client_vendor 
ON billing_configurations(client_id, vendor_id);
```

#### Query Optimization
```java
// Use pagination for large datasets
public Page<Trip> getTripsByClient(
    UUID clientId, 
    Pageable pageable
) {
    return tripRepository.findByClientId(clientId, pageable);
}

// Use projections for list views (don't fetch all fields)
public interface TripSummary {
    UUID getId();
    String getEmployeeName();
    Instant getTripStartTime();
    BigDecimal getTotalCost();
}

public List<TripSummary> getTripSummaries(UUID clientId, LocalDate date) {
    return tripRepository.findSummariesByClientAndDate(clientId, date);
}
```

### 5.3 Asynchronous Processing

```java
@Service
public class BillingJobService {
    
    @Async("billingTaskExecutor")
    public CompletableFuture<BillingStatement> generateBillingAsync(
        UUID clientId,
        UUID vendorId,
        LocalDate startDate,
        LocalDate endDate
    ) {
        BillingStatement statement = billingService.calculateBilling(
            clientId, vendorId, startDate, endDate
        );
        
        // Send notification when done
        notificationService.sendBillingReady(statement);
        
        return CompletableFuture.completedFuture(statement);
    }
}

@Configuration
public class AsyncConfig {
    @Bean(name = "billingTaskExecutor")
    public Executor billingTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("billing-");
        executor.initialize();
        return executor;
    }
}
```

---

## 🎯 Phase 6: Monitoring & Observability (Day 12)

### 6.1 Spring Boot Actuator

```yaml
# application.yml
management:
  endpoints:
    web:
      exposure:
        include: health,metrics,prometheus,info
  endpoint:
    health:
      show-details: always
  metrics:
    export:
      prometheus:
        enabled: true
```

### 6.2 Logging Strategy

```java
@Aspect
@Component
public class LoggingAspect {
    
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);
    
    @Around("@annotation(com.pm.common.annotations.Loggable)")
    public Object logMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        
        UUID tenantId = TenantContext.getTenantId();
        
        logger.info("Executing {}.{} for tenant: {}", 
            className, methodName, tenantId);
        
        long start = System.currentTimeMillis();
        
        try {
            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - start;
            
            logger.info("Completed {}.{} in {}ms", 
                className, methodName, duration);
                
            return result;
        } catch (Exception e) {
            logger.error("Error in {}.{}: {}", 
                className, methodName, e.getMessage(), e);
            throw e;
        }
    }
}
```

### 6.3 Health Checks

```java
@Component
public class DatabaseHealthIndicator implements HealthIndicator {
    
    @Autowired
    private DataSource dataSource;
    
    @Override
    public Health health() {
        try (Connection conn = dataSource.getConnection()) {
            return Health.up()
                .withDetail("database", "PostgreSQL")
                .withDetail("status", "Available")
                .build();
        } catch (SQLException e) {
            return Health.down()
                .withDetail("error", e.getMessage())
                .build();
        }
    }
}
```

---

## 🎯 Phase 7: Error Handling & Resilience (Day 13)

### 7.1 Global Exception Handler

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(TenantIsolationViolationException.class)
    public ResponseEntity<ErrorResponse> handleTenantViolation(
        TenantIsolationViolationException ex
    ) {
        logger.error("SECURITY ALERT: Tenant isolation violation", ex);
        
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body(ErrorResponse.builder()
                .error("Forbidden")
                .message("Access denied to requested resource")
                .timestamp(Instant.now())
                .build());
    }
    
    @ExceptionHandler(BillingCalculationException.class)
    public ResponseEntity<ErrorResponse> handleBillingError(
        BillingCalculationException ex
    ) {
        logger.error("Billing calculation failed", ex);
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ErrorResponse.builder()
                .error("Billing Error")
                .message("Failed to calculate billing: " + ex.getMessage())
                .timestamp(Instant.now())
                .build());
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericError(Exception ex) {
        logger.error("Unexpected error", ex);
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ErrorResponse.builder()
                .error("Internal Server Error")
                .message("An unexpected error occurred")
                .timestamp(Instant.now())
                .build());
    }
}
```

### 7.2 Circuit Breaker (Resilience4j)

```java
@Service
public class VendorServiceClient {
    
    @CircuitBreaker(name = "vendorService", fallbackMethod = "getVendorFallback")
    @Retry(name = "vendorService", maxAttempts = 3)
    public Vendor getVendor(UUID vendorId) {
        return restTemplate.getForObject(
            vendorServiceUrl + "/vendors/" + vendorId,
            Vendor.class
        );
    }
    
    private Vendor getVendorFallback(UUID vendorId, Exception ex) {
        logger.warn("Vendor service unavailable, using cached data");
        return vendorCache.get(vendorId)
            .orElse(Vendor.builder()
                .id(vendorId)
                .name("Vendor (Service Unavailable)")
                .build());
    }
}
```

---

## 🎯 Phase 8: Testing & Documentation (Day 14)

### 8.1 Unit Tests

```java
@SpringBootTest
class BillingCalculationServiceTest {
    
    @MockBean
    private TripRepository tripRepository;
    
    @MockBean
    private BillingConfigRepository configRepository;
    
    @Autowired
    private BillingCalculationService billingService;
    
    @Test
    void testPackageModelWithOverage() {
        // Given
        UUID clientId = UUID.randomUUID();
        UUID vendorId = UUID.randomUUID();
        
        BillingConfiguration config = BillingConfiguration.builder()
            .billingModel(BillingModel.PACKAGE)
            .packageCost(new BigDecimal("10000"))
            .includedTrips(100)
            .includedKilometers(1000)
            .extraKmRate(new BigDecimal("15"))
            .build();
            
        List<Trip> trips = createMockTrips(120, 1200); // 20 extra trips, 200 extra km
        
        when(configRepository.findByClientAndVendor(clientId, vendorId))
            .thenReturn(Optional.of(config));
        when(tripRepository.findByClientAndVendorAndDateRange(any(), any(), any(), any()))
            .thenReturn(trips);
            
        // When
        BillingStatement result = billingService.calculateBilling(
            clientId, vendorId, LocalDate.now(), LocalDate.now()
        );
        
        // Then
        assertEquals(new BigDecimal("10000"), result.getBaseCost());
        assertEquals(new BigDecimal("3000"), result.getExtraCost()); // 200 km * 15
        assertEquals(new BigDecimal("13000"), result.getTotalCost());
    }
}
```

### 8.2 Integration Tests

```java
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
class BillingIntegrationTest {
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Test
    void testEndToEndBillingFlow() {
        // 1. Create client
        ClientRequestDTO clientRequest = new ClientRequestDTO("Test Corp");
        ResponseEntity<ClientResponseDTO> clientResponse = restTemplate
            .postForEntity("/api/clients", clientRequest, ClientResponseDTO.class);
        UUID clientId = clientResponse.getBody().getId();
        
        // 2. Create vendor
        VendorRequestDTO vendorRequest = new VendorRequestDTO("Test Vendor");
        ResponseEntity<VendorResponseDTO> vendorResponse = restTemplate
            .postForEntity("/api/vendors", vendorRequest, VendorResponseDTO.class);
        UUID vendorId = vendorResponse.getBody().getId();
        
        // 3. Configure billing
        BillingConfigDTO config = createBillingConfig(clientId, vendorId);
        restTemplate.postForEntity("/api/billing/config", config, Void.class);
        
        // 4. Create trips
        for (int i = 0; i < 10; i++) {
            TripRequestDTO trip = createMockTrip(clientId, vendorId);
            restTemplate.postForEntity("/api/trips", trip, Void.class);
        }
        
        // 5. Generate billing
        ResponseEntity<BillingStatement> billingResponse = restTemplate
            .getForEntity(
                "/api/billing/calculate?clientId=" + clientId + 
                "&vendorId=" + vendorId,
                BillingStatement.class
            );
            
        // Assert
        assertNotNull(billingResponse.getBody());
        assertEquals(10, billingResponse.getBody().getTripCount());
    }
}
```

### 8.3 API Documentation (Swagger/OpenAPI)

```java
@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Unified Billing & Reporting API",
        version = "1.0",
        description = "API for multi-client, multi-vendor billing platform",
        contact = @Contact(name = "MoveInSync", email = "support@moveinsync.com")
    ),
    security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT"
)
public class OpenAPIConfig {
}
```

---

## 📊 Complexity Analysis Documentation

### Time Complexity:
- **Trip Creation**: O(1) - Direct database insert
- **Billing Calculation**: O(n) where n = number of trips in period
- **Report Generation**: O(n + m) where n = trips, m = aggregations
- **Dashboard Stats**: O(1) with caching, O(k) without cache where k = data points

### Space Complexity:
- **Billing Calculation**: O(1) - Constant space for calculations
- **Report Generation**: O(n) - Stores report data in memory
- **Caching**: O(m) where m = cached entities

---

## 🗂️ Updated Project Structure

```
unified-billing-platform/
├── api-gateway/                 # Entry point, JWT validation
├── auth-service/               # ✅ Enhanced: Multi-tenant, RBAC
├── client-service/             # ⭐ NEW: Client management
├── vendor-service/             # ⭐ NEW: Vendor management
├── employee-service/           # 🔄 TRANSFORMED from patient-service
├── trip-service/               # ⭐ NEW: Trip management
├── billing-service/            # 🔄 ENHANCED: Billing calculation
├── reporting-service/          # ⭐ NEW: Report generation
├── analytics-service/          # 🔄 ENHANCED: Dashboard stats
├── notification-service/       # Optional: Email notifications
├── frontend/                   # ⭐ NEW: React dashboard
│   ├── src/
│   │   ├── components/
│   │   ├── pages/
│   │   └── services/
├── docker-compose.yml          # All services orchestration
└── README.md
```

---

## 🚀 Implementation Timeline

### Week 1: Core Services
- **Days 1-2**: Multi-tenancy & Auth enhancement
- **Days 3-4**: Client, Vendor, Employee services
- **Days 5-7**: Trip management & Billing logic

### Week 2: Reporting & UI
- **Days 8-9**: Reporting service & Analytics
- **Days 10-12**: Frontend dashboard (all 4 views)
- **Days 13-14**: Testing & documentation

---

## 🎯 Interview Presentation Tips

### What to Highlight:

1. **Architecture Decisions**
   - Microservices for scalability
   - gRPC for inter-service communication
   - Kafka for event streaming
   - Multi-tenant architecture with strict isolation

2. **Security**
   - JWT authentication with tenant context
   - Role-based access control
   - Data isolation at database query level
   - Encrypted sensitive data

3. **Performance**
   - Redis caching for frequently accessed data
   - Database indexing strategy
   - Asynchronous report generation
   - Pagination for large datasets

4. **Trade-offs**
   - Microservices complexity vs. scalability
   - Consistency vs. availability (CAP theorem)
   - Real-time vs. eventual consistency
   - Memory usage vs. response time (caching)

5. **Monitoring**
   - Spring Boot Actuator for health checks
   - Prometheus metrics
   - Structured logging with correlation IDs
   - Circuit breakers for fault tolerance

6. **Extensibility**
   - New billing models can be added via strategy pattern
   - Plugin architecture for report formats
   - Event-driven for adding new consumers

---

## 📝 Documentation Deliverables

1. **Architecture Diagram** (draw.io or LucidChart)
2. **API Documentation** (Swagger UI)
3. **Database Schema** (ER Diagram)
4. **Deployment Guide** (Docker Compose)
5. **Demo Video** (10-15 minutes showing key features)
6. **Complexity Analysis Document**
7. **Trade-offs Document**

---

## ✅ Checklist Before Interview

- [ ] All services running via Docker Compose
- [ ] Frontend dashboard accessible
- [ ] Sample data loaded (clients, vendors, employees, trips)
- [ ] All 4 role-based dashboards working
- [ ] Report generation (PDF/Excel) functional
- [ ] Swagger API docs accessible
- [ ] Demo video recorded
- [ ] Architecture diagram prepared
- [ ] Complexity analysis documented
- [ ] Trade-offs documented
- [ ] Code well-commented
- [ ] README with setup instructions

---

## 🔗 Useful Resources

- Spring Boot Documentation
- React + TypeScript Guide
- Microservices Patterns (Chris Richardson)
- gRPC Java Tutorial
- Redis Caching Best Practices

---

**Good luck with your interview! 🚀**

This is a production-ready, scalable system that demonstrates strong software engineering principles.


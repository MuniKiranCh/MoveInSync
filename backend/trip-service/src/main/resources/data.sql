-- Unified Billing Platform - Realistic Trip and Billing Model Data
-- Client IDs: Amazon (a1111111111111111111111111111111)
-- Vendor IDs: Ola (11111111111111111111111111111111), Uber (22222222222222222222222222222222), Rapido (33333333333333333333333333333333)
-- Employee: Priya Patel (from Amazon)

-- ========================================
-- BILLING MODELS (Client-Vendor specific)
-- ========================================

-- Ola for Amazon: TRIP Model
INSERT INTO billing_models (id, client_id, vendor_id, model_type, rate_per_trip, rate_per_km, extra_km_rate, extra_hour_rate, standard_trip_km, standard_trip_hours, peak_hour_multiplier, active, created_at, updated_at)
VALUES (
    UNHEX('b1111111111111111111111111111111'),
    UNHEX('a1111111111111111111111111111111'),
    UNHEX('11111111111111111111111111111111'),
    'TRIP',
    300.00,  -- Base rate per trip
    20.00,   -- Rate per km
    25.00,   -- Extra km rate (beyond 15km)
    50.00,   -- Extra hour rate (beyond 1 hour)
    15.00,   -- Standard 15km per trip
    1.00,    -- Standard 1 hour per trip
    1.5,     -- 50% extra during peak hours
    1,
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE model_type = model_type;

-- Uber for Amazon: PACKAGE Model
INSERT INTO billing_models (id, client_id, vendor_id, model_type, package_monthly_rate, package_trips_included, package_kms_included, extra_trip_rate, extra_km_rate, extra_hour_rate, standard_trip_km, standard_trip_hours, active, created_at, updated_at)
VALUES (
    UNHEX('b2222222222222222222222222222222'),
    UNHEX('a1111111111111111111111111111111'),
    UNHEX('22222222222222222222222222222222'),
    'PACKAGE',
    25000.00,  -- Monthly package
    100,       -- 100 trips included
    1500.00,   -- 1500 km included
    400.00,    -- Extra trip rate
    22.00,     -- Extra km rate
    55.00,     -- Extra hour rate
    15.00,     -- Standard km per trip
    1.00,      -- Standard hour per trip
    1,
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE model_type = model_type;

-- Rapido for Amazon: HYBRID Model
INSERT INTO billing_models (id, client_id, vendor_id, model_type, package_monthly_rate, package_trips_included, package_kms_included, rate_per_trip, rate_per_km, extra_km_rate, extra_hour_rate, standard_trip_km, standard_trip_hours, active, created_at, updated_at)
VALUES (
    UNHEX('b3333333333333333333333333333333'),
    UNHEX('a1111111111111111111111111111111'),
    UNHEX('33333333333333333333333333333333'),
    'HYBRID',
    15000.00,  -- Monthly base
    50,        -- 50 trips included
    750.00,    -- 750 km included
    250.00,    -- Rate per extra trip
    18.00,     -- Rate per km (for extra trips)
    20.00,     -- Extra km rate
    45.00,     -- Extra hour rate
    12.00,     -- Standard km per trip
    0.75,      -- Standard 45 min per trip
    1,
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE model_type = model_type;

-- ========================================
-- ADDITIONAL BILLING MODELS FOR OLA (OLA001)
-- Multiple Package Options for Different Client Needs
-- ========================================

-- OLA PACKAGE MODEL 1: Premium Enterprise Package for Amazon
-- High volume, comprehensive coverage
INSERT INTO billing_models (id, client_id, vendor_id, model_type, package_monthly_rate, package_trips_included, package_kms_included, extra_trip_rate, extra_km_rate, extra_hour_rate, standard_trip_km, standard_trip_hours, active, created_at, updated_at)
VALUES (
    UNHEX('b4444444444444444444444444444444'),
    UNHEX('a1111111111111111111111111111111'),  -- Amazon
    UNHEX('11111111111111111111111111111111'),  -- Ola
    'PACKAGE',
    45000.00,  -- Premium monthly package
    150,       -- 150 trips included
    2500.00,   -- 2500 km included
    350.00,    -- Extra trip rate
    22.00,     -- Extra km rate (beyond package limit)
    55.00,     -- Extra hour rate (beyond 1 hour)
    16.00,     -- Standard 16km per trip
    1.00,      -- Standard 1 hour per trip
    1,
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE model_type = model_type;

-- OLA PACKAGE MODEL 2: Standard Business Package for TechCorp
-- Mid-tier package for medium-sized companies
INSERT INTO billing_models (id, client_id, vendor_id, model_type, package_monthly_rate, package_trips_included, package_kms_included, extra_trip_rate, extra_km_rate, extra_hour_rate, standard_trip_km, standard_trip_hours, active, created_at, updated_at)
VALUES (
    UNHEX('b5555555555555555555555555555555'),
    UNHEX('a2222222222222222222222222222222'),  -- TechCorp
    UNHEX('11111111111111111111111111111111'),  -- Ola
    'PACKAGE',
    30000.00,  -- Standard monthly package
    120,       -- 120 trips included
    1800.00,   -- 1800 km included
    320.00,    -- Extra trip rate
    20.00,     -- Extra km rate
    50.00,     -- Extra hour rate
    15.00,     -- Standard 15km per trip
    1.00,      -- Standard 1 hour per trip
    1,
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE model_type = model_type;

-- OLA PACKAGE MODEL 3: Startup Package 
-- Budget-friendly package for smaller operations
INSERT INTO billing_models (id, client_id, vendor_id, model_type, package_monthly_rate, package_trips_included, package_kms_included, extra_trip_rate, extra_km_rate, extra_hour_rate, standard_trip_km, standard_trip_hours, active, created_at, updated_at)
VALUES (
    UNHEX('b6666666666666666666666666666666'),
    UNHEX('a3333333333333333333333333333333'),  -- FinServe Solutions (or any third client)
    UNHEX('11111111111111111111111111111111'),  -- Ola
    'PACKAGE',
    18000.00,  -- Startup monthly package
    80,        -- 80 trips included
    1200.00,   -- 1200 km included
    280.00,    -- Extra trip rate
    18.00,     -- Extra km rate
    45.00,     -- Extra hour rate
    15.00,     -- Standard 15km per trip
    1.00,      -- Standard 1 hour per trip
    1,
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE model_type = model_type;

-- OLA HYBRID MODEL 1: Flexible Executive Package for Amazon
-- Combines base package with flexible per-trip billing
INSERT INTO billing_models (id, client_id, vendor_id, model_type, package_monthly_rate, package_trips_included, package_kms_included, rate_per_trip, rate_per_km, extra_km_rate, extra_hour_rate, standard_trip_km, standard_trip_hours, peak_hour_multiplier, active, created_at, updated_at)
VALUES (
    UNHEX('b7777777777777777777777777777777'),
    UNHEX('a1111111111111111111111111111111'),  -- Amazon
    UNHEX('11111111111111111111111111111111'),  -- Ola
    'HYBRID',
    20000.00,  -- Base monthly fee
    60,        -- 60 trips included in base
    900.00,    -- 900 km included
    280.00,    -- Rate per additional trip
    15.00,     -- Rate per km for additional trips
    20.00,     -- Extra km rate beyond standard
    48.00,     -- Extra hour rate
    15.00,     -- Standard km per trip
    1.00,      -- Standard hour per trip
    1.3,       -- 30% peak hour surcharge
    1,
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE model_type = model_type;

-- OLA HYBRID MODEL 2: Smart Corporate Package for TechCorp
-- Best for companies with variable monthly usage
INSERT INTO billing_models (id, client_id, vendor_id, model_type, package_monthly_rate, package_trips_included, package_kms_included, rate_per_trip, rate_per_km, extra_km_rate, extra_hour_rate, standard_trip_km, standard_trip_hours, peak_hour_multiplier, active, created_at, updated_at)
VALUES (
    UNHEX('b8888888888888888888888888888888'),
    UNHEX('a2222222222222222222222222222222'),  -- TechCorp
    UNHEX('11111111111111111111111111111111'),  -- Ola
    'HYBRID',
    12000.00,  -- Lower base monthly fee
    40,        -- 40 trips included
    600.00,    -- 600 km included
    260.00,    -- Rate per additional trip
    16.00,     -- Rate per km for additional trips
    22.00,     -- Extra km rate
    50.00,     -- Extra hour rate
    15.00,     -- Standard km per trip
    1.00,      -- Standard hour per trip
    1.4,       -- 40% peak hour surcharge
    1,
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE model_type = model_type;

-- OLA TRIP MODEL 2: Pay-As-You-Go Premium for TechCorp
-- No monthly commitment, higher per-trip rates
INSERT INTO billing_models (id, client_id, vendor_id, model_type, rate_per_trip, rate_per_km, extra_km_rate, extra_hour_rate, standard_trip_km, standard_trip_hours, peak_hour_multiplier, active, created_at, updated_at)
VALUES (
    UNHEX('b9999999999999999999999999999999'),
    UNHEX('a2222222222222222222222222222222'),  -- TechCorp
    UNHEX('11111111111111111111111111111111'),  -- Ola
    'TRIP',
    350.00,  -- Higher base rate per trip (no commitment)
    22.00,   -- Rate per km
    28.00,   -- Extra km rate (beyond 15km)
    55.00,   -- Extra hour rate (beyond 1 hour)
    15.00,   -- Standard 15km per trip
    1.00,    -- Standard 1 hour per trip
    1.5,     -- 50% peak hour surcharge
    1,
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE model_type = model_type;

-- OLA TRIP MODEL 3: Economy Pay-Per-Trip
-- Budget option with basic features
INSERT INTO billing_models (id, client_id, vendor_id, model_type, rate_per_trip, rate_per_km, extra_km_rate, extra_hour_rate, standard_trip_km, standard_trip_hours, peak_hour_multiplier, active, created_at, updated_at)
VALUES (
    UNHEX('ba111111111111111111111111111111'),
    UNHEX('a3333333333333333333333333333333'),  -- FinServe or third client
    UNHEX('11111111111111111111111111111111'),  -- Ola
    'TRIP',
    250.00,  -- Economy base rate per trip
    18.00,   -- Rate per km
    23.00,   -- Extra km rate
    45.00,   -- Extra hour rate
    12.00,   -- Standard 12km per trip (shorter)
    0.75,    -- Standard 45 minutes per trip
    1.3,     -- 30% peak hour surcharge
    1,
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE model_type = model_type;

-- ========================================
-- REALISTIC TRIPS FOR NOVEMBER 2024
-- ========================================

-- Ola Trips (TRIP Model - more profitable for longer distances)
-- Trip 1: Morning commute - Standard
INSERT INTO trips (id, client_id, vendor_id, employee_id, vehicle_number, driver_name, driver_phone, trip_start_time, trip_end_time, pickup_location, drop_location, distance_km, duration_hours, trip_type, status, base_cost, extra_km_cost, extra_hour_cost, total_cost, billed, created_at, updated_at)
VALUES (
    UNHEX('01111111111111111111111111111111'),
    UNHEX('a1111111111111111111111111111111'),
    UNHEX('11111111111111111111111111111111'),
    UNHEX('44444444444444444444444444444444'),  -- Priya Patel
    'KA-01-MH-5678',
    'Rajesh Kumar',
    '+91-98765-11111',
    '2024-11-01 08:30:00',
    '2024-11-01 09:25:00',
    'Whitefield, Bengaluru',
    'Amazon Office, Bellandur',
    14.5,
    0.92,
    'HOME_TO_OFFICE',
    'COMPLETED',
    300.00,  -- Base
    0.00,    -- No extra km (14.5 < 15)
    0.00,    -- No extra hour
    300.00,
    0,
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE status = status;

-- Trip 2: Evening return - Extra km
INSERT INTO trips (id, client_id, vendor_id, employee_id, vehicle_number, driver_name, driver_phone, trip_start_time, trip_end_time, pickup_location, drop_location, distance_km, duration_hours, trip_type, status, base_cost, extra_km_cost, extra_hour_cost, total_cost, billed, created_at, updated_at)
VALUES (
    UNHEX('02222222222222222222222222222222'),
    UNHEX('a1111111111111111111111111111111'),
    UNHEX('11111111111111111111111111111111'),
    UNHEX('44444444444444444444444444444444'),
    'KA-01-MH-5678',
    'Rajesh Kumar',
    '+91-98765-11111',
    '2024-11-01 18:45:00',
    '2024-11-01 20:00:00',
    'Amazon Office, Bellandur',
    'Whitefield, Bengaluru',
    18.2,
    1.25,
    'OFFICE_TO_HOME',
    'COMPLETED',
    300.00,
    80.00,   -- 3.2 extra km * 25
    12.50,   -- 0.25 extra hour * 50
    392.50,
    0,
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE status = status;

-- Trip 3: Long distance trip
INSERT INTO trips (id, client_id, vendor_id, employee_id, vehicle_number, driver_name, driver_phone, trip_start_time, trip_end_time, pickup_location, drop_location, distance_km, duration_hours, trip_type, status, base_cost, extra_km_cost, extra_hour_cost, total_cost, billed, created_at, updated_at)
VALUES (
    UNHEX('03333333333333333333333333333333'),
    UNHEX('a1111111111111111111111111111111'),
    UNHEX('11111111111111111111111111111111'),
    UNHEX('44444444444444444444444444444444'),
    'KA-01-MH-9012',
    'Suresh Reddy',
    '+91-98765-22222',
    '2024-11-05 09:00:00',
    '2024-11-05 10:45:00',
    'Koramangala, Bengaluru',
    'Electronics City, Bengaluru',
    25.8,
    1.75,
    'HOME_TO_OFFICE',
    'COMPLETED',
    300.00,
    270.00,  -- 10.8 extra km * 25
    37.50,   -- 0.75 extra hour * 50
    607.50,
    0,
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE status = status;

-- Uber Trips (PACKAGE Model - included in package)
-- Trip 4: Short trip within package
INSERT INTO trips (id, client_id, vendor_id, employee_id, vehicle_number, driver_name, driver_phone, trip_start_time, trip_end_time, pickup_location, drop_location, distance_km, duration_hours, trip_type, status, base_cost, extra_km_cost, extra_hour_cost, total_cost, billed, created_at, updated_at)
VALUES (
    UNHEX('04444444444444444444444444444444'),
    UNHEX('a1111111111111111111111111111111'),
    UNHEX('22222222222222222222222222222222'),
    UNHEX('44444444444444444444444444444444'),
    'KA-02-AB-1234',
    'Priya Sharma',
    '+91-98765-33333',
    '2024-11-02 08:15:00',
    '2024-11-02 09:00:00',
    'Indiranagar, Bengaluru',
    'Amazon Office, Bellandur',
    12.3,
    0.75,
    'HOME_TO_OFFICE',
    'COMPLETED',
    0.00,    -- Included in package
    0.00,
    0.00,
    0.00,    -- Covered by monthly package
    0,
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE status = status;

-- Trip 5: Within package
INSERT INTO trips (id, client_id, vendor_id, employee_id, vehicle_number, driver_name, driver_phone, trip_start_time, trip_end_time, pickup_location, drop_location, distance_km, duration_hours, trip_type, status, base_cost, extra_km_cost, extra_hour_cost, total_cost, billed, created_at, updated_at)
VALUES (
    UNHEX('05555555555555555555555555555555'),
    UNHEX('a1111111111111111111111111111111'),
    UNHEX('22222222222222222222222222222222'),
    UNHEX('44444444444444444444444444444444'),
    'KA-02-CD-5678',
    'Amit Verma',
    '+91-98765-44444',
    '2024-11-02 19:00:00',
    '2024-11-02 19:50:00',
    'Amazon Office, Bellandur',
    'Indiranagar, Bengaluru',
    13.1,
    0.83,
    'OFFICE_TO_HOME',
    'COMPLETED',
    0.00,
    0.00,
    0.00,
    0.00,
    0,
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE status = status;

-- Rapido Trips (HYBRID Model - bike taxi, shorter distances)
-- Trip 6: Quick commute within package
INSERT INTO trips (id, client_id, vendor_id, employee_id, vehicle_number, driver_name, driver_phone, trip_start_time, trip_end_time, pickup_location, drop_location, distance_km, duration_hours, trip_type, status, base_cost, extra_km_cost, extra_hour_cost, total_cost, billed, created_at, updated_at)
VALUES (
    UNHEX('06666666666666666666666666666666'),
    UNHEX('a1111111111111111111111111111111'),
    UNHEX('33333333333333333333333333333333'),
    UNHEX('44444444444444444444444444444444'),
    'KA-03-XY-9999',
    'Rohan Kumar',
    '+91-98765-55555',
    '2024-11-03 08:45:00',
    '2024-11-03 09:20:00',
    'HSR Layout, Bengaluru',
    'Amazon Office, Bellandur',
    8.5,
    0.58,
    'HOME_TO_OFFICE',
    'COMPLETED',
    0.00,    -- Within monthly package
    0.00,
    0.00,
    0.00,
    0,
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE status = status;

-- Trip 7: Extra km beyond standard
INSERT INTO trips (id, client_id, vendor_id, employee_id, vehicle_number, driver_name, driver_phone, trip_start_time, trip_end_time, pickup_location, drop_location, distance_km, duration_hours, trip_type, status, base_cost, extra_km_cost, extra_hour_cost, total_cost, billed, created_at, updated_at)
VALUES (
    UNHEX('07777777777777777777777777777777'),
    UNHEX('a1111111111111111111111111111111'),
    UNHEX('33333333333333333333333333333333'),
    UNHEX('44444444444444444444444444444444'),
    'KA-03-XY-8888',
    'Vikram Singh',
    '+91-98765-66666',
    '2024-11-03 19:15:00',
    '2024-11-03 20:10:00',
    'Amazon Office, Bellandur',
    'Marathahalli, Bengaluru',
    15.8,
    0.92,
    'OFFICE_TO_HOME',
    'COMPLETED',
    0.00,    -- Within package (trip count)
    76.00,   -- 3.8 extra km * 20
    0.00,
    76.00,
    0,
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE status = status;

-- Add more trips for realistic monthly data
-- Week 2 trips
INSERT INTO trips (id, client_id, vendor_id, employee_id, vehicle_number, driver_name, driver_phone, trip_start_time, trip_end_time, pickup_location, drop_location, distance_km, duration_hours, trip_type, status, base_cost, extra_km_cost, extra_hour_cost, total_cost, billed, created_at, updated_at)
VALUES 
(UNHEX('08888888888888888888888888888888'), UNHEX('a1111111111111111111111111111111'), UNHEX('11111111111111111111111111111111'), UNHEX('44444444444444444444444444444444'), 'KA-01-MH-5678', 'Rajesh Kumar', '+91-98765-11111', '2024-11-08 08:30:00', '2024-11-08 09:20:00', 'Whitefield, Bengaluru', 'Amazon Office, Bellandur', 14.2, 0.83, 'HOME_TO_OFFICE', 'COMPLETED', 300.00, 0.00, 0.00, 300.00, 0, NOW(), NOW()),
(UNHEX('09999999999999999999999999999999'), UNHEX('a1111111111111111111111111111111'), UNHEX('11111111111111111111111111111111'), UNHEX('44444444444444444444444444444444'), 'KA-01-MH-5678', 'Rajesh Kumar', '+91-98765-11111', '2024-11-08 19:00:00', '2024-11-08 20:10:00', 'Amazon Office, Bellandur', 'Whitefield, Bengaluru', 16.5, 1.17, 'OFFICE_TO_HOME', 'COMPLETED', 300.00, 37.50, 8.50, 346.00, 0, NOW(), NOW())
ON DUPLICATE KEY UPDATE status = status;

-- ========================================
-- TRIPS FOR NEW EMPLOYEES (Priya Patel, Rahul Verma, Anita Kumar)
-- Generated for November 2024
-- ========================================

-- Trips for Priya Patel (e0000001-0000-0000-0000-000000000001) - IT Department
-- Week 1 - OLA Trips
INSERT INTO trips (id, client_id, vendor_id, employee_id, vehicle_number, driver_name, driver_phone, trip_start_time, trip_end_time, pickup_location, drop_location, distance_km, duration_hours, trip_type, status, base_cost, extra_km_cost, extra_hour_cost, total_cost, billed, created_at, updated_at)
VALUES 
(UNHEX('0a111111111111111111111111111111'), UNHEX('a1111111111111111111111111111111'), UNHEX('11111111111111111111111111111111'), UNHEX(REPLACE('e0000001-0000-0000-0000-000000000001', '-', '')), 'KA-01-AB-1234', 'Suresh Gowda', '+91-98765-77777', '2024-11-04 08:30:00', '2024-11-04 09:15:00', 'Whitefield, Bengaluru', 'Amazon Embassy Tech Village', 12.5, 0.75, 'HOME_TO_OFFICE', 'COMPLETED', 300.00, 0.00, 0.00, 300.00, 0, NOW(), NOW()),
(UNHEX('0a222222222222222222222222222222'), UNHEX('a1111111111111111111111111111111'), UNHEX('11111111111111111111111111111111'), UNHEX(REPLACE('e0000001-0000-0000-0000-000000000001', '-', '')), 'KA-01-AB-1234', 'Suresh Gowda', '+91-98765-77777', '2024-11-04 18:30:00', '2024-11-04 19:25:00', 'Amazon Embassy Tech Village', 'Whitefield, Bengaluru', 13.2, 0.92, 'OFFICE_TO_HOME', 'COMPLETED', 300.00, 0.00, 0.00, 300.00, 0, NOW(), NOW()),
(UNHEX('0a333333333333333333333333333333'), UNHEX('a1111111111111111111111111111111'), UNHEX('11111111111111111111111111111111'), UNHEX(REPLACE('e0000001-0000-0000-0000-000000000001', '-', '')), 'KA-01-CD-5678', 'Ramesh Kumar', '+91-98765-88888', '2024-11-05 09:00:00', '2024-11-05 09:50:00', 'Whitefield, Bengaluru', 'Amazon Embassy Tech Village', 14.8, 0.83, 'HOME_TO_OFFICE', 'COMPLETED', 300.00, 0.00, 0.00, 300.00, 0, NOW(), NOW()),
(UNHEX('0a444444444444444444444444444444'), UNHEX('a1111111111111111111111111111111'), UNHEX('11111111111111111111111111111111'), UNHEX(REPLACE('e0000001-0000-0000-0000-000000000001', '-', '')), 'KA-01-CD-5678', 'Ramesh Kumar', '+91-98765-88888', '2024-11-05 19:15:00', '2024-11-05 20:30:00', 'Amazon Embassy Tech Village', 'Whitefield, Bengaluru', 17.5, 1.25, 'OFFICE_TO_HOME', 'COMPLETED', 300.00, 62.50, 12.50, 375.00, 0, NOW(), NOW())
ON DUPLICATE KEY UPDATE status = status;

-- Week 2 - UBER Trips (Package)
INSERT INTO trips (id, client_id, vendor_id, employee_id, vehicle_number, driver_name, driver_phone, trip_start_time, trip_end_time, pickup_location, drop_location, distance_km, duration_hours, trip_type, status, base_cost, extra_km_cost, extra_hour_cost, total_cost, billed, created_at, updated_at)
VALUES 
(UNHEX('0a555555555555555555555555555555'), UNHEX('a1111111111111111111111111111111'), UNHEX('22222222222222222222222222222222'), UNHEX(REPLACE('e0000001-0000-0000-0000-000000000001', '-', '')), 'KA-02-XY-9876', 'Anjali Mehta', '+91-98765-99999', '2024-11-11 08:15:00', '2024-11-11 09:00:00', 'Whitefield, Bengaluru', 'Amazon Embassy Tech Village', 11.5, 0.75, 'HOME_TO_OFFICE', 'COMPLETED', 0.00, 0.00, 0.00, 0.00, 0, NOW(), NOW()),
(UNHEX('0a666666666666666666666666666666'), UNHEX('a1111111111111111111111111111111'), UNHEX('22222222222222222222222222222222'), UNHEX(REPLACE('e0000001-0000-0000-0000-000000000001', '-', '')), 'KA-02-XY-9876', 'Anjali Mehta', '+91-98765-99999', '2024-11-11 18:45:00', '2024-11-11 19:30:00', 'Amazon Embassy Tech Village', 'Whitefield, Bengaluru', 12.0, 0.75, 'OFFICE_TO_HOME', 'COMPLETED', 0.00, 0.00, 0.00, 0.00, 0, NOW(), NOW())
ON DUPLICATE KEY UPDATE status = status;

-- Trips for Rahul Verma (e0000002-0000-0000-0000-000000000002) - Operations
INSERT INTO trips (id, client_id, vendor_id, employee_id, vehicle_number, driver_name, driver_phone, trip_start_time, trip_end_time, pickup_location, drop_location, distance_km, duration_hours, trip_type, status, base_cost, extra_km_cost, extra_hour_cost, total_cost, billed, created_at, updated_at)
VALUES 
(UNHEX('0b111111111111111111111111111111'), UNHEX('a1111111111111111111111111111111'), UNHEX('11111111111111111111111111111111'), UNHEX(REPLACE('e0000002-0000-0000-0000-000000000002', '-', '')), 'KA-01-EF-2345', 'Prakash Singh', '+91-97654-11111', '2024-11-06 07:45:00', '2024-11-06 08:40:00', 'Koramangala, Bengaluru', 'Amazon Embassy Tech Village', 16.2, 0.92, 'HOME_TO_OFFICE', 'COMPLETED', 300.00, 30.00, 0.00, 330.00, 0, NOW(), NOW()),
(UNHEX('0b222222222222222222222222222222'), UNHEX('a1111111111111111111111111111111'), UNHEX('11111111111111111111111111111111'), UNHEX(REPLACE('e0000002-0000-0000-0000-000000000002', '-', '')), 'KA-01-EF-2345', 'Prakash Singh', '+91-97654-11111', '2024-11-06 19:30:00', '2024-11-06 20:45:00', 'Amazon Embassy Tech Village', 'Koramangala, Bengaluru', 18.5, 1.25, 'OFFICE_TO_HOME', 'COMPLETED', 300.00, 87.50, 12.50, 400.00, 0, NOW(), NOW()),
(UNHEX('0b333333333333333333333333333333'), UNHEX('a1111111111111111111111111111111'), UNHEX('33333333333333333333333333333333'), UNHEX(REPLACE('e0000002-0000-0000-0000-000000000002', '-', '')), 'KA-03-ZZ-7777', 'Deepak Rao', '+91-97654-22222', '2024-11-07 08:00:00', '2024-11-07 08:35:00', 'Koramangala, Bengaluru', 'Amazon Embassy Tech Village', 9.5, 0.58, 'HOME_TO_OFFICE', 'COMPLETED', 0.00, 0.00, 0.00, 0.00, 0, NOW(), NOW()),
(UNHEX('0b444444444444444444444444444444'), UNHEX('a1111111111111111111111111111111'), UNHEX('33333333333333333333333333333333'), UNHEX(REPLACE('e0000002-0000-0000-0000-000000000002', '-', '')), 'KA-03-ZZ-7777', 'Deepak Rao', '+91-97654-22222', '2024-11-07 18:15:00', '2024-11-07 18:55:00', 'Amazon Embassy Tech Village', 'Koramangala, Bengaluru', 10.2, 0.67, 'OFFICE_TO_HOME', 'COMPLETED', 0.00, 0.00, 0.00, 0.00, 0, NOW(), NOW())
ON DUPLICATE KEY UPDATE status = status;

-- Trips for Anita Kumar (e0000003-0000-0000-0000-000000000003) - HR
INSERT INTO trips (id, client_id, vendor_id, employee_id, vehicle_number, driver_name, driver_phone, trip_start_time, trip_end_time, pickup_location, drop_location, distance_km, duration_hours, trip_type, status, base_cost, extra_km_cost, extra_hour_cost, total_cost, billed, created_at, updated_at)
VALUES 
(UNHEX('0c111111111111111111111111111111'), UNHEX('a1111111111111111111111111111111'), UNHEX('22222222222222222222222222222222'), UNHEX(REPLACE('e0000003-0000-0000-0000-000000000003', '-', '')), 'KA-02-GH-3456', 'Lakshmi Reddy', '+91-97654-33333', '2024-11-08 08:30:00', '2024-11-08 09:15:00', 'Indiranagar, Bengaluru', 'Amazon Embassy Tech Village', 10.5, 0.75, 'HOME_TO_OFFICE', 'COMPLETED', 0.00, 0.00, 0.00, 0.00, 0, NOW(), NOW()),
(UNHEX('0c222222222222222222222222222222'), UNHEX('a1111111111111111111111111111111'), UNHEX('22222222222222222222222222222222'), UNHEX(REPLACE('e0000003-0000-0000-0000-000000000003', '-', '')), 'KA-02-GH-3456', 'Lakshmi Reddy', '+91-97654-33333', '2024-11-08 18:00:00', '2024-11-08 18:50:00', 'Amazon Embassy Tech Village', 'Indiranagar, Bengaluru', 11.2, 0.83, 'OFFICE_TO_HOME', 'COMPLETED', 0.00, 0.00, 0.00, 0.00, 0, NOW(), NOW()),
(UNHEX('0c333333333333333333333333333333'), UNHEX('a1111111111111111111111111111111'), UNHEX('11111111111111111111111111111111'), UNHEX(REPLACE('e0000003-0000-0000-0000-000000000003', '-', '')), 'KA-01-IJ-4567', 'Ganesh Naik', '+91-97654-44444', '2024-11-12 09:00:00', '2024-11-12 09:55:00', 'Indiranagar, Bengaluru', 'Amazon Embassy Tech Village', 14.0, 0.92, 'HOME_TO_OFFICE', 'COMPLETED', 300.00, 0.00, 0.00, 300.00, 0, NOW(), NOW()),
(UNHEX('0c444444444444444444444444444444'), UNHEX('a1111111111111111111111111111111'), UNHEX('11111111111111111111111111111111'), UNHEX(REPLACE('e0000003-0000-0000-0000-000000000003', '-', '')), 'KA-01-IJ-4567', 'Ganesh Naik', '+91-97654-44444', '2024-11-12 19:00:00', '2024-11-12 20:10:00', 'Amazon Embassy Tech Village', 'Indiranagar, Bengaluru', 15.8, 1.17, 'OFFICE_TO_HOME', 'COMPLETED', 300.00, 20.00, 8.50, 328.50, 0, NOW(), NOW())
ON DUPLICATE KEY UPDATE status = status;

-- ========================================
-- TRIPS FOR TEST1 USER (e0000000-test-0000-0000-000000000001) - Main Simulation User
-- Comprehensive trip history for November 2024
-- ========================================

-- Week 1 - OLA Trips (TRIP Model)
INSERT INTO trips (id, client_id, vendor_id, employee_id, vehicle_number, driver_name, driver_phone, trip_start_time, trip_end_time, pickup_location, drop_location, distance_km, duration_hours, trip_type, status, base_cost, extra_km_cost, extra_hour_cost, total_cost, billed, created_at, updated_at)
VALUES 
-- Monday Morning
(UNHEX('0d111111111111111111111111111111'), UNHEX('a1111111111111111111111111111111'), UNHEX('11111111111111111111111111111111'), UNHEX('e0000000000000000000000000000001'), 'KA-01-TEST-101', 'Kumar Swamy', '+91-99888-11111', '2024-11-04 08:00:00', '2024-11-04 08:55:00', 'Marathahalli, Bengaluru', 'Amazon Embassy Tech Village', 12.5, 0.92, 'HOME_TO_OFFICE', 'COMPLETED', 300.00, 0.00, 0.00, 300.00, 0, NOW(), NOW()),
-- Monday Evening
(UNHEX('0d222222222222222222222222222222'), UNHEX('a1111111111111111111111111111111'), UNHEX('11111111111111111111111111111111'), UNHEX('e0000000000000000000000000000001'), 'KA-01-TEST-101', 'Kumar Swamy', '+91-99888-11111', '2024-11-04 18:30:00', '2024-11-04 19:35:00', 'Amazon Embassy Tech Village', 'Marathahalli, Bengaluru', 13.8, 1.08, 'OFFICE_TO_HOME', 'COMPLETED', 300.00, 0.00, 4.00, 304.00, 0, NOW(), NOW()),
-- Tuesday Morning - Longer route
(UNHEX('0d333333333333333333333333333333'), UNHEX('a1111111111111111111111111111111'), UNHEX('11111111111111111111111111111111'), UNHEX('e0000000000000000000000000000001'), 'KA-01-TEST-102', 'Rajesh Kumar', '+91-99888-22222', '2024-11-05 08:15:00', '2024-11-05 09:20:00', 'Marathahalli, Bengaluru', 'Amazon Embassy Tech Village', 18.5, 1.08, 'HOME_TO_OFFICE', 'COMPLETED', 300.00, 87.50, 4.00, 391.50, 0, NOW(), NOW()),
-- Tuesday Evening
(UNHEX('0d444444444444444444444444444444'), UNHEX('a1111111111111111111111111111111'), UNHEX('11111111111111111111111111111111'), UNHEX('e0000000000000000000000000000001'), 'KA-01-TEST-102', 'Rajesh Kumar', '+91-99888-22222', '2024-11-05 19:00:00', '2024-11-05 20:15:00', 'Amazon Embassy Tech Village', 'Marathahalli, Bengaluru', 19.2, 1.25, 'OFFICE_TO_HOME', 'COMPLETED', 300.00, 105.00, 12.50, 417.50, 0, NOW(), NOW()),
-- Wednesday Morning
(UNHEX('0d555555555555555555555555555555'), UNHEX('a1111111111111111111111111111111'), UNHEX('11111111111111111111111111111111'), UNHEX('e0000000000000000000000000000001'), 'KA-01-TEST-103', 'Suresh Gowda', '+91-99888-33333', '2024-11-06 07:45:00', '2024-11-06 08:40:00', 'Marathahalli, Bengaluru', 'Amazon Embassy Tech Village', 13.2, 0.92, 'HOME_TO_OFFICE', 'COMPLETED', 300.00, 0.00, 0.00, 300.00, 0, NOW(), NOW())
ON DUPLICATE KEY UPDATE status = status;

-- Week 2 - UBER Trips (PACKAGE Model - Included in package)
INSERT INTO trips (id, client_id, vendor_id, employee_id, vehicle_number, driver_name, driver_phone, trip_start_time, trip_end_time, pickup_location, drop_location, distance_km, duration_hours, trip_type, status, base_cost, extra_km_cost, extra_hour_cost, total_cost, billed, created_at, updated_at)
VALUES 
-- Wednesday Evening
(UNHEX('0d666666666666666666666666666666'), UNHEX('a1111111111111111111111111111111'), UNHEX('22222222222222222222222222222222'), UNHEX('e0000000000000000000000000000001'), 'KA-02-TEST-201', 'Priya Sharma', '+91-99888-44444', '2024-11-06 18:15:00', '2024-11-06 19:05:00', 'Amazon Embassy Tech Village', 'Marathahalli, Bengaluru', 11.5, 0.83, 'OFFICE_TO_HOME', 'COMPLETED', 0.00, 0.00, 0.00, 0.00, 0, NOW(), NOW()),
-- Thursday Morning
(UNHEX('0d777777777777777777777777777777'), UNHEX('a1111111111111111111111111111111'), UNHEX('22222222222222222222222222222222'), UNHEX('e0000000000000000000000000000001'), 'KA-02-TEST-202', 'Anjali Desai', '+91-99888-55555', '2024-11-07 08:00:00', '2024-11-07 08:50:00', 'Marathahalli, Bengaluru', 'Amazon Embassy Tech Village', 12.0, 0.83, 'HOME_TO_OFFICE', 'COMPLETED', 0.00, 0.00, 0.00, 0.00, 0, NOW(), NOW()),
-- Thursday Evening
(UNHEX('0d888888888888888888888888888888'), UNHEX('a1111111111111111111111111111111'), UNHEX('22222222222222222222222222222222'), UNHEX('e0000000000000000000000000000001'), 'KA-02-TEST-202', 'Anjali Desai', '+91-99888-55555', '2024-11-07 18:45:00', '2024-11-07 19:35:00', 'Amazon Embassy Tech Village', 'Marathahalli, Bengaluru', 12.3, 0.83, 'OFFICE_TO_HOME', 'COMPLETED', 0.00, 0.00, 0.00, 0.00, 0, NOW(), NOW()),
-- Friday Morning
(UNHEX('0d999999999999999999999999999999'), UNHEX('a1111111111111111111111111111111'), UNHEX('22222222222222222222222222222222'), UNHEX('e0000000000000000000000000000001'), 'KA-02-TEST-203', 'Deepak Rao', '+91-99888-66666', '2024-11-08 08:30:00', '2024-11-08 09:20:00', 'Marathahalli, Bengaluru', 'Amazon Embassy Tech Village', 11.8, 0.83, 'HOME_TO_OFFICE', 'COMPLETED', 0.00, 0.00, 0.00, 0.00, 0, NOW(), NOW()),
-- Friday Evening
(UNHEX('0daaaaaaaaaaaaaaaaaaaaaaaaaaaaaa'), UNHEX('a1111111111111111111111111111111'), UNHEX('22222222222222222222222222222222'), UNHEX('e0000000000000000000000000000001'), 'KA-02-TEST-203', 'Deepak Rao', '+91-99888-66666', '2024-11-08 19:00:00', '2024-11-08 19:50:00', 'Amazon Embassy Tech Village', 'Marathahalli, Bengaluru', 12.5, 0.83, 'OFFICE_TO_HOME', 'COMPLETED', 0.00, 0.00, 0.00, 0.00, 0, NOW(), NOW())
ON DUPLICATE KEY UPDATE status = status;

-- Week 3 - RAPIDO Trips (HYBRID Model - Within package)
INSERT INTO trips (id, client_id, vendor_id, employee_id, vehicle_number, driver_name, driver_phone, trip_start_time, trip_end_time, pickup_location, drop_location, distance_km, duration_hours, trip_type, status, base_cost, extra_km_cost, extra_hour_cost, total_cost, billed, created_at, updated_at)
VALUES 
-- Monday Morning
(UNHEX('0dbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb'), UNHEX('a1111111111111111111111111111111'), UNHEX('33333333333333333333333333333333'), UNHEX('e0000000000000000000000000000001'), 'KA-03-TEST-301', 'Vikram Singh', '+91-99888-77777', '2024-11-11 08:00:00', '2024-11-11 08:35:00', 'Marathahalli, Bengaluru', 'Amazon Embassy Tech Village', 9.5, 0.58, 'HOME_TO_OFFICE', 'COMPLETED', 0.00, 0.00, 0.00, 0.00, 0, NOW(), NOW()),
-- Monday Evening
(UNHEX('0dcccccccccccccccccccccccccccccc'), UNHEX('a1111111111111111111111111111111'), UNHEX('33333333333333333333333333333333'), UNHEX('e0000000000000000000000000000001'), 'KA-03-TEST-301', 'Vikram Singh', '+91-99888-77777', '2024-11-11 18:15:00', '2024-11-11 18:55:00', 'Amazon Embassy Tech Village', 'Marathahalli, Bengaluru', 10.2, 0.67, 'OFFICE_TO_HOME', 'COMPLETED', 0.00, 0.00, 0.00, 0.00, 0, NOW(), NOW()),
-- Tuesday Morning - Longer route with extra charges
(UNHEX('0ddddddddddddddddddddddddddddddd'), UNHEX('a1111111111111111111111111111111'), UNHEX('33333333333333333333333333333333'), UNHEX('e0000000000000000000000000000001'), 'KA-03-TEST-302', 'Rohan Kumar', '+91-99888-88888', '2024-11-12 08:15:00', '2024-11-12 09:05:00', 'Marathahalli, Bengaluru', 'Amazon Embassy Tech Village', 14.5, 0.83, 'HOME_TO_OFFICE', 'COMPLETED', 0.00, 50.00, 0.00, 50.00, 0, NOW(), NOW()),
-- Tuesday Evening
(UNHEX('0deeeeeeeeeeeeeeeeeeeeeeeeeeeeee'), UNHEX('a1111111111111111111111111111111'), UNHEX('33333333333333333333333333333333'), UNHEX('e0000000000000000000000000000001'), 'KA-03-TEST-302', 'Rohan Kumar', '+91-99888-88888', '2024-11-12 19:00:00', '2024-11-12 19:50:00', 'Amazon Embassy Tech Village', 'Marathahalli, Bengaluru', 15.2, 0.83, 'OFFICE_TO_HOME', 'COMPLETED', 0.00, 64.00, 0.00, 64.00, 0, NOW(), NOW())
ON DUPLICATE KEY UPDATE status = status;

-- Week 4 - Mixed Vendors (Recent trips)
INSERT INTO trips (id, client_id, vendor_id, employee_id, vehicle_number, driver_name, driver_phone, trip_start_time, trip_end_time, pickup_location, drop_location, distance_km, duration_hours, trip_type, status, base_cost, extra_km_cost, extra_hour_cost, total_cost, billed, created_at, updated_at)
VALUES 
-- Wednesday - OLA
(UNHEX('0dffffffffffffffffffffffffffffff'), UNHEX('a1111111111111111111111111111111'), UNHEX('11111111111111111111111111111111'), UNHEX('e0000000000000000000000000000001'), 'KA-01-TEST-104', 'Prakash Reddy', '+91-99888-99999', '2024-11-13 08:00:00', '2024-11-13 08:50:00', 'Marathahalli, Bengaluru', 'Amazon Embassy Tech Village', 12.8, 0.83, 'HOME_TO_OFFICE', 'COMPLETED', 300.00, 0.00, 0.00, 300.00, 0, NOW(), NOW()),
-- Wednesday Evening - OLA
(UNHEX('0deeeeeeeeeeeeeeeeeeeeeeeeeeeeef'), UNHEX('a1111111111111111111111111111111'), UNHEX('11111111111111111111111111111111'), UNHEX('e0000000000000000000000000000001'), 'KA-01-TEST-104', 'Prakash Reddy', '+91-99888-99999', '2024-11-13 18:30:00', '2024-11-13 19:25:00', 'Amazon Embassy Tech Village', 'Marathahalli, Bengaluru', 14.5, 0.92, 'OFFICE_TO_HOME', 'COMPLETED', 300.00, 0.00, 0.00, 300.00, 0, NOW(), NOW()),
-- Thursday - UBER
(UNHEX('0ddddddddddddddddddddddddddddddf'), UNHEX('a1111111111111111111111111111111'), UNHEX('22222222222222222222222222222222'), UNHEX('e0000000000000000000000000000001'), 'KA-02-TEST-204', 'Lakshmi Naik', '+91-99888-10101', '2024-11-14 08:15:00', '2024-11-14 09:00:00', 'Marathahalli, Bengaluru', 'Amazon Embassy Tech Village', 11.2, 0.75, 'HOME_TO_OFFICE', 'COMPLETED', 0.00, 0.00, 0.00, 0.00, 0, NOW(), NOW()),
-- Thursday Evening - UBER
(UNHEX('0dcccccccccccccccccccccccccccccc'), UNHEX('a1111111111111111111111111111111'), UNHEX('22222222222222222222222222222222'), UNHEX('e0000000000000000000000000000001'), 'KA-02-TEST-204', 'Lakshmi Naik', '+91-99888-10101', '2024-11-14 18:45:00', '2024-11-14 19:30:00', 'Amazon Embassy Tech Village', 'Marathahalli, Bengaluru', 11.8, 0.75, 'OFFICE_TO_HOME', 'COMPLETED', 0.00, 0.00, 0.00, 0.00, 0, NOW(), NOW())
ON DUPLICATE KEY UPDATE status = status;

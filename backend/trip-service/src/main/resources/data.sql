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

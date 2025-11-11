-- Sample Vendors for Amazon India
-- Ola, Uber, and Rapido as transportation vendors

-- Insert Ola Cabs
INSERT INTO vendors (id, name, code, client_id, service_type, address, contact_email, contact_phone, contact_person, bank_account_details, tax_id, gst_number, active, created_at, updated_at)
VALUES (
    UNHEX('11111111111111111111111111111111'),
    'Ola Cabs',
    'OLA001',
    UNHEX('a1111111111111111111111111111111'),
    'Cab Service - Ride Hailing',
    'Ola Campus, Koramangala, Bengaluru, Karnataka 560095',
    'partnerships@olacabs.com',
    '+91-80-4719-8000',
    'Amit Sharma',
    'HDFC Bank, Account: 50200012345678, IFSC: HDFC0001234',
    'AABCO1234D',
    '29AABCO1234D1Z5',
    1,
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE name = name;

-- Insert Uber
INSERT INTO vendors (id, name, code, client_id, service_type, address, contact_email, contact_phone, contact_person, bank_account_details, tax_id, gst_number, active, created_at, updated_at)
VALUES (
    UNHEX('22222222222222222222222222222222'),
    'Uber India',
    'UBER001',
    UNHEX('a1111111111111111111111111111111'),
    'Cab Service - Ride Sharing',
    'Uber Office, Cyber City, Gurgaon, Haryana 122002',
    'business@uber.com',
    '+91-124-4643-000',
    'Priya Mehta',
    'ICICI Bank, Account: 001705012345, IFSC: ICIC0000017',
    'AABCU5678E',
    '29AABCU5678E1Z8',
    1,
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE name = name;

-- Insert Rapido
INSERT INTO vendors (id, name, code, client_id, service_type, address, contact_email, contact_phone, contact_person, bank_account_details, tax_id, gst_number, active, created_at, updated_at)
VALUES (
    UNHEX('33333333333333333333333333333333'),
    'Rapido Bike Taxi',
    'RAPIDO001',
    UNHEX('a1111111111111111111111111111111'),
    'Bike Taxi Service',
    'Rapido HQ, HSR Layout, Bengaluru, Karnataka 560102',
    'corporate@rapido.bike',
    '+91-80-4567-8900',
    'Rohan Kumar',
    'Axis Bank, Account: 912020012345678, IFSC: UTIB0001234',
    'AABCR9876F',
    '29AABCR9876F1Z3',
    1,
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE name = name;

-- Additional vendors for testing with other clients
-- Swift Transport for TechCorp
INSERT INTO vendors (id, name, code, client_id, service_type, address, contact_email, contact_phone, contact_person, bank_account_details, tax_id, gst_number, active, created_at, updated_at)
VALUES (
    UNHEX('44444444444444444444444444444444'),
    'Swift Transport Services',
    'SWIFT001',
    UNHEX('a2222222222222222222222222222222'),
    'Corporate Transportation',
    '45 Transport Nagar, Whitefield, Bengaluru, Karnataka 560066',
    'admin@swifttransport.in',
    '+91-80-2345-6789',
    'Suresh Reddy',
    'SBI Bank, Account: 30012345678901, IFSC: SBIN0001234',
    'AABCS1111G',
    '29AABCS1111G1Z1',
    1,
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE name = name;

-- Meru Cabs for TechCorp
INSERT INTO vendors (id, name, code, client_id, service_type, address, contact_email, contact_phone, contact_person, bank_account_details, tax_id, gst_number, active, created_at, updated_at)
VALUES (
    UNHEX('55555555555555555555555555555555'),
    'Meru Cabs',
    'MERU001',
    UNHEX('a2222222222222222222222222222222'),
    'Premium Cab Service',
    'Meru House, Andheri East, Mumbai, Maharashtra 400069',
    'corporate@merucabs.com',
    '+91-22-4242-4242',
    'Neha Singh',
    'HDFC Bank, Account: 50200098765432, IFSC: HDFC0005678',
    'AABCM2222H',
    '27AABCM2222H1Z2',
    1,
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE name = name;

-- ========================================
-- VENDOR SUBSCRIPTION PACKAGES
-- ========================================

-- ====================
-- OLA CABS PACKAGES
-- ====================

-- OLA Package 1: Premium Enterprise Package
INSERT INTO vendor_subscription_packages (id, vendor_id, package_code, package_name, description, package_type, monthly_rate, trips_included, kms_included, extra_trip_rate, extra_km_rate, extra_hour_rate, standard_trip_km, standard_trip_hours, active, created_at, updated_at)
VALUES (
    1,
    UNHEX('11111111111111111111111111111111'),
    'OLA-PKG-PREM-001',
    'Premium Enterprise Package',
    'High-volume package for large enterprises. Includes 150 trips and 2500 km per month with dedicated support.',
    'PACKAGE',
    45000.00,
    150,
    2500.00,
    350.00,
    22.00,
    55.00,
    16.00,
    1.00,
    1,
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE package_name = package_name;

-- OLA Package 2: Standard Business Package
INSERT INTO vendor_subscription_packages (id, vendor_id, package_code, package_name, description, package_type, monthly_rate, trips_included, kms_included, extra_trip_rate, extra_km_rate, extra_hour_rate, standard_trip_km, standard_trip_hours, active, created_at, updated_at)
VALUES (
    2,
    UNHEX('11111111111111111111111111111111'),
    'OLA-PKG-STD-001',
    'Standard Business Package',
    'Perfect for medium-sized companies. 120 trips and 1800 km included monthly.',
    'PACKAGE',
    30000.00,
    120,
    1800.00,
    320.00,
    20.00,
    50.00,
    15.00,
    1.00,
    1,
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE package_name = package_name;

-- OLA Package 3: Startup Package
INSERT INTO vendor_subscription_packages (id, vendor_id, package_code, package_name, description, package_type, monthly_rate, trips_included, kms_included, extra_trip_rate, extra_km_rate, extra_hour_rate, standard_trip_km, standard_trip_hours, active, created_at, updated_at)
VALUES (
    3,
    UNHEX('11111111111111111111111111111111'),
    'OLA-PKG-START-001',
    'Startup Package',
    'Budget-friendly package for startups and small businesses. 80 trips and 1200 km monthly.',
    'PACKAGE',
    18000.00,
    80,
    1200.00,
    280.00,
    18.00,
    45.00,
    15.00,
    1.00,
    1,
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE package_name = package_name;

-- OLA Hybrid 1: Flexible Executive Package
INSERT INTO vendor_subscription_packages (id, vendor_id, package_code, package_name, description, package_type, monthly_rate, trips_included, kms_included, rate_per_trip, rate_per_km, extra_km_rate, extra_hour_rate, standard_trip_km, standard_trip_hours, peak_hour_multiplier, active, created_at, updated_at)
VALUES (
    4,
    UNHEX('11111111111111111111111111111111'),
    'OLA-HYB-EXEC-001',
    'Flexible Executive Package',
    'Hybrid model with base 60 trips included + pay-per-trip for additional rides. Best for variable usage.',
    'HYBRID',
    20000.00,
    60,
    900.00,
    280.00,
    15.00,
    20.00,
    48.00,
    15.00,
    1.00,
    1.3,
    1,
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE package_name = package_name;

-- OLA Hybrid 2: Smart Corporate Package
INSERT INTO vendor_subscription_packages (id, vendor_id, package_code, package_name, description, package_type, monthly_rate, trips_included, kms_included, rate_per_trip, rate_per_km, extra_km_rate, extra_hour_rate, standard_trip_km, standard_trip_hours, peak_hour_multiplier, active, created_at, updated_at)
VALUES (
    5,
    UNHEX('11111111111111111111111111111111'),
    'OLA-HYB-CORP-001',
    'Smart Corporate Package',
    'Lower base fee with 40 trips included. Ideal for companies with seasonal variations.',
    'HYBRID',
    12000.00,
    40,
    600.00,
    260.00,
    16.00,
    22.00,
    50.00,
    15.00,
    1.00,
    1.4,
    1,
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE package_name = package_name;

-- OLA Trip 1: Pay-As-You-Go Standard
INSERT INTO vendor_subscription_packages (id, vendor_id, package_code, package_name, description, package_type, rate_per_trip, rate_per_km, extra_km_rate, extra_hour_rate, standard_trip_km, standard_trip_hours, peak_hour_multiplier, active, created_at, updated_at)
VALUES (
    6,
    UNHEX('11111111111111111111111111111111'),
    'OLA-TRIP-STD-001',
    'Pay-As-You-Go Standard',
    'No commitment required. Pay per trip basis. Ideal for occasional users.',
    'TRIP',
    300.00,
    20.00,
    25.00,
    50.00,
    15.00,
    1.00,
    1.5,
    1,
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE package_name = package_name;

-- OLA Trip 2: Pay-As-You-Go Premium
INSERT INTO vendor_subscription_packages (id, vendor_id, package_code, package_name, description, package_type, rate_per_trip, rate_per_km, extra_km_rate, extra_hour_rate, standard_trip_km, standard_trip_hours, peak_hour_multiplier, active, created_at, updated_at)
VALUES (
    7,
    UNHEX('11111111111111111111111111111111'),
    'OLA-TRIP-PREM-001',
    'Pay-As-You-Go Premium',
    'Premium per-trip service with priority booking and premium vehicles.',
    'TRIP',
    350.00,
    22.00,
    28.00,
    55.00,
    15.00,
    1.00,
    1.5,
    1,
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE package_name = package_name;

-- ====================
-- UBER INDIA PACKAGES
-- ====================

-- UBER Package 1: Enterprise Unlimited
INSERT INTO vendor_subscription_packages (id, vendor_id, package_code, package_name, description, package_type, monthly_rate, trips_included, kms_included, extra_trip_rate, extra_km_rate, extra_hour_rate, standard_trip_km, standard_trip_hours, active, created_at, updated_at)
VALUES (
    8,
    UNHEX('22222222222222222222222222222222'),
    'UBER-PKG-ENT-001',
    'Enterprise Unlimited',
    'Unlimited rides package for large enterprises with dedicated account manager.',
    'PACKAGE',
    50000.00,
    200,
    3000.00,
    400.00,
    25.00,
    60.00,
    15.00,
    1.00,
    1,
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE package_name = package_name;

-- UBER Package 2: Business Plus
INSERT INTO vendor_subscription_packages (id, vendor_id, package_code, package_name, description, package_type, monthly_rate, trips_included, kms_included, extra_trip_rate, extra_km_rate, extra_hour_rate, standard_trip_km, standard_trip_hours, active, created_at, updated_at)
VALUES (
    9,
    UNHEX('22222222222222222222222222222222'),
    'UBER-PKG-BUS-001',
    'Business Plus',
    'Comprehensive business package with 100 trips and 1500 km monthly.',
    'PACKAGE',
    25000.00,
    100,
    1500.00,
    400.00,
    22.00,
    55.00,
    15.00,
    1.00,
    1,
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE package_name = package_name;

-- UBER Trip: Uber Go - Economy
INSERT INTO vendor_subscription_packages (id, vendor_id, package_code, package_name, description, package_type, rate_per_trip, rate_per_km, extra_km_rate, extra_hour_rate, standard_trip_km, standard_trip_hours, peak_hour_multiplier, active, created_at, updated_at)
VALUES (
    10,
    UNHEX('22222222222222222222222222222222'),
    'UBER-TRIP-GO-001',
    'Uber Go - Economy',
    'Affordable rides for everyday commute. No base subscription required.',
    'TRIP',
    280.00,
    18.00,
    24.00,
    48.00,
    12.00,
    1.00,
    1.6,
    1,
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE package_name = package_name;

-- ====================
-- RAPIDO PACKAGES
-- ====================

-- RAPIDO Hybrid: Quick Commute Package
INSERT INTO vendor_subscription_packages (id, vendor_id, package_code, package_name, description, package_type, monthly_rate, trips_included, kms_included, rate_per_trip, rate_per_km, extra_km_rate, extra_hour_rate, standard_trip_km, standard_trip_hours, active, created_at, updated_at)
VALUES (
    11,
    UNHEX('33333333333333333333333333333333'),
    'RAPIDO-HYB-QCK-001',
    'Quick Commute Package',
    'Bike taxi hybrid package. Base 50 trips included with affordable per-ride charges.',
    'HYBRID',
    15000.00,
    50,
    750.00,
    250.00,
    18.00,
    20.00,
    45.00,
    12.00,
    0.75,
    1,
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE package_name = package_name;

-- RAPIDO Package: Daily Commuter
INSERT INTO vendor_subscription_packages (id, vendor_id, package_code, package_name, description, package_type, monthly_rate, trips_included, kms_included, extra_trip_rate, extra_km_rate, extra_hour_rate, standard_trip_km, standard_trip_hours, active, created_at, updated_at)
VALUES (
    12,
    UNHEX('33333333333333333333333333333333'),
    'RAPIDO-PKG-DAILY-001',
    'Daily Commuter',
    'Perfect for daily office commute. 60 rides and 600 km included.',
    'PACKAGE',
    10000.00,
    60,
    600.00,
    200.00,
    15.00,
    40.00,
    10.00,
    0.50,
    1,
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE package_name = package_name;

-- RAPIDO Trip: Bike Ride - Per Trip
INSERT INTO vendor_subscription_packages (id, vendor_id, package_code, package_name, description, package_type, rate_per_trip, rate_per_km, extra_km_rate, extra_hour_rate, standard_trip_km, standard_trip_hours, peak_hour_multiplier, active, created_at, updated_at)
VALUES (
    13,
    UNHEX('33333333333333333333333333333333'),
    'RAPIDO-TRIP-BIKE-001',
    'Bike Ride - Per Trip',
    'Quick and affordable bike rides. No subscription needed.',
    'TRIP',
    150.00,
    12.00,
    18.00,
    35.00,
    8.00,
    0.50,
    1.4,
    1,
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE package_name = package_name;

-- ====================
-- SWIFT TRANSPORT PACKAGES
-- ====================

-- SWIFT Package: Corporate Fleet
INSERT INTO vendor_subscription_packages (id, vendor_id, package_code, package_name, description, package_type, monthly_rate, trips_included, kms_included, extra_trip_rate, extra_km_rate, extra_hour_rate, standard_trip_km, standard_trip_hours, active, created_at, updated_at)
VALUES (
    14,
    UNHEX('44444444444444444444444444444444'),
    'SWIFT-PKG-FLEET-001',
    'Corporate Fleet Package',
    'Dedicated corporate transportation with professional drivers. 100 trips included.',
    'PACKAGE',
    35000.00,
    100,
    1500.00,
    380.00,
    24.00,
    58.00,
    15.00,
    1.00,
    1,
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE package_name = package_name;

-- ====================
-- MERU CABS PACKAGES
-- ====================

-- MERU Package: Premium Business
INSERT INTO vendor_subscription_packages (id, vendor_id, package_code, package_name, description, package_type, monthly_rate, trips_included, kms_included, extra_trip_rate, extra_km_rate, extra_hour_rate, standard_trip_km, standard_trip_hours, active, created_at, updated_at)
VALUES (
    15,
    UNHEX('55555555555555555555555555555555'),
    'MERU-PKG-PREM-001',
    'Premium Business Package',
    'Premium sedans with professional chauffeurs. 80 trips and 1200 km monthly.',
    'PACKAGE',
    40000.00,
    80,
    1200.00,
    450.00,
    28.00,
    65.00,
    15.00,
    1.00,
    1,
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE package_name = package_name;

-- MERU Trip: Meru Premium Ride
INSERT INTO vendor_subscription_packages (id, vendor_id, package_code, package_name, description, package_type, rate_per_trip, rate_per_km, extra_km_rate, extra_hour_rate, standard_trip_km, standard_trip_hours, peak_hour_multiplier, active, created_at, updated_at)
VALUES (
    16,
    UNHEX('55555555555555555555555555555555'),
    'MERU-TRIP-PREM-001',
    'Meru Premium Ride',
    'Premium per-ride service with luxury vehicles and experienced drivers.',
    'TRIP',
    400.00,
    25.00,
    32.00,
    60.00,
    15.00,
    1.00,
    1.4,
    1,
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE package_name = package_name;
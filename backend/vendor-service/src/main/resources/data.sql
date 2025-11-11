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

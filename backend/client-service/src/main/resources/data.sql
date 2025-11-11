-- Sample Data for Client Service
-- Scenario: Amazon as the main client with multiple vendors

-- Insert Amazon as the primary client
INSERT INTO clients (id, name, code, industry, address, contact_email, contact_phone, contact_person, active, created_at, updated_at)
VALUES (
    UNHEX(REPLACE('a1111111-1111-1111-1111-111111111111', '-', '')),
    'Amazon India',
    'AMZN001',
    'E-Commerce & Technology',
    'Embassy Tech Village, Outer Ring Road, Bengaluru, Karnataka 560103',
    'transport@amazon.in',
    '+91-80-4619-2000',
    'Rajesh Kumar',
    true,
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE name = name;

-- Insert TechCorp as another client
INSERT INTO clients (id, name, code, industry, address, contact_email, contact_phone, contact_person, active, created_at, updated_at)
VALUES (
    UNHEX(REPLACE('a2222222-2222-2222-2222-222222222222', '-', '')),
    'TechCorp Solutions',
    'TECH001',
    'IT Services',
    '123 Tech Park, Whitefield, Bengaluru, Karnataka 560066',
    'hr@techcorp.com',
    '+91-80-1234-5678',
    'Priya Sharma',
    true,
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE name = name;

-- Insert Infosys as another client
INSERT INTO clients (id, name, code, industry, address, contact_email, contact_phone, contact_person, active, created_at, updated_at)
VALUES (
    UNHEX(REPLACE('a3333333-3333-3333-3333-333333333333', '-', '')),
    'Infosys Limited',
    'INFO001',
    'IT Services & Consulting',
    'Electronics City, Hosur Road, Bengaluru, Karnataka 560100',
    'facilities@infosys.com',
    '+91-80-2852-0261',
    'Anil Verma',
    true,
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE name = name;

-- Insert Flipkart as another client
INSERT INTO clients (id, name, code, industry, address, contact_email, contact_phone, contact_person, active, created_at, updated_at)
VALUES (
    UNHEX(REPLACE('a4444444-4444-4444-4444-444444444444', '-', '')),
    'Flipkart',
    'FLIP001',
    'E-Commerce',
    'Cessna Business Park, Varthur Main Road, Bengaluru, Karnataka 560103',
    'admin@flipkart.com',
    '+91-80-6178-1000',
    'Sneha Reddy',
    true,
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE name = name;

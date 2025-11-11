-- Unified Billing Platform - Sample Users for MySQL
-- BCrypt hashed password for "password":
-- $2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG

-- Note: Using UNHEX(REPLACE(...)) to convert UUID strings to binary(16) for MySQL

-- Super Admin User (email: admin@moveinsync.com, password: password)
-- MoveInSync internal admin who manages all clients/companies
INSERT IGNORE INTO users (id, email, password, role, tenant_id, first_name, last_name, active)
VALUES (
    UNHEX(REPLACE('11111111-1111-1111-1111-111111111111', '-', '')),
    'admin@moveinsync.com',
    '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG',
    'ADMIN',
    UNHEX(REPLACE('00000000-0000-0000-0000-000000000000', '-', '')),
    'System',
    'Admin',
    TRUE
);

-- Client User for Amazon India (email: admin@amazon.in, password: password)
-- CLIENT role - manages their company's employees
INSERT IGNORE INTO users (id, email, password, role, tenant_id, first_name, last_name, active)
VALUES (
    UNHEX(REPLACE('22222222-2222-2222-2222-222222222222', '-', '')),
    'admin@amazon.in',
    '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG',
    'CLIENT',
    UNHEX(REPLACE('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', '-', '')),
    'Rahul',
    'Sharma',
    TRUE
);

-- Vendor User (email: vendor@ola.com, password: password)
INSERT IGNORE INTO users (id, email, password, role, tenant_id, vendor_id, first_name, last_name, active)
VALUES (
    UNHEX(REPLACE('33333333-3333-3333-3333-333333333333', '-', '')),
    'vendor@ola.com',
    '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG',
    'VENDOR',
    UNHEX(REPLACE('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', '-', '')),
    UNHEX(REPLACE('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', '-', '')),
    'Amit',
    'Sharma',
    TRUE
);

-- Note: Employee accounts will be created by company admins via the UI
-- No pre-populated employee credentials (they will create their own with proper password hashing)

-- Unified Billing Platform - Sample Users
-- BCrypt hashed password for "password": $2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG

-- Super Admin (MoveInSync)
INSERT INTO users (id, email, password, role, tenant_id, first_name, last_name, active)
VALUES (
    UNHEX(REPLACE('11111111-1111-1111-1111-111111111111', '-', '')),
    'admin@moveinsync.com',
    '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG',
    'ADMIN',
    UNHEX(REPLACE('00000000-0000-0000-0000-000000000000', '-', '')),
    'System',
    'Admin',
    TRUE
)
ON DUPLICATE KEY UPDATE email = email;

-- Client Admin (Amazon India)
INSERT INTO users (id, email, password, role, tenant_id, first_name, last_name, active)
VALUES (
    UNHEX(REPLACE('22222222-2222-2222-2222-222222222222', '-', '')),
    'admin@amazon.in',
    '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG',
    'CLIENT',
    UNHEX(REPLACE('a1111111-1111-1111-1111-111111111111', '-', '')),
    'Rahul',
    'Sharma',
    TRUE
)
ON DUPLICATE KEY UPDATE email = email;

-- Vendor (Ola Cabs)
INSERT INTO users (id, email, password, role, tenant_id, vendor_id, first_name, last_name, active)
VALUES (
    UNHEX(REPLACE('33333333-3333-3333-3333-333333333333', '-', '')),
    'vendor@ola.com',
    '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG',
    'VENDOR',
    UNHEX(REPLACE('11111111-1111-1111-1111-111111111111', '-', '')),
    UNHEX(REPLACE('11111111-1111-1111-1111-111111111111', '-', '')),
    'Amit',
    'Sharma',
    TRUE
)
ON DUPLICATE KEY UPDATE email = email;

-- Sample Employees for Amazon India
-- Employee 1
INSERT INTO users (id, email, password, role, tenant_id, first_name, last_name, phone, employee_id, department, active)
VALUES (
    UNHEX(REPLACE('e0000001-0000-0000-0000-000000000001', '-', '')),
    'priya.patel@amazon.in',
    '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG',
    'EMPLOYEE',
    UNHEX(REPLACE('a1111111-1111-1111-1111-111111111111', '-', '')),
    'Priya',
    'Patel',
    '+91-98765-43210',
    'AMZN-EMP-001',
    'Information Technology',
    TRUE
)
ON DUPLICATE KEY UPDATE email = email;

-- Employee 2
INSERT INTO users (id, email, password, role, tenant_id, first_name, last_name, phone, employee_id, department, active)
VALUES (
    UNHEX(REPLACE('e0000002-0000-0000-0000-000000000002', '-', '')),
    'rahul.verma@amazon.in',
    '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG',
    'EMPLOYEE',
    UNHEX(REPLACE('a1111111-1111-1111-1111-111111111111', '-', '')),
    'Rahul',
    'Verma',
    '+91-98765-43211',
    'AMZN-EMP-002',
    'Operations',
    TRUE
)
ON DUPLICATE KEY UPDATE email = email;

-- Employee 3
INSERT INTO users (id, email, password, role, tenant_id, first_name, last_name, phone, employee_id, department, active)
VALUES (
    UNHEX(REPLACE('e0000003-0000-0000-0000-000000000003', '-', '')),
    'anita.kumar@amazon.in',
    '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG',
    'EMPLOYEE',
    UNHEX(REPLACE('a1111111-1111-1111-1111-111111111111', '-', '')),
    'Anita',
    'Kumar',
    '+91-98765-43212',
    'AMZN-EMP-003',
    'Human Resources',
    TRUE
)
ON DUPLICATE KEY UPDATE email = email;

-- Test Employee (Main simulation user)
INSERT INTO users (id, email, password, role, tenant_id, first_name, last_name, phone, employee_id, department, active)
VALUES (
    UNHEX('e0000000000000000000000000000001'),
    'test1@gmail.com',
    '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG',
    'EMPLOYEE',
    UNHEX('a1111111111111111111111111111111'),
    'Test',
    'User',
    '+91-99999-99999',
    'AMZN-TEST-001',
    'IT',
    TRUE
)
ON DUPLICATE KEY UPDATE email = email;

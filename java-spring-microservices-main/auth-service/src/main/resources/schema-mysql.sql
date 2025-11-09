-- Auth Service MySQL Schema
-- Database: unified_billing_auth

CREATE DATABASE IF NOT EXISTS unified_billing_auth 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

USE unified_billing_auth;

-- Drop existing tables if they exist (for clean setup)
DROP TABLE IF EXISTS users;

-- Create users table
CREATE TABLE users (
    id CHAR(36) PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    tenant_id CHAR(36) NOT NULL,
    vendor_id CHAR(36),
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    -- Indexes for performance
    INDEX idx_tenant (tenant_id),
    INDEX idx_email (email),
    INDEX idx_role (role),
    INDEX idx_active (active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insert sample users (password for all: password123)
-- Password hash: $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy

INSERT INTO users (id, email, password, role, tenant_id, vendor_id, first_name, last_name, active, created_at, updated_at)
VALUES 
-- System Admin (MoveInSync Internal)
('11111111-1111-1111-1111-111111111111', 
 'admin@moveinsync.com', 
 '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 
 'ADMIN', 
 '00000000-0000-0000-0000-000000000000', 
 NULL, 
 'System', 
 'Admin', 
 true, 
 NOW(), 
 NOW()),

-- Client Manager for TechCorp
('22222222-2222-2222-2222-222222222222', 
 'manager@techcorp.com', 
 '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 
 'CLIENT_MANAGER', 
 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 
 NULL, 
 'Alice', 
 'Manager', 
 true, 
 NOW(), 
 NOW()),

-- Vendor User for Swift Transport
('33333333-3333-3333-3333-333333333333', 
 'vendor@swifttransport.com', 
 '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 
 'VENDOR', 
 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 
 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 
 'John', 
 'Vendor', 
 true, 
 NOW(), 
 NOW()),

-- Employee for TechCorp
('44444444-4444-4444-4444-444444444444', 
 'employee@techcorp.com', 
 '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 
 'EMPLOYEE', 
 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 
 NULL, 
 'Bob', 
 'Employee', 
 true, 
 NOW(), 
 NOW()),

-- Finance Team Member
('55555555-5555-5555-5555-555555555555', 
 'finance@moveinsync.com', 
 '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 
 'FINANCE_TEAM', 
 '00000000-0000-0000-0000-000000000000', 
 NULL, 
 'Sarah', 
 'Finance', 
 true, 
 NOW(), 
 NOW());

-- Verify data
SELECT 
    id, 
    email, 
    role, 
    tenant_id, 
    CONCAT(first_name, ' ', last_name) AS full_name,
    active,
    created_at
FROM users
ORDER BY role, email;




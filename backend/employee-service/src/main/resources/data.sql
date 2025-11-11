-- Sample Employees for MoveInSync Unified Billing Platform
-- These are the actual employees using the transportation services

-- Amazon India Employees (Client: a1111111-1111-1111-1111-111111111111)

-- Employee 1: Priya Patel - IT Department
INSERT IGNORE INTO employees (id, client_id, employee_code, first_name, last_name, email, phone_number, department, designation, home_address, date_of_joining, date_of_birth, active, total_incentives_earned, total_trips_completed, total_distance_traveled, created_at, updated_at)
VALUES (
    UNHEX('44444444444444444444444444444444'),
    UNHEX('a1111111111111111111111111111111'),
    'AMZN-EMP-001',
    'Priya',
    'Patel',
    'priya.patel@amazon.in',
    '+91-98765-43210',
    'Information Technology',
    'Senior Software Engineer',
    'Whitefield, Bengaluru, Karnataka 560066',
    '2022-03-15',
    '1995-06-20',
    TRUE,
    1500.00,
    25,
    350.50,
    NOW(),
    NOW()
);

-- Employee 2: Rahul Sharma - Operations
INSERT IGNORE INTO employees (id, client_id, employee_code, first_name, last_name, email, phone_number, department, designation, home_address, date_of_joining, date_of_birth, active, total_incentives_earned, total_trips_completed, total_distance_traveled, created_at, updated_at)
VALUES (
    UNHEX('55555555555555555555555555555555'),
    UNHEX('a1111111111111111111111111111111'),
    'AMZN-EMP-002',
    'Rahul',
    'Sharma',
    'rahul.sharma@amazon.in',
    '+91-98765-43211',
    'Operations',
    'Operations Manager',
    'Koramangala, Bengaluru, Karnataka 560095',
    '2021-08-10',
    '1990-11-15',
    TRUE,
    2200.00,
    40,
    520.75,
    NOW(),
    NOW()
);

-- Employee 3: Anita Kumar - HR
INSERT IGNORE INTO employees (id, client_id, employee_code, first_name, last_name, email, phone_number, department, designation, home_address, date_of_joining, date_of_birth, active, total_incentives_earned, total_trips_completed, total_distance_traveled, created_at, updated_at)
VALUES (
    UNHEX('66666666666666666666666666666666'),
    UNHEX('a1111111111111111111111111111111'),
    'AMZN-EMP-003',
    'Anita',
    'Kumar',
    'anita.kumar@amazon.in',
    '+91-98765-43212',
    'Human Resources',
    'HR Manager',
    'Indiranagar, Bengaluru, Karnataka 560038',
    '2020-01-20',
    '1988-03-25',
    TRUE,
    1800.00,
    32,
    410.25,
    NOW(),
    NOW()
);

-- TechCorp Solutions Employees (Client: a2222222-2222-2222-2222-222222222222)

-- Employee 4: Vikram Reddy - Engineering
INSERT IGNORE INTO employees (id, client_id, employee_code, first_name, last_name, email, phone_number, department, designation, home_address, date_of_joining, date_of_birth, active, total_incentives_earned, total_trips_completed, total_distance_traveled, created_at, updated_at)
VALUES (
    UNHEX('77777777777777777777777777777777'),
    UNHEX('a2222222222222222222222222222222'),
    'TECH-EMP-001',
    'Vikram',
    'Reddy',
    'vikram.reddy@techcorp.com',
    '+91-98765-43213',
    'Engineering',
    'Lead Developer',
    'Whitefield, Bengaluru, Karnataka 560066',
    '2022-05-01',
    '1992-09-10',
    TRUE,
    950.00,
    18,
    245.50,
    NOW(),
    NOW()
);

-- Employee 5: Sneha Iyer - Product Management
INSERT IGNORE INTO employees (id, client_id, employee_code, first_name, last_name, email, phone_number, department, designation, home_address, date_of_joining, date_of_birth, active, total_incentives_earned, total_trips_completed, total_distance_traveled, created_at, updated_at)
VALUES (
    UNHEX('88888888888888888888888888888888'),
    UNHEX('a2222222222222222222222222222222'),
    'TECH-EMP-002',
    'Sneha',
    'Iyer',
    'sneha.iyer@techcorp.com',
    '+91-98765-43214',
    'Product Management',
    'Product Manager',
    'HSR Layout, Bengaluru, Karnataka 560102',
    '2021-11-15',
    '1991-07-18',
    TRUE,
    1200.00,
    22,
    305.75,
    NOW(),
    NOW()
);

-- Infosys Limited Employees (Client: a3333333-3333-3333-3333-333333333333)

-- Employee 6: Arjun Mehta - Consulting
INSERT IGNORE INTO employees (id, client_id, employee_code, first_name, last_name, email, phone_number, department, designation, home_address, date_of_joining, date_of_birth, active, total_incentives_earned, total_trips_completed, total_distance_traveled, created_at, updated_at)
VALUES (
    UNHEX('99999999999999999999999999999999'),
    UNHEX('a3333333333333333333333333333333'),
    'INFO-EMP-001',
    'Arjun',
    'Mehta',
    'arjun.mehta@infosys.com',
    '+91-98765-43215',
    'Consulting',
    'Senior Consultant',
    'Electronics City, Bengaluru, Karnataka 560100',
    '2020-07-01',
    '1989-12-05',
    TRUE,
    2500.00,
    45,
    600.00,
    NOW(),
    NOW()
);

-- Employee 7: Divya Nair - Finance
INSERT IGNORE INTO employees (id, client_id, employee_code, first_name, last_name, email, phone_number, department, designation, home_address, date_of_joining, date_of_birth, active, total_incentives_earned, total_trips_completed, total_distance_traveled, created_at, updated_at)
VALUES (
    UNHEX('aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa'),
    UNHEX('a3333333333333333333333333333333'),
    'INFO-EMP-002',
    'Divya',
    'Nair',
    'divya.nair@infosys.com',
    '+91-98765-43216',
    'Finance',
    'Financial Analyst',
    'Marathahalli, Bengaluru, Karnataka 560037',
    '2021-03-10',
    '1993-04-22',
    TRUE,
    1350.00,
    28,
    380.25,
    NOW(),
    NOW()
);

-- Flipkart Employees (Client: a4444444-4444-4444-4444-444444444444)

-- Employee 8: Karthik Krishnan - Product
INSERT IGNORE INTO employees (id, client_id, employee_code, first_name, last_name, email, phone_number, department, designation, home_address, date_of_joining, date_of_birth, active, total_incentives_earned, total_trips_completed, total_distance_traveled, created_at, updated_at)
VALUES (
    UNHEX('bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb'),
    UNHEX('a4444444444444444444444444444444'),
    'FLIP-EMP-001',
    'Karthik',
    'Krishnan',
    'karthik.krishnan@flipkart.com',
    '+91-98765-43217',
    'Product',
    'Product Lead',
    'Bellandur, Bengaluru, Karnataka 560103',
    '2022-02-01',
    '1990-08-30',
    TRUE,
    1750.00,
    30,
    425.50,
    NOW(),
    NOW()
);


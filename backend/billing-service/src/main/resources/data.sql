-- Unified Billing Platform - Sample Invoices

-- Invoice for Ola (TRIP Model) - November 2024
INSERT INTO invoices (id, invoice_number, client_id, vendor_id, billing_period_start, billing_period_end, 
                      base_amount, extra_charges, total_amount, tax_amount, final_amount, 
                      status, due_date, total_trips, total_km, total_hours, notes, created_at, updated_at)
VALUES (
    UNHEX('10000000000000000000000000000001'),
    'INV-OLA-2024-11-001',
    UNHEX('a1111111111111111111111111111111'),  -- Amazon
    UNHEX('11111111111111111111111111111111'),  -- Ola
    '2024-11-01',
    '2024-11-30',
    1500.00,  -- Base (5 trips × ₹300)
    446.00,   -- Extra charges (extra km + hours)
    1946.00,  -- Total
    350.28,   -- 18% GST
    2296.28,  -- Final with tax
    'PENDING',
    '2024-12-15',
    5,
    88.2,
    5.17,
    'TRIP billing model: ₹300 base + ₹20/km. Extra charges for distances beyond 15km and hours beyond 1hr.',
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE status = status;

-- Invoice for Uber (PACKAGE Model) - November 2024
INSERT INTO invoices (id, invoice_number, client_id, vendor_id, billing_period_start, billing_period_end, 
                      base_amount, extra_charges, total_amount, tax_amount, final_amount, 
                      status, due_date, total_trips, total_km, total_hours, notes, created_at, updated_at)
VALUES (
    UNHEX('20000000000000000000000000000002'),
    'INV-UBER-2024-11-001',
    UNHEX('a1111111111111111111111111111111'),  -- Amazon
    UNHEX('22222222222222222222222222222222'),  -- Uber
    '2024-11-01',
    '2024-11-30',
    25000.00,  -- Monthly package
    0.00,      -- No extra charges (within limits)
    25000.00,  -- Total
    4500.00,   -- 18% GST
    29500.00,  -- Final with tax
    'PAID',
    '2024-12-15',
    2,
    25.4,
    1.58,
    'PACKAGE billing model: ₹25,000/month for 100 trips and 1500km. Current usage: 2 trips, 25.4km (well within limits).',
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE status = status;

-- Invoice for Rapido (HYBRID Model) - November 2024
INSERT INTO invoices (id, invoice_number, client_id, vendor_id, billing_period_start, billing_period_end, 
                      base_amount, extra_charges, total_amount, tax_amount, final_amount, 
                      status, due_date, total_trips, total_km, total_hours, notes, created_at, updated_at)
VALUES (
    UNHEX('30000000000000000000000000000003'),
    'INV-RAPIDO-2024-11-001',
    UNHEX('a1111111111111111111111111111111'),  -- Amazon
    UNHEX('33333333333333333333333333333333'),  -- Rapido
    '2024-11-01',
    '2024-11-30',
    15000.00,  -- Monthly base
    76.00,     -- Extra km charges
    15076.00,  -- Total
    2713.68,   -- 18% GST
    17789.68,  -- Final with tax
    'PENDING',
    '2024-12-15',
    2,
    24.3,
    1.50,
    'HYBRID billing model: ₹15,000 base + 50 trips/750km included. Extra: 1 trip with 3.8km beyond standard (₹76).',
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE status = status;

-- Invoice for Ola (Previous month - October 2024) - PAID
INSERT INTO invoices (id, invoice_number, client_id, vendor_id, billing_period_start, billing_period_end, 
                      base_amount, extra_charges, total_amount, tax_amount, final_amount, 
                      status, due_date, paid_date, total_trips, total_km, total_hours, notes, created_at, updated_at)
VALUES (
    UNHEX('10000000000000000000000000000002'),
    'INV-OLA-2024-10-001',
    UNHEX('a1111111111111111111111111111111'),
    UNHEX('11111111111111111111111111111111'),
    '2024-10-01',
    '2024-10-31',
    2100.00,
    523.50,
    2623.50,
    472.23,
    3095.73,
    'PAID',
    '2024-11-15',
    '2024-11-12',
    7,
    115.8,
    6.83,
    'TRIP billing model - October 2024. Payment received on time.',
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE status = status;


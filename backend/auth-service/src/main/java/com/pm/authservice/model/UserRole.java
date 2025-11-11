package com.pm.authservice.model;

public enum UserRole {
    ADMIN,              // MoveInSync super admin - manages all clients/companies
    CLIENT,             // Client/Company admin - manages their own employees
    VENDOR,             // Vendor user - view vendor-specific data
    EMPLOYEE,           // Employee - view personal trips and incentives (added by their company)
    FINANCE_TEAM        // Internal finance team - generate reports
}


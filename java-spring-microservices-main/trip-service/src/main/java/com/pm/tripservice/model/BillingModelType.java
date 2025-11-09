package com.pm.tripservice.model;

public enum BillingModelType {
    TRIP,       // Billing per trip + per km
    PACKAGE,    // Fixed monthly package with included trips/kms
    HYBRID      // Combination of package + per-trip overage
}


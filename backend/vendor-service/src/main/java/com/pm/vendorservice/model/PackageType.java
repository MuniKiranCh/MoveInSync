package com.pm.vendorservice.model;

public enum PackageType {
    TRIP,       // Pay-per-trip billing model
    PACKAGE,    // Fixed monthly package with included trips/kms
    HYBRID      // Combination of base package + per-trip charges
}


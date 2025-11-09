package com.pm.billingservice.model;

public enum InvoiceStatus {
    DRAFT,          // Invoice created but not finalized
    PENDING,        // Invoice sent, awaiting payment
    PAID,           // Payment received
    OVERDUE,        // Payment past due date
    CANCELLED,      // Invoice cancelled
    DISPUTED        // Payment dispute
}


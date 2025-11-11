package com.pm.tripservice.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "employee_subscriptions", indexes = {
    @Index(name = "idx_subscription_employee", columnList = "employeeId"),
    @Index(name = "idx_subscription_client_vendor", columnList = "clientId,vendorId"),
    @Index(name = "idx_subscription_active", columnList = "active")
})
public class EmployeeSubscription {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private UUID employeeId;

    @Column(nullable = false)
    private UUID clientId;  // The company that's paying

    @Column(nullable = false)
    private UUID vendorId;

    @Column(nullable = false)
    private UUID billingModelId;  // Reference to the billing model/package

    @Column(nullable = false)
    private LocalDate subscriptionStartDate;

    @Column
    private LocalDate subscriptionEndDate;

    @Column(nullable = false)
    private Boolean active = true;

    // Usage tracking for the current billing period
    @Column
    private Integer tripsUsed = 0;

    @Column
    private Integer tripsLimit;  // From billing model

    @Column(precision = 10, scale = 2)
    private java.math.BigDecimal kmUsed = java.math.BigDecimal.ZERO;

    @Column(precision = 10, scale = 2)
    private java.math.BigDecimal kmLimit;  // From billing model

    @Column
    private LocalDate currentBillingPeriodStart;

    @Column
    private LocalDate currentBillingPeriodEnd;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column
    private Instant createdAt;

    @Column
    private Instant updatedAt;

    @Column
    private UUID assignedBy;  // Client admin who assigned this

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
        if (active == null) {
            active = true;
        }
        if (tripsUsed == null) {
            tripsUsed = 0;
        }
        if (kmUsed == null) {
            kmUsed = java.math.BigDecimal.ZERO;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }

    // Business methods
    public boolean isWithinLimits() {
        boolean withinTrips = tripsLimit == null || tripsUsed < tripsLimit;
        boolean withinKm = kmLimit == null || kmUsed.compareTo(kmLimit) < 0;
        return withinTrips && withinKm;
    }

    public int getTripsRemaining() {
        if (tripsLimit == null) return -1; // Unlimited
        return Math.max(0, tripsLimit - tripsUsed);
    }

    public java.math.BigDecimal getKmRemaining() {
        if (kmLimit == null) return java.math.BigDecimal.valueOf(-1); // Unlimited
        return kmLimit.subtract(kmUsed).max(java.math.BigDecimal.ZERO);
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(UUID employeeId) {
        this.employeeId = employeeId;
    }

    public UUID getClientId() {
        return clientId;
    }

    public void setClientId(UUID clientId) {
        this.clientId = clientId;
    }

    public UUID getVendorId() {
        return vendorId;
    }

    public void setVendorId(UUID vendorId) {
        this.vendorId = vendorId;
    }

    public UUID getBillingModelId() {
        return billingModelId;
    }

    public void setBillingModelId(UUID billingModelId) {
        this.billingModelId = billingModelId;
    }

    public LocalDate getSubscriptionStartDate() {
        return subscriptionStartDate;
    }

    public void setSubscriptionStartDate(LocalDate subscriptionStartDate) {
        this.subscriptionStartDate = subscriptionStartDate;
    }

    public LocalDate getSubscriptionEndDate() {
        return subscriptionEndDate;
    }

    public void setSubscriptionEndDate(LocalDate subscriptionEndDate) {
        this.subscriptionEndDate = subscriptionEndDate;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Integer getTripsUsed() {
        return tripsUsed;
    }

    public void setTripsUsed(Integer tripsUsed) {
        this.tripsUsed = tripsUsed;
    }

    public Integer getTripsLimit() {
        return tripsLimit;
    }

    public void setTripsLimit(Integer tripsLimit) {
        this.tripsLimit = tripsLimit;
    }

    public java.math.BigDecimal getKmUsed() {
        return kmUsed;
    }

    public void setKmUsed(java.math.BigDecimal kmUsed) {
        this.kmUsed = kmUsed;
    }

    public java.math.BigDecimal getKmLimit() {
        return kmLimit;
    }

    public void setKmLimit(java.math.BigDecimal kmLimit) {
        this.kmLimit = kmLimit;
    }

    public LocalDate getCurrentBillingPeriodStart() {
        return currentBillingPeriodStart;
    }

    public void setCurrentBillingPeriodStart(LocalDate currentBillingPeriodStart) {
        this.currentBillingPeriodStart = currentBillingPeriodStart;
    }

    public LocalDate getCurrentBillingPeriodEnd() {
        return currentBillingPeriodEnd;
    }

    public void setCurrentBillingPeriodEnd(LocalDate currentBillingPeriodEnd) {
        this.currentBillingPeriodEnd = currentBillingPeriodEnd;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public UUID getAssignedBy() {
        return assignedBy;
    }

    public void setAssignedBy(UUID assignedBy) {
        this.assignedBy = assignedBy;
    }
}


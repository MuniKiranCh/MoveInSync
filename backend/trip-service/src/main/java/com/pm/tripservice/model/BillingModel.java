package com.pm.tripservice.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "billing_models", indexes = {
    @Index(name = "idx_billing_client_vendor", columnList = "clientId,vendorId", unique = true)
})
public class BillingModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private UUID clientId;

    @Column(nullable = false)
    private UUID vendorId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BillingModelType modelType;

    // Trip-based pricing
    @Column(precision = 10, scale = 2)
    private BigDecimal ratePerTrip;

    @Column(precision = 10, scale = 2)
    private BigDecimal ratePerKm;

    // Package-based pricing
    @Column(precision = 10, scale = 2)
    private BigDecimal packageMonthlyRate;

    @Column
    private Integer packageTripsIncluded;

    @Column(precision = 10, scale = 2)
    private BigDecimal packageKmsIncluded;

    // Extra charges
    @Column(precision = 10, scale = 2)
    private BigDecimal extraTripRate;

    @Column(precision = 10, scale = 2)
    private BigDecimal extraKmRate;

    @Column(precision = 10, scale = 2)
    private BigDecimal extraHourRate;

    @Column(precision = 10, scale = 2)
    private BigDecimal peakHourMultiplier;

    // Standard limits
    @Column(precision = 10, scale = 2)
    private BigDecimal standardTripKm;  // Standard km per trip

    @Column(precision = 10, scale = 2)
    private BigDecimal standardTripHours;  // Standard hours per trip

    @Column
    private Boolean active = true;

    @Column
    private Instant createdAt;

    @Column
    private Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public BillingModelType getModelType() {
        return modelType;
    }

    public void setModelType(BillingModelType modelType) {
        this.modelType = modelType;
    }

    public BigDecimal getRatePerTrip() {
        return ratePerTrip;
    }

    public void setRatePerTrip(BigDecimal ratePerTrip) {
        this.ratePerTrip = ratePerTrip;
    }

    public BigDecimal getRatePerKm() {
        return ratePerKm;
    }

    public void setRatePerKm(BigDecimal ratePerKm) {
        this.ratePerKm = ratePerKm;
    }

    public BigDecimal getPackageMonthlyRate() {
        return packageMonthlyRate;
    }

    public void setPackageMonthlyRate(BigDecimal packageMonthlyRate) {
        this.packageMonthlyRate = packageMonthlyRate;
    }

    public Integer getPackageTripsIncluded() {
        return packageTripsIncluded;
    }

    public void setPackageTripsIncluded(Integer packageTripsIncluded) {
        this.packageTripsIncluded = packageTripsIncluded;
    }

    public BigDecimal getPackageKmsIncluded() {
        return packageKmsIncluded;
    }

    public void setPackageKmsIncluded(BigDecimal packageKmsIncluded) {
        this.packageKmsIncluded = packageKmsIncluded;
    }

    public BigDecimal getExtraTripRate() {
        return extraTripRate;
    }

    public void setExtraTripRate(BigDecimal extraTripRate) {
        this.extraTripRate = extraTripRate;
    }

    public BigDecimal getExtraKmRate() {
        return extraKmRate;
    }

    public void setExtraKmRate(BigDecimal extraKmRate) {
        this.extraKmRate = extraKmRate;
    }

    public BigDecimal getExtraHourRate() {
        return extraHourRate;
    }

    public void setExtraHourRate(BigDecimal extraHourRate) {
        this.extraHourRate = extraHourRate;
    }

    public BigDecimal getPeakHourMultiplier() {
        return peakHourMultiplier;
    }

    public void setPeakHourMultiplier(BigDecimal peakHourMultiplier) {
        this.peakHourMultiplier = peakHourMultiplier;
    }

    public BigDecimal getStandardTripKm() {
        return standardTripKm;
    }

    public void setStandardTripKm(BigDecimal standardTripKm) {
        this.standardTripKm = standardTripKm;
    }

    public BigDecimal getStandardTripHours() {
        return standardTripHours;
    }

    public void setStandardTripHours(BigDecimal standardTripHours) {
        this.standardTripHours = standardTripHours;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}


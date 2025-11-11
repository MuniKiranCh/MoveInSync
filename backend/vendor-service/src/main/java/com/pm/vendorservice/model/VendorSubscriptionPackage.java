package com.pm.vendorservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "vendor_subscription_packages", indexes = {
    @Index(name = "idx_package_vendor_id", columnList = "vendor_id"),
    @Index(name = "idx_package_code", columnList = "package_code")
})
public class VendorSubscriptionPackage {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "vendor_id", nullable = false)
    private UUID vendorId;

    @NotNull
    @Column(name = "package_code", nullable = false, unique = true)
    private String packageCode;  // e.g., "OLA-PREMIUM-001"

    @NotNull
    @Column(nullable = false)
    private String packageName;  // e.g., "Premium Enterprise Package"

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PackageType packageType;

    // Package Model Fields
    @Column(precision = 10, scale = 2)
    private BigDecimal monthlyRate;

    @Column
    private Integer tripsIncluded;

    @Column(precision = 10, scale = 2)
    private BigDecimal kmsIncluded;

    // Trip Model Fields
    @Column(precision = 10, scale = 2)
    private BigDecimal ratePerTrip;

    @Column(precision = 10, scale = 2)
    private BigDecimal ratePerKm;

    // Extra charges (applicable to all types)
    @Column(precision = 10, scale = 2)
    private BigDecimal extraTripRate;

    @Column(precision = 10, scale = 2)
    private BigDecimal extraKmRate;

    @Column(precision = 10, scale = 2)
    private BigDecimal extraHourRate;

    // Standard limits
    @Column(precision = 10, scale = 2)
    private BigDecimal standardTripKm;  // Standard km per trip

    @Column(precision = 10, scale = 2)
    private BigDecimal standardTripHours;  // Standard hours per trip

    @Column(precision = 10, scale = 2)
    private BigDecimal peakHourMultiplier;

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
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getVendorId() {
        return vendorId;
    }

    public void setVendorId(UUID vendorId) {
        this.vendorId = vendorId;
    }

    public String getPackageCode() {
        return packageCode;
    }

    public void setPackageCode(String packageCode) {
        this.packageCode = packageCode;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PackageType getPackageType() {
        return packageType;
    }

    public void setPackageType(PackageType packageType) {
        this.packageType = packageType;
    }

    public BigDecimal getMonthlyRate() {
        return monthlyRate;
    }

    public void setMonthlyRate(BigDecimal monthlyRate) {
        this.monthlyRate = monthlyRate;
    }

    public Integer getTripsIncluded() {
        return tripsIncluded;
    }

    public void setTripsIncluded(Integer tripsIncluded) {
        this.tripsIncluded = tripsIncluded;
    }

    public BigDecimal getKmsIncluded() {
        return kmsIncluded;
    }

    public void setKmsIncluded(BigDecimal kmsIncluded) {
        this.kmsIncluded = kmsIncluded;
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

    public BigDecimal getPeakHourMultiplier() {
        return peakHourMultiplier;
    }

    public void setPeakHourMultiplier(BigDecimal peakHourMultiplier) {
        this.peakHourMultiplier = peakHourMultiplier;
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


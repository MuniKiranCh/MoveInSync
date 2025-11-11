package com.pm.vendorservice.dto;

import com.pm.vendorservice.model.PackageType;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class SubscriptionPackageResponseDTO {
    
    private Long id;
    private UUID vendorId;
    private String vendorName;
    private String packageCode;
    private String packageName;
    private String description;
    private PackageType packageType;
    private BigDecimal monthlyRate;
    private Integer tripsIncluded;
    private BigDecimal kmsIncluded;
    private BigDecimal ratePerTrip;
    private BigDecimal ratePerKm;
    private BigDecimal extraTripRate;
    private BigDecimal extraKmRate;
    private BigDecimal extraHourRate;
    private BigDecimal standardTripKm;
    private BigDecimal standardTripHours;
    private BigDecimal peakHourMultiplier;
    private Boolean active;
    private Instant createdAt;
    private Instant updatedAt;

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

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
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

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}


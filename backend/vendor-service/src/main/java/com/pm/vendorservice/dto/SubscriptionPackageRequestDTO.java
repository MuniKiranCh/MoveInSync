package com.pm.vendorservice.dto;

import com.pm.vendorservice.model.PackageType;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

public class SubscriptionPackageRequestDTO {
    
    @NotNull
    private UUID vendorId;
    
    @NotNull
    private String packageCode;
    
    @NotNull
    private String packageName;
    
    private String description;
    
    @NotNull
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

    // Getters and Setters
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
}


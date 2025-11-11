package com.pm.billingservice.dto;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;
import java.util.UUID;

public class BillingCalculationResponse {
    private UUID clientId;
    private UUID vendorId;
    private YearMonth billingMonth;
    private String billingModelType; // TRIP, PACKAGE, HYBRID
    
    // Base charges
    private BigDecimal baseCost;
    private BigDecimal packageMonthlyRate; // For PACKAGE/HYBRID
    
    // Trip summary
    private Integer totalTrips;
    private Integer includedTrips; // For PACKAGE/HYBRID
    private Integer extraTrips;
    
    // Distance summary
    private BigDecimal totalDistanceKm;
    private BigDecimal includedKm; // For PACKAGE/HYBRID
    private BigDecimal extraKm;
    
    // Time summary
    private BigDecimal totalHours;
    private BigDecimal extraHours;
    
    // Cost breakdown
    private BigDecimal tripCharges; // For TRIP model
    private BigDecimal distanceCharges; // For TRIP model
    private BigDecimal extraTripCharges; // For PACKAGE/HYBRID
    private BigDecimal extraKmCharges;
    private BigDecimal extraHourCharges;
    private BigDecimal peakHourCharges;
    
    // Totals
    private BigDecimal totalCost;
    private BigDecimal taxAmount;
    private BigDecimal grandTotal;
    
    // Trip details
    private List<TripSummary> trips;
    
    // Calculation details
    private String calculationNotes;
    
    // Nested class for trip summary
    public static class TripSummary {
        private UUID tripId;
        private String tripDate;
        private BigDecimal distanceKm;
        private BigDecimal durationHours;
        private BigDecimal cost;
        
        // Getters and Setters
        public UUID getTripId() {
            return tripId;
        }
        
        public void setTripId(UUID tripId) {
            this.tripId = tripId;
        }
        
        public String getTripDate() {
            return tripDate;
        }
        
        public void setTripDate(String tripDate) {
            this.tripDate = tripDate;
        }
        
        public BigDecimal getDistanceKm() {
            return distanceKm;
        }
        
        public void setDistanceKm(BigDecimal distanceKm) {
            this.distanceKm = distanceKm;
        }
        
        public BigDecimal getDurationHours() {
            return durationHours;
        }
        
        public void setDurationHours(BigDecimal durationHours) {
            this.durationHours = durationHours;
        }
        
        public BigDecimal getCost() {
            return cost;
        }
        
        public void setCost(BigDecimal cost) {
            this.cost = cost;
        }
    }
    
    // Getters and Setters
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
    
    public YearMonth getBillingMonth() {
        return billingMonth;
    }
    
    public void setBillingMonth(YearMonth billingMonth) {
        this.billingMonth = billingMonth;
    }
    
    public String getBillingModelType() {
        return billingModelType;
    }
    
    public void setBillingModelType(String billingModelType) {
        this.billingModelType = billingModelType;
    }
    
    public BigDecimal getBaseCost() {
        return baseCost;
    }
    
    public void setBaseCost(BigDecimal baseCost) {
        this.baseCost = baseCost;
    }
    
    public BigDecimal getPackageMonthlyRate() {
        return packageMonthlyRate;
    }
    
    public void setPackageMonthlyRate(BigDecimal packageMonthlyRate) {
        this.packageMonthlyRate = packageMonthlyRate;
    }
    
    public Integer getTotalTrips() {
        return totalTrips;
    }
    
    public void setTotalTrips(Integer totalTrips) {
        this.totalTrips = totalTrips;
    }
    
    public Integer getIncludedTrips() {
        return includedTrips;
    }
    
    public void setIncludedTrips(Integer includedTrips) {
        this.includedTrips = includedTrips;
    }
    
    public Integer getExtraTrips() {
        return extraTrips;
    }
    
    public void setExtraTrips(Integer extraTrips) {
        this.extraTrips = extraTrips;
    }
    
    public BigDecimal getTotalDistanceKm() {
        return totalDistanceKm;
    }
    
    public void setTotalDistanceKm(BigDecimal totalDistanceKm) {
        this.totalDistanceKm = totalDistanceKm;
    }
    
    public BigDecimal getIncludedKm() {
        return includedKm;
    }
    
    public void setIncludedKm(BigDecimal includedKm) {
        this.includedKm = includedKm;
    }
    
    public BigDecimal getExtraKm() {
        return extraKm;
    }
    
    public void setExtraKm(BigDecimal extraKm) {
        this.extraKm = extraKm;
    }
    
    public BigDecimal getTotalHours() {
        return totalHours;
    }
    
    public void setTotalHours(BigDecimal totalHours) {
        this.totalHours = totalHours;
    }
    
    public BigDecimal getExtraHours() {
        return extraHours;
    }
    
    public void setExtraHours(BigDecimal extraHours) {
        this.extraHours = extraHours;
    }
    
    public BigDecimal getTripCharges() {
        return tripCharges;
    }
    
    public void setTripCharges(BigDecimal tripCharges) {
        this.tripCharges = tripCharges;
    }
    
    public BigDecimal getDistanceCharges() {
        return distanceCharges;
    }
    
    public void setDistanceCharges(BigDecimal distanceCharges) {
        this.distanceCharges = distanceCharges;
    }
    
    public BigDecimal getExtraTripCharges() {
        return extraTripCharges;
    }
    
    public void setExtraTripCharges(BigDecimal extraTripCharges) {
        this.extraTripCharges = extraTripCharges;
    }
    
    public BigDecimal getExtraKmCharges() {
        return extraKmCharges;
    }
    
    public void setExtraKmCharges(BigDecimal extraKmCharges) {
        this.extraKmCharges = extraKmCharges;
    }
    
    public BigDecimal getExtraHourCharges() {
        return extraHourCharges;
    }
    
    public void setExtraHourCharges(BigDecimal extraHourCharges) {
        this.extraHourCharges = extraHourCharges;
    }
    
    public BigDecimal getPeakHourCharges() {
        return peakHourCharges;
    }
    
    public void setPeakHourCharges(BigDecimal peakHourCharges) {
        this.peakHourCharges = peakHourCharges;
    }
    
    public BigDecimal getTotalCost() {
        return totalCost;
    }
    
    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }
    
    public BigDecimal getTaxAmount() {
        return taxAmount;
    }
    
    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }
    
    public BigDecimal getGrandTotal() {
        return grandTotal;
    }
    
    public void setGrandTotal(BigDecimal grandTotal) {
        this.grandTotal = grandTotal;
    }
    
    public List<TripSummary> getTrips() {
        return trips;
    }
    
    public void setTrips(List<TripSummary> trips) {
        this.trips = trips;
    }
    
    public String getCalculationNotes() {
        return calculationNotes;
    }
    
    public void setCalculationNotes(String calculationNotes) {
        this.calculationNotes = calculationNotes;
    }
}


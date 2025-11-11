package com.pm.billingservice.service;

import com.pm.billingservice.dto.BillingCalculationRequest;
import com.pm.billingservice.dto.BillingCalculationResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

/**
 * Billing Calculation Service for MoveInSync
 * Implements calculation logic for all 3 billing models:
 * 1. TRIP Model - Per trip and per km charges
 * 2. PACKAGE Model - Fixed monthly cost with included limits
 * 3. HYBRID Model - Combination of package and per-trip charges
 */
@Service
public class BillingCalculationService {
    
    @Value("${trip.service.url:http://localhost:4020}")
    private String tripServiceUrl;
    
    private final RestTemplate restTemplate = new RestTemplate();
    
    private static final BigDecimal GST_RATE = new BigDecimal("0.18"); // 18% GST
    
    /**
     * Main calculation method - orchestrates billing calculation
     * Time Complexity: O(n) where n = number of trips
     * Space Complexity: O(n) for storing trip data
     */
    public BillingCalculationResponse calculateBilling(BillingCalculationRequest request) {
        // Step 1: Fetch trips from Trip Service
        List<TripData> trips = fetchTripsForBilling(
            request.getClientId(),
            request.getVendorId(),
            request.getBillingMonth()
        );
        
        // Step 2: Fetch billing model from Trip Service
        BillingModelData model = fetchBillingModel(
            request.getClientId(),
            request.getVendorId()
        );
        
        // Step 3: Calculate based on billing model type
        BillingCalculationResponse response;
        switch (model.getModelType()) {
            case "TRIP":
                response = calculateTripModel(trips, model, request);
                break;
            case "PACKAGE":
                response = calculatePackageModel(trips, model, request);
                break;
            case "HYBRID":
                response = calculateHybridModel(trips, model, request);
                break;
            default:
                throw new IllegalArgumentException("Unknown billing model type: " + model.getModelType());
        }
        
        // Step 4: Add taxes and finalize
        applyTaxes(response);
        
        return response;
    }
    
    /**
     * TRIP Model Calculation
     * Formula: Total = (trips * ratePerTrip) + (totalKm * ratePerKm) + extraCharges
     * Use Case: Suitable for clients with variable trip volumes
     */
    private BillingCalculationResponse calculateTripModel(
            List<TripData> trips,
            BillingModelData model,
            BillingCalculationRequest request) {
        
        BillingCalculationResponse response = new BillingCalculationResponse();
        response.setClientId(request.getClientId());
        response.setVendorId(request.getVendorId());
        response.setBillingMonth(request.getBillingMonth());
        response.setBillingModelType("TRIP");
        
        // Calculate totals
        int totalTrips = trips.size();
        BigDecimal totalDistance = trips.stream()
            .map(TripData::getDistanceKm)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalHours = trips.stream()
            .map(TripData::getDurationHours)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // Base charges
        BigDecimal tripCharges = model.getRatePerTrip()
            .multiply(new BigDecimal(totalTrips));
        BigDecimal distanceCharges = model.getRatePerKm()
            .multiply(totalDistance);
        
        // Calculate extra charges for each trip
        BigDecimal extraKmCharges = BigDecimal.ZERO;
        BigDecimal extraHourCharges = BigDecimal.ZERO;
        BigDecimal extraKm = BigDecimal.ZERO;
        BigDecimal extraHours = BigDecimal.ZERO;
        
        for (TripData trip : trips) {
            // Extra KM beyond standard
            if (trip.getDistanceKm().compareTo(model.getStandardTripKm()) > 0) {
                BigDecimal tripExtraKm = trip.getDistanceKm().subtract(model.getStandardTripKm());
                extraKm = extraKm.add(tripExtraKm);
                extraKmCharges = extraKmCharges.add(
                    tripExtraKm.multiply(model.getExtraKmRate())
                );
            }
            
            // Extra hours beyond standard
            if (trip.getDurationHours().compareTo(model.getStandardTripHours()) > 0) {
                BigDecimal tripExtraHours = trip.getDurationHours().subtract(model.getStandardTripHours());
                extraHours = extraHours.add(tripExtraHours);
                extraHourCharges = extraHourCharges.add(
                    tripExtraHours.multiply(model.getExtraHourRate())
                );
            }
        }
        
        // Set response values
        response.setTotalTrips(totalTrips);
        response.setTotalDistanceKm(totalDistance);
        response.setTotalHours(totalHours);
        response.setExtraKm(extraKm);
        response.setExtraHours(extraHours);
        
        response.setTripCharges(tripCharges);
        response.setDistanceCharges(distanceCharges);
        response.setExtraKmCharges(extraKmCharges);
        response.setExtraHourCharges(extraHourCharges);
        
        // Calculate total cost
        BigDecimal totalCost = tripCharges
            .add(distanceCharges)
            .add(extraKmCharges)
            .add(extraHourCharges);
        
        response.setTotalCost(totalCost);
        response.setCalculationNotes(
            "TRIP Model: " + totalTrips + " trips × ₹" + model.getRatePerTrip() + 
            " + " + totalDistance + " km × ₹" + model.getRatePerKm() + 
            " + Extra charges"
        );
        
        return response;
    }
    
    /**
     * PACKAGE Model Calculation
     * Formula: Total = packageRate + overageCharges (if exceeds included limits)
     * Use Case: Suitable for clients with predictable trip volumes
     */
    private BillingCalculationResponse calculatePackageModel(
            List<TripData> trips,
            BillingModelData model,
            BillingCalculationRequest request) {
        
        BillingCalculationResponse response = new BillingCalculationResponse();
        response.setClientId(request.getClientId());
        response.setVendorId(request.getVendorId());
        response.setBillingMonth(request.getBillingMonth());
        response.setBillingModelType("PACKAGE");
        
        // Calculate totals
        int totalTrips = trips.size();
        BigDecimal totalDistance = trips.stream()
            .map(TripData::getDistanceKm)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalHours = trips.stream()
            .map(TripData::getDurationHours)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // Base package rate
        BigDecimal packageRate = model.getPackageMonthlyRate();
        
        // Calculate overages
        int extraTrips = Math.max(0, totalTrips - model.getPackageTripsIncluded());
        BigDecimal extraKm = totalDistance.subtract(model.getPackageKmsIncluded())
            .max(BigDecimal.ZERO);
        
        // Overage charges
        BigDecimal extraTripCharges = model.getExtraTripRate()
            .multiply(new BigDecimal(extraTrips));
        BigDecimal extraKmCharges = model.getExtraKmRate()
            .multiply(extraKm);
        
        // Set response values
        response.setPackageMonthlyRate(packageRate);
        response.setTotalTrips(totalTrips);
        response.setIncludedTrips(model.getPackageTripsIncluded());
        response.setExtraTrips(extraTrips);
        response.setTotalDistanceKm(totalDistance);
        response.setIncludedKm(model.getPackageKmsIncluded());
        response.setExtraKm(extraKm);
        response.setTotalHours(totalHours);
        
        response.setExtraTripCharges(extraTripCharges);
        response.setExtraKmCharges(extraKmCharges);
        
        // Calculate total cost
        BigDecimal totalCost = packageRate
            .add(extraTripCharges)
            .add(extraKmCharges);
        
        response.setTotalCost(totalCost);
        response.setCalculationNotes(
            "PACKAGE Model: Base ₹" + packageRate + 
            " (includes " + model.getPackageTripsIncluded() + " trips, " + 
            model.getPackageKmsIncluded() + " km) + Overages: " + 
            extraTrips + " extra trips, " + extraKm + " extra km"
        );
        
        return response;
    }
    
    /**
     * HYBRID Model Calculation
     * Formula: Total = packageRate + perTripCharges (for extra trips) + overageCharges
     * Use Case: Suitable for clients with base predictable volume + variable extra trips
     */
    private BillingCalculationResponse calculateHybridModel(
            List<TripData> trips,
            BillingModelData model,
            BillingCalculationRequest request) {
        
        BillingCalculationResponse response = new BillingCalculationResponse();
        response.setClientId(request.getClientId());
        response.setVendorId(request.getVendorId());
        response.setBillingMonth(request.getBillingMonth());
        response.setBillingModelType("HYBRID");
        
        // Calculate totals
        int totalTrips = trips.size();
        BigDecimal totalDistance = trips.stream()
            .map(TripData::getDistanceKm)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // Base package rate
        BigDecimal packageRate = model.getPackageMonthlyRate();
        
        // Calculate trips within and beyond package
        int includedTrips = Math.min(totalTrips, model.getPackageTripsIncluded());
        int extraTrips = Math.max(0, totalTrips - model.getPackageTripsIncluded());
        
        // For extra trips, charge per trip + per km
        BigDecimal extraTripCharges = BigDecimal.ZERO;
        BigDecimal extraDistanceCharges = BigDecimal.ZERO;
        BigDecimal extraKm = BigDecimal.ZERO;
        
        // Process only trips beyond included count
        List<TripData> extraTripList = trips.subList(
            Math.min(includedTrips, trips.size()),
            trips.size()
        );
        
        for (TripData trip : extraTripList) {
            extraTripCharges = extraTripCharges.add(model.getRatePerTrip());
            extraDistanceCharges = extraDistanceCharges.add(
                trip.getDistanceKm().multiply(model.getRatePerKm())
            );
            extraKm = extraKm.add(trip.getDistanceKm());
        }
        
        // Set response values
        response.setPackageMonthlyRate(packageRate);
        response.setTotalTrips(totalTrips);
        response.setIncludedTrips(model.getPackageTripsIncluded());
        response.setExtraTrips(extraTrips);
        response.setTotalDistanceKm(totalDistance);
        response.setExtraKm(extraKm);
        
        response.setExtraTripCharges(extraTripCharges);
        response.setDistanceCharges(extraDistanceCharges);
        
        // Calculate total cost
        BigDecimal totalCost = packageRate
            .add(extraTripCharges)
            .add(extraDistanceCharges);
        
        response.setTotalCost(totalCost);
        response.setCalculationNotes(
            "HYBRID Model: Base ₹" + packageRate + 
            " (includes " + model.getPackageTripsIncluded() + " trips) + " +
            extraTrips + " extra trips charged at ₹" + model.getRatePerTrip() + 
            " per trip + ₹" + model.getRatePerKm() + " per km"
        );
        
        return response;
    }
    
    /**
     * Apply GST taxes to the calculation
     */
    private void applyTaxes(BillingCalculationResponse response) {
        BigDecimal taxAmount = response.getTotalCost()
            .multiply(GST_RATE)
            .setScale(2, RoundingMode.HALF_UP);
        
        BigDecimal grandTotal = response.getTotalCost()
            .add(taxAmount)
            .setScale(2, RoundingMode.HALF_UP);
        
        response.setTaxAmount(taxAmount);
        response.setGrandTotal(grandTotal);
    }
    
    /**
     * Fetch trips from Trip Service
     * In real implementation, this would call the Trip Service REST API
     */
    private List<TripData> fetchTripsForBilling(UUID clientId, UUID vendorId, YearMonth month) {
        // Mock implementation - In production, call Trip Service API
        // Example: restTemplate.getForObject(tripServiceUrl + "/trips?clientId=" + clientId + "&vendorId=" + vendorId + "&month=" + month, TripData[].class);
        
        // For now, return mock data
        List<TripData> trips = new ArrayList<>();
        
        // Simulate 10 trips for the month
        for (int i = 0; i < 10; i++) {
            TripData trip = new TripData();
            trip.setId(UUID.randomUUID());
            trip.setDistanceKm(new BigDecimal("15.5"));
            trip.setDurationHours(new BigDecimal("1.2"));
            trips.add(trip);
        }
        
        return trips;
    }
    
    /**
     * Fetch billing model from Trip Service
     */
    private BillingModelData fetchBillingModel(UUID clientId, UUID vendorId) {
        // Mock implementation - In production, call Trip Service API
        
        BillingModelData model = new BillingModelData();
        model.setModelType("PACKAGE"); // Default to PACKAGE for demo
        model.setPackageMonthlyRate(new BigDecimal("25000"));
        model.setPackageTripsIncluded(100);
        model.setPackageKmsIncluded(new BigDecimal("1500"));
        model.setExtraTripRate(new BigDecimal("400"));
        model.setExtraKmRate(new BigDecimal("22"));
        model.setExtraHourRate(new BigDecimal("50"));
        model.setRatePerTrip(new BigDecimal("300"));
        model.setRatePerKm(new BigDecimal("20"));
        model.setStandardTripKm(new BigDecimal("15"));
        model.setStandardTripHours(new BigDecimal("1"));
        
        return model;
    }
    
    // Inner classes for data transfer
    private static class TripData {
        private UUID id;
        private BigDecimal distanceKm;
        private BigDecimal durationHours;
        
        public UUID getId() { return id; }
        public void setId(UUID id) { this.id = id; }
        public BigDecimal getDistanceKm() { return distanceKm; }
        public void setDistanceKm(BigDecimal distanceKm) { this.distanceKm = distanceKm; }
        public BigDecimal getDurationHours() { return durationHours; }
        public void setDurationHours(BigDecimal durationHours) { this.durationHours = durationHours; }
    }
    
    private static class BillingModelData {
        private String modelType;
        private BigDecimal ratePerTrip;
        private BigDecimal ratePerKm;
        private BigDecimal packageMonthlyRate;
        private Integer packageTripsIncluded;
        private BigDecimal packageKmsIncluded;
        private BigDecimal extraTripRate;
        private BigDecimal extraKmRate;
        private BigDecimal extraHourRate;
        private BigDecimal standardTripKm;
        private BigDecimal standardTripHours;
        
        // Getters and Setters
        public String getModelType() { return modelType; }
        public void setModelType(String modelType) { this.modelType = modelType; }
        public BigDecimal getRatePerTrip() { return ratePerTrip; }
        public void setRatePerTrip(BigDecimal ratePerTrip) { this.ratePerTrip = ratePerTrip; }
        public BigDecimal getRatePerKm() { return ratePerKm; }
        public void setRatePerKm(BigDecimal ratePerKm) { this.ratePerKm = ratePerKm; }
        public BigDecimal getPackageMonthlyRate() { return packageMonthlyRate; }
        public void setPackageMonthlyRate(BigDecimal packageMonthlyRate) { this.packageMonthlyRate = packageMonthlyRate; }
        public Integer getPackageTripsIncluded() { return packageTripsIncluded; }
        public void setPackageTripsIncluded(Integer packageTripsIncluded) { this.packageTripsIncluded = packageTripsIncluded; }
        public BigDecimal getPackageKmsIncluded() { return packageKmsIncluded; }
        public void setPackageKmsIncluded(BigDecimal packageKmsIncluded) { this.packageKmsIncluded = packageKmsIncluded; }
        public BigDecimal getExtraTripRate() { return extraTripRate; }
        public void setExtraTripRate(BigDecimal extraTripRate) { this.extraTripRate = extraTripRate; }
        public BigDecimal getExtraKmRate() { return extraKmRate; }
        public void setExtraKmRate(BigDecimal extraKmRate) { this.extraKmRate = extraKmRate; }
        public BigDecimal getExtraHourRate() { return extraHourRate; }
        public void setExtraHourRate(BigDecimal extraHourRate) { this.extraHourRate = extraHourRate; }
        public BigDecimal getStandardTripKm() { return standardTripKm; }
        public void setStandardTripKm(BigDecimal standardTripKm) { this.standardTripKm = standardTripKm; }
        public BigDecimal getStandardTripHours() { return standardTripHours; }
        public void setStandardTripHours(BigDecimal standardTripHours) { this.standardTripHours = standardTripHours; }
    }
}


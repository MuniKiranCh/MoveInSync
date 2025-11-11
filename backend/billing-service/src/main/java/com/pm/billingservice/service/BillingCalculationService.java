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
     * Calls the Trip Service REST API to get actual trip data
     */
    private List<TripData> fetchTripsForBilling(UUID clientId, UUID vendorId, YearMonth month) {
        try {
            // Convert YearMonth to LocalDate range (first and last day of month)
            LocalDate startDate = month.atDay(1);
            LocalDate endDate = month.atEndOfMonth();
            
            // Build URL with query parameters - using the existing endpoint
            String url = String.format(
                "%s/trips/client/%s/vendor/%s?startDate=%s&endDate=%s",
                tripServiceUrl,
                clientId.toString(),
                vendorId.toString(),
                startDate.toString(),
                endDate.toString()
            );
            
            // Call Trip Service API
            Map<String, Object>[] tripsArray = restTemplate.getForObject(url, Map[].class);
            
            if (tripsArray == null || tripsArray.length == 0) {
                return new ArrayList<>();
            }
            
            // Convert to TripData objects
            List<TripData> trips = new ArrayList<>();
            for (Map<String, Object> tripMap : tripsArray) {
                TripData trip = new TripData();
                trip.setId(UUID.fromString((String) tripMap.get("id")));
                trip.setDistanceKm(new BigDecimal(tripMap.get("distanceKm").toString()));
                trip.setDurationHours(new BigDecimal(tripMap.get("durationHours").toString()));
                trips.add(trip);
            }
            
            return trips;
        } catch (Exception e) {
            System.err.println("Error fetching trips from Trip Service: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    /**
     * Fetch billing model from Trip Service
     * Calls the Trip Service REST API to get the billing model for client-vendor pair
     */
    private BillingModelData fetchBillingModel(UUID clientId, UUID vendorId) {
        try {
            // Build URL
            String url = String.format(
                "%s/billing-models/client/%s/vendor/%s",
                tripServiceUrl,
                clientId.toString(),
                vendorId.toString()
            );
            
            // Call Trip Service API
            Map<String, Object> modelMap = restTemplate.getForObject(url, Map.class);
            
            if (modelMap == null) {
                throw new IllegalArgumentException("No billing model found for client " + clientId + " and vendor " + vendorId);
            }
            
            // Convert to BillingModelData
            BillingModelData model = new BillingModelData();
            model.setModelType((String) modelMap.get("modelType"));
            
            // Set all fields with safe conversion
            model.setRatePerTrip(convertToBigDecimal(modelMap.get("ratePerTrip")));
            model.setRatePerKm(convertToBigDecimal(modelMap.get("ratePerKm")));
            model.setPackageMonthlyRate(convertToBigDecimal(modelMap.get("packageMonthlyRate")));
            model.setPackageTripsIncluded(convertToInteger(modelMap.get("packageTripsIncluded")));
            model.setPackageKmsIncluded(convertToBigDecimal(modelMap.get("packageKmsIncluded")));
            model.setExtraTripRate(convertToBigDecimal(modelMap.get("extraTripRate")));
            model.setExtraKmRate(convertToBigDecimal(modelMap.get("extraKmRate")));
            model.setExtraHourRate(convertToBigDecimal(modelMap.get("extraHourRate")));
            model.setStandardTripKm(convertToBigDecimal(modelMap.get("standardTripKm")));
            model.setStandardTripHours(convertToBigDecimal(modelMap.get("standardTripHours")));
            
            return model;
        } catch (Exception e) {
            System.err.println("Error fetching billing model from Trip Service: " + e.getMessage());
            e.printStackTrace();
            throw new IllegalArgumentException("Could not fetch billing model: " + e.getMessage());
        }
    }
    
    /**
     * Helper method to safely convert Object to BigDecimal
     */
    private BigDecimal convertToBigDecimal(Object value) {
        if (value == null) return BigDecimal.ZERO;
        return new BigDecimal(value.toString());
    }
    
    /**
     * Helper method to safely convert Object to Integer
     */
    private Integer convertToInteger(Object value) {
        if (value == null) return 0;
        if (value instanceof Integer) return (Integer) value;
        return Integer.valueOf(value.toString());
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


package com.pm.billingservice.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Incentive Calculation Service for MoveInSync
 * Calculates incentives for:
 * 1. Employees - for extra hours worked
 * 2. Vendors - for extra km/trips beyond standard limits
 * 
 * As per MoveInSync requirements: Employees earn incentives for extra hours/trips
 */
@Service
public class IncentiveCalculationService {
    
    private static final BigDecimal EMPLOYEE_INCENTIVE_PER_EXTRA_HOUR = new BigDecimal("250"); // ₹250 per extra hour
    private static final BigDecimal EMPLOYEE_INCENTIVE_PER_EXTRA_TRIP = new BigDecimal("100"); // ₹100 per extra trip beyond limit
    private static final BigDecimal VENDOR_BONUS_PER_EXTRA_KM = new BigDecimal("5"); // ₹5 per km as vendor incentive
    
    /**
     * Calculate employee incentive for a single trip
     * Incentive earned when employee takes trips beyond regular office hours or extra distance
     * Time Complexity: O(1)
     */
    public BigDecimal calculateEmployeeIncentiveForTrip(
            BigDecimal tripDurationHours,
            BigDecimal standardTripHours,
            BigDecimal tripDistanceKm,
            BigDecimal standardTripKm,
            LocalDateTime tripStartTime) {
        
        BigDecimal incentive = BigDecimal.ZERO;
        
        // 1. Extra hour incentive
        if (tripDurationHours.compareTo(standardTripHours) > 0) {
            BigDecimal extraHours = tripDurationHours.subtract(standardTripHours);
            BigDecimal hourlyIncentive = extraHours.multiply(EMPLOYEE_INCENTIVE_PER_EXTRA_HOUR);
            incentive = incentive.add(hourlyIncentive);
        }
        
        // 2. Late night/early morning bonus (6 PM to 6 AM)
        int hour = tripStartTime.getHour();
        if (hour >= 18 || hour <= 6) {
            BigDecimal nightBonus = new BigDecimal("150"); // ₹150 for late/early trips
            incentive = incentive.add(nightBonus);
        }
        
        // 3. Weekend bonus
        if (tripStartTime.getDayOfWeek().getValue() >= 6) { // Saturday or Sunday
            BigDecimal weekendBonus = new BigDecimal("200"); // ₹200 for weekend trips
            incentive = incentive.add(weekendBonus);
        }
        
        return incentive;
    }
    
    /**
     * Calculate total employee incentive for a month
     * Aggregates all trip-level incentives
     */
    public EmployeeIncentiveResponse calculateMonthlyEmployeeIncentive(
            UUID employeeId,
            UUID clientId,
            int totalTrips,
            BigDecimal totalExtraHours,
            int lateNightTrips,
            int weekendTrips) {
        
        EmployeeIncentiveResponse response = new EmployeeIncentiveResponse();
        response.setEmployeeId(employeeId);
        response.setClientId(clientId);
        
        // Calculate incentives
        BigDecimal extraHourIncentive = totalExtraHours.multiply(EMPLOYEE_INCENTIVE_PER_EXTRA_HOUR);
        BigDecimal lateNightIncentive = new BigDecimal(lateNightTrips).multiply(new BigDecimal("150"));
        BigDecimal weekendIncentive = new BigDecimal(weekendTrips).multiply(new BigDecimal("200"));
        
        BigDecimal totalIncentive = extraHourIncentive
            .add(lateNightIncentive)
            .add(weekendIncentive);
        
        response.setExtraHourIncentive(extraHourIncentive);
        response.setLateNightIncentive(lateNightIncentive);
        response.setWeekendIncentive(weekendIncentive);
        response.setTotalIncentive(totalIncentive);
        response.setTotalTrips(totalTrips);
        response.setCalculationDetails(
            "Extra Hours: " + totalExtraHours + " hrs × ₹" + EMPLOYEE_INCENTIVE_PER_EXTRA_HOUR + 
            " | Late Night: " + lateNightTrips + " trips × ₹150" +
            " | Weekend: " + weekendTrips + " trips × ₹200"
        );
        
        return response;
    }
    
    /**
     * Calculate vendor bonus/incentive
     * Vendors may get bonuses for exceeding service quality targets
     */
    public VendorIncentiveResponse calculateVendorIncentive(
            UUID vendorId,
            UUID clientId,
            BigDecimal totalExtraKm,
            int totalTripsCompleted,
            double serviceRating) {
        
        VendorIncentiveResponse response = new VendorIncentiveResponse();
        response.setVendorId(vendorId);
        response.setClientId(clientId);
        
        // Bonus for extra km serviced
        BigDecimal extraKmBonus = totalExtraKm.multiply(VENDOR_BONUS_PER_EXTRA_KM);
        
        // High rating bonus (if rating > 4.5)
        BigDecimal ratingBonus = BigDecimal.ZERO;
        if (serviceRating >= 4.5) {
            ratingBonus = new BigDecimal("5000"); // ₹5000 bonus for excellent service
        } else if (serviceRating >= 4.0) {
            ratingBonus = new BigDecimal("2000"); // ₹2000 bonus for good service
        }
        
        // Volume bonus (>100 trips per month)
        BigDecimal volumeBonus = BigDecimal.ZERO;
        if (totalTripsCompleted > 100) {
            volumeBonus = new BigDecimal(totalTripsCompleted - 100)
                .multiply(new BigDecimal("50")); // ₹50 per trip beyond 100
        }
        
        BigDecimal totalIncentive = extraKmBonus
            .add(ratingBonus)
            .add(volumeBonus);
        
        response.setExtraKmBonus(extraKmBonus);
        response.setRatingBonus(ratingBonus);
        response.setVolumeBonus(volumeBonus);
        response.setTotalIncentive(totalIncentive);
        response.setServiceRating(serviceRating);
        response.setTotalTrips(totalTripsCompleted);
        
        return response;
    }
    
    // Response DTOs
    public static class EmployeeIncentiveResponse {
        private UUID employeeId;
        private UUID clientId;
        private BigDecimal extraHourIncentive;
        private BigDecimal lateNightIncentive;
        private BigDecimal weekendIncentive;
        private BigDecimal totalIncentive;
        private int totalTrips;
        private String calculationDetails;
        
        // Getters and Setters
        public UUID getEmployeeId() { return employeeId; }
        public void setEmployeeId(UUID employeeId) { this.employeeId = employeeId; }
        public UUID getClientId() { return clientId; }
        public void setClientId(UUID clientId) { this.clientId = clientId; }
        public BigDecimal getExtraHourIncentive() { return extraHourIncentive; }
        public void setExtraHourIncentive(BigDecimal extraHourIncentive) { this.extraHourIncentive = extraHourIncentive; }
        public BigDecimal getLateNightIncentive() { return lateNightIncentive; }
        public void setLateNightIncentive(BigDecimal lateNightIncentive) { this.lateNightIncentive = lateNightIncentive; }
        public BigDecimal getWeekendIncentive() { return weekendIncentive; }
        public void setWeekendIncentive(BigDecimal weekendIncentive) { this.weekendIncentive = weekendIncentive; }
        public BigDecimal getTotalIncentive() { return totalIncentive; }
        public void setTotalIncentive(BigDecimal totalIncentive) { this.totalIncentive = totalIncentive; }
        public int getTotalTrips() { return totalTrips; }
        public void setTotalTrips(int totalTrips) { this.totalTrips = totalTrips; }
        public String getCalculationDetails() { return calculationDetails; }
        public void setCalculationDetails(String calculationDetails) { this.calculationDetails = calculationDetails; }
    }
    
    public static class VendorIncentiveResponse {
        private UUID vendorId;
        private UUID clientId;
        private BigDecimal extraKmBonus;
        private BigDecimal ratingBonus;
        private BigDecimal volumeBonus;
        private BigDecimal totalIncentive;
        private double serviceRating;
        private int totalTrips;
        
        // Getters and Setters
        public UUID getVendorId() { return vendorId; }
        public void setVendorId(UUID vendorId) { this.vendorId = vendorId; }
        public UUID getClientId() { return clientId; }
        public void setClientId(UUID clientId) { this.clientId = clientId; }
        public BigDecimal getExtraKmBonus() { return extraKmBonus; }
        public void setExtraKmBonus(BigDecimal extraKmBonus) { this.extraKmBonus = extraKmBonus; }
        public BigDecimal getRatingBonus() { return ratingBonus; }
        public void setRatingBonus(BigDecimal ratingBonus) { this.ratingBonus = ratingBonus; }
        public BigDecimal getVolumeBonus() { return volumeBonus; }
        public void setVolumeBonus(BigDecimal volumeBonus) { this.volumeBonus = volumeBonus; }
        public BigDecimal getTotalIncentive() { return totalIncentive; }
        public void setTotalIncentive(BigDecimal totalIncentive) { this.totalIncentive = totalIncentive; }
        public double getServiceRating() { return serviceRating; }
        public void setServiceRating(double serviceRating) { this.serviceRating = serviceRating; }
        public int getTotalTrips() { return totalTrips; }
        public void setTotalTrips(int totalTrips) { this.totalTrips = totalTrips; }
    }
}


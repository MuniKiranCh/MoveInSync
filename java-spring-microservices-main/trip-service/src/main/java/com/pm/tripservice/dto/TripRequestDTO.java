package com.pm.tripservice.dto;

import com.pm.tripservice.model.TripStatus;
import com.pm.tripservice.model.TripType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class TripRequestDTO {

    @NotNull(message = "Client ID is required")
    private UUID clientId;

    @NotNull(message = "Vendor ID is required")
    private UUID vendorId;

    @NotNull(message = "Employee ID is required")
    private UUID employeeId;

    @NotNull(message = "Vehicle number is required")
    @Size(min = 5, max = 20, message = "Vehicle number must be between 5 and 20 characters")
    private String vehicleNumber;

    private String driverName;

    private String driverPhone;

    @NotNull(message = "Trip start time is required")
    private Instant tripStartTime;

    private Instant tripEndTime;

    @NotNull(message = "Pickup location is required")
    @Size(min = 3, max = 200, message = "Pickup location must be between 3 and 200 characters")
    private String pickupLocation;

    private String dropLocation;

    private BigDecimal distanceKm;

    private TripType tripType;

    private TripStatus status;

    private String notes;

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

    public UUID getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(UUID employeeId) {
        this.employeeId = employeeId;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverPhone() {
        return driverPhone;
    }

    public void setDriverPhone(String driverPhone) {
        this.driverPhone = driverPhone;
    }

    public Instant getTripStartTime() {
        return tripStartTime;
    }

    public void setTripStartTime(Instant tripStartTime) {
        this.tripStartTime = tripStartTime;
    }

    public Instant getTripEndTime() {
        return tripEndTime;
    }

    public void setTripEndTime(Instant tripEndTime) {
        this.tripEndTime = tripEndTime;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public String getDropLocation() {
        return dropLocation;
    }

    public void setDropLocation(String dropLocation) {
        this.dropLocation = dropLocation;
    }

    public BigDecimal getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(BigDecimal distanceKm) {
        this.distanceKm = distanceKm;
    }

    public TripType getTripType() {
        return tripType;
    }

    public void setTripType(TripType tripType) {
        this.tripType = tripType;
    }

    public TripStatus getStatus() {
        return status;
    }

    public void setStatus(TripStatus status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}


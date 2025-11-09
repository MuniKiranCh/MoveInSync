package com.pm.tripservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "trips", indexes = {
    @Index(name = "idx_trip_client", columnList = "clientId"),
    @Index(name = "idx_trip_vendor", columnList = "vendorId"),
    @Index(name = "idx_trip_employee", columnList = "employeeId"),
    @Index(name = "idx_trip_start_time", columnList = "tripStartTime"),
    @Index(name = "idx_trip_client_vendor_date", columnList = "clientId,vendorId,tripStartTime")
})
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotNull
    @Column(nullable = false)
    private UUID clientId;  // Tenant ID

    @NotNull
    @Column(nullable = false)
    private UUID vendorId;

    @NotNull
    @Column(nullable = false)
    private UUID employeeId;

    @NotNull
    @Column(nullable = false)
    private String vehicleNumber;

    @Column
    private String driverName;

    @Column
    private String driverPhone;

    @NotNull
    @Column(nullable = false)
    private Instant tripStartTime;

    @Column
    private Instant tripEndTime;

    @NotNull
    @Column(nullable = false)
    private String pickupLocation;

    @Column
    private String dropLocation;

    @Column(precision = 10, scale = 2)
    private BigDecimal distanceKm;

    @Column(precision = 10, scale = 2)
    private BigDecimal durationHours;

    @Enumerated(EnumType.STRING)
    @Column
    private TripType tripType;

    @Enumerated(EnumType.STRING)
    @Column
    private TripStatus status;

    @Column(precision = 10, scale = 2)
    private BigDecimal baseCost;

    @Column(precision = 10, scale = 2)
    private BigDecimal extraKmCost;

    @Column(precision = 10, scale = 2)
    private BigDecimal extraHourCost;

    @Column(precision = 10, scale = 2)
    private BigDecimal totalCost;

    @Column
    private UUID billingCycleId;

    @Column
    private Boolean billed = false;

    @Column
    private String notes;

    @Column
    private Instant createdAt;

    @Column
    private Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
        if (status == null) {
            status = TripStatus.SCHEDULED;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }

    // Business methods
    public Duration getDuration() {
        if (tripEndTime == null) {
            return Duration.between(tripStartTime, Instant.now());
        }
        return Duration.between(tripStartTime, tripEndTime);
    }

    public void completeTrip(Instant endTime, BigDecimal distance, String dropLocation) {
        this.tripEndTime = endTime;
        this.distanceKm = distance;
        this.dropLocation = dropLocation;
        this.status = TripStatus.COMPLETED;
        
        // Calculate duration in hours
        Duration duration = Duration.between(tripStartTime, endTime);
        this.durationHours = BigDecimal.valueOf(duration.toMinutes() / 60.0);
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

    public BigDecimal getDurationHours() {
        return durationHours;
    }

    public void setDurationHours(BigDecimal durationHours) {
        this.durationHours = durationHours;
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

    public BigDecimal getBaseCost() {
        return baseCost;
    }

    public void setBaseCost(BigDecimal baseCost) {
        this.baseCost = baseCost;
    }

    public BigDecimal getExtraKmCost() {
        return extraKmCost;
    }

    public void setExtraKmCost(BigDecimal extraKmCost) {
        this.extraKmCost = extraKmCost;
    }

    public BigDecimal getExtraHourCost() {
        return extraHourCost;
    }

    public void setExtraHourCost(BigDecimal extraHourCost) {
        this.extraHourCost = extraHourCost;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public UUID getBillingCycleId() {
        return billingCycleId;
    }

    public void setBillingCycleId(UUID billingCycleId) {
        this.billingCycleId = billingCycleId;
    }

    public Boolean getBilled() {
        return billed;
    }

    public void setBilled(Boolean billed) {
        this.billed = billed;
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
}


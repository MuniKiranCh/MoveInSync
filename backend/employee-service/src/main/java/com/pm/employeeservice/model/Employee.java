package com.pm.employeeservice.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "employees", indexes = {
    @Index(name = "idx_employee_client", columnList = "clientId"),
    @Index(name = "idx_employee_email", columnList = "email"),
    @Index(name = "idx_employee_code", columnList = "employeeCode")
})
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private UUID clientId;  // Tenant ID - which company they work for

    @Column(nullable = false, unique = true)
    private String employeeCode;  // EMP001, EMP002, etc.

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String phoneNumber;

    @Column
    private String department;  // HR, IT, Sales, etc.

    @Column
    private String designation;  // Manager, Developer, etc.

    @Column(columnDefinition = "TEXT")
    private String homeAddress;

    @Column
    private LocalDate dateOfJoining;

    @Column
    private LocalDate dateOfBirth;

    @Column
    private Boolean active = true;

    // Incentive tracking fields
    @Column(precision = 10, scale = 2)
    private BigDecimal totalIncentivesEarned = BigDecimal.ZERO;

    @Column
    private Integer totalTripsCompleted = 0;

    @Column(precision = 10, scale = 2)
    private BigDecimal totalDistanceTraveled = BigDecimal.ZERO;

    @Column
    private Instant createdAt;

    @Column
    private Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
        if (active == null) {
            active = true;
        }
        if (totalIncentivesEarned == null) {
            totalIncentivesEarned = BigDecimal.ZERO;
        }
        if (totalTripsCompleted == null) {
            totalTripsCompleted = 0;
        }
        if (totalDistanceTraveled == null) {
            totalDistanceTraveled = BigDecimal.ZERO;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }

    // Business methods
    public String getFullName() {
        return firstName + " " + lastName;
    }

    public void incrementTripCount() {
        this.totalTripsCompleted++;
    }

    public void addIncentive(BigDecimal amount) {
        this.totalIncentivesEarned = this.totalIncentivesEarned.add(amount);
    }

    public void addDistance(BigDecimal distance) {
        this.totalDistanceTraveled = this.totalDistanceTraveled.add(distance);
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

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public LocalDate getDateOfJoining() {
        return dateOfJoining;
    }

    public void setDateOfJoining(LocalDate dateOfJoining) {
        this.dateOfJoining = dateOfJoining;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public BigDecimal getTotalIncentivesEarned() {
        return totalIncentivesEarned;
    }

    public void setTotalIncentivesEarned(BigDecimal totalIncentivesEarned) {
        this.totalIncentivesEarned = totalIncentivesEarned;
    }

    public Integer getTotalTripsCompleted() {
        return totalTripsCompleted;
    }

    public void setTotalTripsCompleted(Integer totalTripsCompleted) {
        this.totalTripsCompleted = totalTripsCompleted;
    }

    public BigDecimal getTotalDistanceTraveled() {
        return totalDistanceTraveled;
    }

    public void setTotalDistanceTraveled(BigDecimal totalDistanceTraveled) {
        this.totalDistanceTraveled = totalDistanceTraveled;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}


package com.pm.vendorservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "vendors", indexes = {
    @Index(name = "idx_vendor_code", columnList = "code"),
    @Index(name = "idx_client_id", columnList = "client_id")
})
public class Vendor {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotNull
    @Column(nullable = false)
    private String name;

    @NotNull
    @Column(nullable = false, unique = true)
    private String code;  // e.g., "OLA001", "UBER001"

    @Column(name = "client_id")
    private UUID clientId;  // Reference to the client (Amazon)

    @Column
    private String serviceType;  // e.g., "Cab Service", "Ride Sharing"

    @Column(columnDefinition = "TEXT")
    private String address;

    @Email
    @Column
    private String contactEmail;

    @Column
    private String contactPhone;

    @Column
    private String contactPerson;

    @Column(columnDefinition = "TEXT")
    private String bankAccountDetails;

    @Column
    private String taxId;

    @Column
    private String gstNumber;

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
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public UUID getClientId() {
        return clientId;
    }

    public void setClientId(UUID clientId) {
        this.clientId = clientId;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getBankAccountDetails() {
        return bankAccountDetails;
    }

    public void setBankAccountDetails(String bankAccountDetails) {
        this.bankAccountDetails = bankAccountDetails;
    }

    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    public String getGstNumber() {
        return gstNumber;
    }

    public void setGstNumber(String gstNumber) {
        this.gstNumber = gstNumber;
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


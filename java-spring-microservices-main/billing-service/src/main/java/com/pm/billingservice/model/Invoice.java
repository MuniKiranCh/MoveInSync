package com.pm.billingservice.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "invoices", indexes = {
    @Index(name = "idx_invoice_client_vendor", columnList = "clientId,vendorId"),
    @Index(name = "idx_invoice_billing_period", columnList = "billingPeriodStart,billingPeriodEnd"),
    @Index(name = "idx_invoice_status", columnList = "status")
})
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String invoiceNumber;

    @Column(nullable = false)
    private UUID clientId;

    @Column(nullable = false)
    private UUID vendorId;

    @Column(nullable = false)
    private LocalDate billingPeriodStart;

    @Column(nullable = false)
    private LocalDate billingPeriodEnd;

    @Column(precision = 10, scale = 2)
    private BigDecimal baseAmount;

    @Column(precision = 10, scale = 2)
    private BigDecimal extraCharges;

    @Column(precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(precision = 10, scale = 2)
    private BigDecimal taxAmount;

    @Column(precision = 10, scale = 2)
    private BigDecimal finalAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InvoiceStatus status;

    @Column
    private LocalDate dueDate;

    @Column
    private LocalDate paidDate;

    @Column
    private Integer totalTrips;

    @Column(precision = 10, scale = 2)
    private BigDecimal totalKm;

    @Column(precision = 10, scale = 2)
    private BigDecimal totalHours;

    @Column(columnDefinition = "TEXT")
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
            status = InvoiceStatus.DRAFT;
        }
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

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
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

    public LocalDate getBillingPeriodStart() {
        return billingPeriodStart;
    }

    public void setBillingPeriodStart(LocalDate billingPeriodStart) {
        this.billingPeriodStart = billingPeriodStart;
    }

    public LocalDate getBillingPeriodEnd() {
        return billingPeriodEnd;
    }

    public void setBillingPeriodEnd(LocalDate billingPeriodEnd) {
        this.billingPeriodEnd = billingPeriodEnd;
    }

    public BigDecimal getBaseAmount() {
        return baseAmount;
    }

    public void setBaseAmount(BigDecimal baseAmount) {
        this.baseAmount = baseAmount;
    }

    public BigDecimal getExtraCharges() {
        return extraCharges;
    }

    public void setExtraCharges(BigDecimal extraCharges) {
        this.extraCharges = extraCharges;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(BigDecimal finalAmount) {
        this.finalAmount = finalAmount;
    }

    public InvoiceStatus getStatus() {
        return status;
    }

    public void setStatus(InvoiceStatus status) {
        this.status = status;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getPaidDate() {
        return paidDate;
    }

    public void setPaidDate(LocalDate paidDate) {
        this.paidDate = paidDate;
    }

    public Integer getTotalTrips() {
        return totalTrips;
    }

    public void setTotalTrips(Integer totalTrips) {
        this.totalTrips = totalTrips;
    }

    public BigDecimal getTotalKm() {
        return totalKm;
    }

    public void setTotalKm(BigDecimal totalKm) {
        this.totalKm = totalKm;
    }

    public BigDecimal getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(BigDecimal totalHours) {
        this.totalHours = totalHours;
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


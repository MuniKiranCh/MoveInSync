package com.pm.billingservice.dto;

import jakarta.validation.constraints.NotNull;
import java.time.YearMonth;
import java.util.UUID;

public class BillingCalculationRequest {
    @NotNull(message = "Client ID is required")
    private UUID clientId;
    
    @NotNull(message = "Vendor ID is required")
    private UUID vendorId;
    
    @NotNull(message = "Billing month is required")
    private YearMonth billingMonth;
    
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
}


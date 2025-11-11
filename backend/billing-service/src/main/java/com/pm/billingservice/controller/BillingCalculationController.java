package com.pm.billingservice.controller;

import com.pm.billingservice.dto.BillingCalculationRequest;
import com.pm.billingservice.dto.BillingCalculationResponse;
import com.pm.billingservice.service.BillingCalculationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.UUID;

@RestController
@RequestMapping("/billing/calculate")
@Tag(name = "Billing Calculations", description = "APIs for calculating billing amounts based on different models")
@CrossOrigin(origins = "*")
public class BillingCalculationController {
    
    @Autowired
    private BillingCalculationService billingCalculationService;
    
    @PostMapping
    @Operation(summary = "Calculate billing", description = "Calculates billing for a client-vendor pair for a given month using the configured billing model (TRIP/PACKAGE/HYBRID)")
    public ResponseEntity<BillingCalculationResponse> calculateBilling(@Valid @RequestBody BillingCalculationRequest request) {
        BillingCalculationResponse response = billingCalculationService.calculateBilling(request);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/client/{clientId}/vendor/{vendorId}")
    @Operation(summary = "Calculate billing by IDs", description = "Calculates billing for current month using client and vendor IDs")
    public ResponseEntity<BillingCalculationResponse> calculateBillingByIds(
            @PathVariable UUID clientId,
            @PathVariable UUID vendorId,
            @RequestParam(required = false) String month) {
        
        BillingCalculationRequest request = new BillingCalculationRequest();
        request.setClientId(clientId);
        request.setVendorId(vendorId);
        request.setBillingMonth(month != null ? YearMonth.parse(month) : YearMonth.now());
        
        BillingCalculationResponse response = billingCalculationService.calculateBilling(request);
        return ResponseEntity.ok(response);
    }
}


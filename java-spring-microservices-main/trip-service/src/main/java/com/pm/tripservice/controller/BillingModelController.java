package com.pm.tripservice.controller;

import com.pm.tripservice.model.BillingModel;
import com.pm.tripservice.repository.BillingModelRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/billing-models")
@Tag(name = "Billing Models", description = "APIs for managing billing models")
public class BillingModelController {
    
    private final BillingModelRepository billingModelRepository;

    public BillingModelController(BillingModelRepository billingModelRepository) {
        this.billingModelRepository = billingModelRepository;
    }

    @Operation(summary = "Get all billing models for a client")
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<BillingModel>> getBillingModelsByClient(@PathVariable UUID clientId) {
        List<BillingModel> models = billingModelRepository.findByClientId(clientId);
        return ResponseEntity.ok(models);
    }

    @Operation(summary = "Get all active billing models for a client")
    @GetMapping("/client/{clientId}/active")
    public ResponseEntity<List<BillingModel>> getActiveBillingModelsByClient(@PathVariable UUID clientId) {
        List<BillingModel> models = billingModelRepository.findByClientIdAndActive(clientId, true);
        return ResponseEntity.ok(models);
    }

    @Operation(summary = "Get billing model for a specific client-vendor combination")
    @GetMapping("/client/{clientId}/vendor/{vendorId}")
    public ResponseEntity<BillingModel> getBillingModelByClientAndVendor(
            @PathVariable UUID clientId,
            @PathVariable UUID vendorId) {
        return billingModelRepository.findByClientIdAndVendorId(clientId, vendorId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get all billing models for a vendor")
    @GetMapping("/vendor/{vendorId}")
    public ResponseEntity<List<BillingModel>> getBillingModelsByVendor(@PathVariable UUID vendorId) {
        List<BillingModel> models = billingModelRepository.findByVendorId(vendorId);
        return ResponseEntity.ok(models);
    }

    @Operation(summary = "Get all active billing models for a vendor")
    @GetMapping("/vendor/{vendorId}/active")
    public ResponseEntity<List<BillingModel>> getActiveBillingModelsByVendor(@PathVariable UUID vendorId) {
        List<BillingModel> models = billingModelRepository.findByVendorIdAndActive(vendorId, true);
        return ResponseEntity.ok(models);
    }

    @Operation(summary = "Get all billing models")
    @GetMapping
    public ResponseEntity<List<BillingModel>> getAllBillingModels() {
        List<BillingModel> models = billingModelRepository.findAll();
        return ResponseEntity.ok(models);
    }

    @Operation(summary = "Get billing model by ID")
    @GetMapping("/{id}")
    public ResponseEntity<BillingModel> getBillingModelById(@PathVariable UUID id) {
        return billingModelRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}


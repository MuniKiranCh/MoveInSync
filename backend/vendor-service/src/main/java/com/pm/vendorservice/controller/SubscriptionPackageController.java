package com.pm.vendorservice.controller;

import com.pm.vendorservice.dto.SubscriptionPackageRequestDTO;
import com.pm.vendorservice.dto.SubscriptionPackageResponseDTO;
import com.pm.vendorservice.service.SubscriptionPackageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/subscription-packages")
@CrossOrigin(origins = "*")
public class SubscriptionPackageController {

    @Autowired
    private SubscriptionPackageService packageService;

    @PostMapping
    public ResponseEntity<SubscriptionPackageResponseDTO> createPackage(@Valid @RequestBody SubscriptionPackageRequestDTO requestDTO) {
        SubscriptionPackageResponseDTO response = packageService.createPackage(requestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionPackageResponseDTO> getPackageById(@PathVariable Long id) {
        SubscriptionPackageResponseDTO response = packageService.getPackageById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<SubscriptionPackageResponseDTO> getPackageByCode(@PathVariable String code) {
        SubscriptionPackageResponseDTO response = packageService.getPackageByCode(code);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<SubscriptionPackageResponseDTO>> getAllPackages() {
        List<SubscriptionPackageResponseDTO> packages = packageService.getAllPackages();
        return ResponseEntity.ok(packages);
    }

    @GetMapping("/vendor/{vendorId}")
    public ResponseEntity<List<SubscriptionPackageResponseDTO>> getPackagesByVendorId(@PathVariable UUID vendorId) {
        List<SubscriptionPackageResponseDTO> packages = packageService.getPackagesByVendorId(vendorId);
        return ResponseEntity.ok(packages);
    }

    @GetMapping("/vendor/{vendorId}/active")
    public ResponseEntity<List<SubscriptionPackageResponseDTO>> getActivePackagesByVendorId(@PathVariable UUID vendorId) {
        List<SubscriptionPackageResponseDTO> packages = packageService.getActivePackagesByVendorId(vendorId);
        return ResponseEntity.ok(packages);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubscriptionPackageResponseDTO> updatePackage(
            @PathVariable Long id,
            @Valid @RequestBody SubscriptionPackageRequestDTO requestDTO) {
        SubscriptionPackageResponseDTO response = packageService.updatePackage(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePackage(@PathVariable Long id) {
        packageService.deletePackage(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivatePackage(@PathVariable Long id) {
        packageService.deactivatePackage(id);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/vendor/email/{email}/active")
    public ResponseEntity<List<SubscriptionPackageResponseDTO>> getActivePackagesByVendorEmail(@PathVariable String email) {
        // First get vendor by email, then get their packages
        try {
            // This will be used with the VendorService to lookup by email
            List<SubscriptionPackageResponseDTO> packages = packageService.getActivePackagesByVendorEmail(email);
            return ResponseEntity.ok(packages);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}


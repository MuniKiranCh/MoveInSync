package com.pm.vendorservice.controller;

import com.pm.vendorservice.dto.VendorRequestDTO;
import com.pm.vendorservice.dto.VendorResponseDTO;
import com.pm.vendorservice.dto.VendorWithPackagesDTO;
import com.pm.vendorservice.service.VendorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/vendors")
@Tag(name = "Vendor", description = "Vendor/transport provider management endpoints")
@CrossOrigin(origins = "*")
public class VendorController {

    @Autowired
    private VendorService vendorService;

    @PostMapping
    @Operation(summary = "Create a new vendor")
    public ResponseEntity<VendorResponseDTO> createVendor(@Valid @RequestBody VendorRequestDTO requestDTO) {
        VendorResponseDTO response = vendorService.createVendor(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get vendor by ID")
    public ResponseEntity<VendorResponseDTO> getVendorById(@PathVariable UUID id) {
        VendorResponseDTO response = vendorService.getVendorById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/code/{code}")
    @Operation(summary = "Get vendor by code")
    public ResponseEntity<VendorResponseDTO> getVendorByCode(@PathVariable String code) {
        VendorResponseDTO response = vendorService.getVendorByCode(code);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Get all vendors")
    public ResponseEntity<List<VendorResponseDTO>> getAllVendors() {
        List<VendorResponseDTO> vendors = vendorService.getAllVendors();
        return ResponseEntity.ok(vendors);
    }

    @GetMapping("/active")
    @Operation(summary = "Get all active vendors")
    public ResponseEntity<List<VendorResponseDTO>> getActiveVendors() {
        List<VendorResponseDTO> vendors = vendorService.getActiveVendors();
        return ResponseEntity.ok(vendors);
    }

    @GetMapping("/client/{clientId}")
    @Operation(summary = "Get vendors by client ID")
    public ResponseEntity<List<VendorResponseDTO>> getVendorsByClient(@PathVariable UUID clientId) {
        List<VendorResponseDTO> vendors = vendorService.getVendorsByClient(clientId);
        return ResponseEntity.ok(vendors);
    }

    @GetMapping("/client/{clientId}/active")
    @Operation(summary = "Get active vendors by client ID")
    public ResponseEntity<List<VendorResponseDTO>> getActiveVendorsByClient(@PathVariable UUID clientId) {
        List<VendorResponseDTO> vendors = vendorService.getActiveVendorsByClient(clientId);
        return ResponseEntity.ok(vendors);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update vendor")
    public ResponseEntity<VendorResponseDTO> updateVendor(
            @PathVariable UUID id,
            @Valid @RequestBody VendorRequestDTO requestDTO) {
        VendorResponseDTO response = vendorService.updateVendor(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate vendor")
    public ResponseEntity<Void> deactivateVendor(@PathVariable UUID id) {
        vendorService.deactivateVendor(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete vendor")
    public ResponseEntity<Void> deleteVendor(@PathVariable UUID id) {
        vendorService.deleteVendor(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/{id}/with-packages")
    @Operation(summary = "Get vendor with subscription packages")
    public ResponseEntity<VendorWithPackagesDTO> getVendorWithPackages(@PathVariable UUID id) {
        VendorWithPackagesDTO response = vendorService.getVendorWithPackages(id);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/with-packages")
    @Operation(summary = "Get all vendors with their subscription packages")
    public ResponseEntity<List<VendorWithPackagesDTO>> getAllVendorsWithPackages() {
        List<VendorWithPackagesDTO> vendors = vendorService.getAllVendorsWithPackages();
        return ResponseEntity.ok(vendors);
    }
    
    @GetMapping("/email/{email}")
    @Operation(summary = "Get vendor by contact email")
    public ResponseEntity<VendorResponseDTO> getVendorByEmail(@PathVariable String email) {
        VendorResponseDTO response = vendorService.getVendorByEmail(email);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/email/{email}/with-packages")
    @Operation(summary = "Get vendor with packages by contact email")
    public ResponseEntity<VendorWithPackagesDTO> getVendorWithPackagesByEmail(@PathVariable String email) {
        VendorWithPackagesDTO response = vendorService.getVendorWithPackagesByEmail(email);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/client/{clientId}/with-packages")
    @Operation(summary = "Get all vendors with packages for a specific client")
    public ResponseEntity<List<VendorWithPackagesDTO>> getClientVendorsWithPackages(@PathVariable UUID clientId) {
        List<VendorWithPackagesDTO> vendors = vendorService.getClientVendorsWithPackages(clientId);
        return ResponseEntity.ok(vendors);
    }
}


package com.pm.tripservice.controller;

import com.pm.tripservice.dto.TripRequestDTO;
import com.pm.tripservice.dto.TripResponseDTO;
import com.pm.tripservice.service.TripService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/trips")
@Tag(name = "Trip", description = "Trip management endpoints")
public class TripController {

    @Autowired
    private TripService tripService;

    @PostMapping
    @Operation(summary = "Create a new trip")
    public ResponseEntity<TripResponseDTO> createTrip(@Valid @RequestBody TripRequestDTO requestDTO) {
        TripResponseDTO response = tripService.createTrip(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get trip by ID")
    public ResponseEntity<TripResponseDTO> getTripById(@PathVariable UUID id) {
        TripResponseDTO response = tripService.getTripById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Get all trips")
    public ResponseEntity<List<TripResponseDTO>> getAllTrips() {
        List<TripResponseDTO> trips = tripService.getAllTrips();
        return ResponseEntity.ok(trips);
    }

    @GetMapping("/client/{clientId}")
    @Operation(summary = "Get all trips for a client")
    public ResponseEntity<List<TripResponseDTO>> getTripsByClient(@PathVariable UUID clientId) {
        List<TripResponseDTO> trips = tripService.getTripsByClient(clientId);
        return ResponseEntity.ok(trips);
    }

    @GetMapping("/employee/{employeeId}")
    @Operation(summary = "Get all trips for an employee")
    public ResponseEntity<List<TripResponseDTO>> getTripsByEmployee(@PathVariable UUID employeeId) {
        List<TripResponseDTO> trips = tripService.getTripsByEmployee(employeeId);
        return ResponseEntity.ok(trips);
    }

    @GetMapping("/vendor/{vendorId}")
    @Operation(summary = "Get all trips for a vendor")
    public ResponseEntity<List<TripResponseDTO>> getTripsByVendor(@PathVariable UUID vendorId) {
        List<TripResponseDTO> trips = tripService.getTripsByVendor(vendorId);
        return ResponseEntity.ok(trips);
    }

    @GetMapping("/client/{clientId}/vendor/{vendorId}")
    @Operation(summary = "Get trips by client, vendor and date range")
    public ResponseEntity<List<TripResponseDTO>> getTripsByClientVendorAndDateRange(
            @PathVariable UUID clientId,
            @PathVariable UUID vendorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        List<TripResponseDTO> trips = tripService.getTripsByClientVendorAndDateRange(
            clientId, vendorId, startDate, endDate
        );
        return ResponseEntity.ok(trips);
    }

    @GetMapping("/employee/{employeeId}/daterange")
    @Operation(summary = "Get trips by employee and date range")
    public ResponseEntity<List<TripResponseDTO>> getTripsByEmployeeAndDateRange(
            @PathVariable UUID employeeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        List<TripResponseDTO> trips = tripService.getTripsByEmployeeAndDateRange(
            employeeId, startDate, endDate
        );
        return ResponseEntity.ok(trips);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update trip")
    public ResponseEntity<TripResponseDTO> updateTrip(
            @PathVariable UUID id,
            @Valid @RequestBody TripRequestDTO requestDTO) {
        TripResponseDTO response = tripService.updateTrip(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/complete")
    @Operation(summary = "Complete a trip")
    public ResponseEntity<TripResponseDTO> completeTrip(
            @PathVariable UUID id,
            @RequestParam BigDecimal distance,
            @RequestParam String dropLocation) {
        TripResponseDTO response = tripService.completeTrip(id, distance, dropLocation);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/cancel")
    @Operation(summary = "Cancel a trip")
    public ResponseEntity<TripResponseDTO> cancelTrip(
            @PathVariable UUID id,
            @RequestParam(required = false) String reason) {
        TripResponseDTO response = tripService.cancelTrip(id, reason);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete trip")
    public ResponseEntity<Void> deleteTrip(@PathVariable UUID id) {
        tripService.deleteTrip(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/client/{clientId}/unbilled")
    @Operation(summary = "Get unbilled trips for a client")
    public ResponseEntity<List<TripResponseDTO>> getUnbilledTrips(@PathVariable UUID clientId) {
        List<TripResponseDTO> trips = tripService.getUnbilledTripsByClient(clientId);
        return ResponseEntity.ok(trips);
    }

    @GetMapping("/client/{clientId}/count")
    @Operation(summary = "Count trips by client and month")
    public ResponseEntity<Integer> countTripsByClientAndMonth(
            @PathVariable UUID clientId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate month) {
        Integer count = tripService.countTripsByClientAndMonth(clientId, month);
        return ResponseEntity.ok(count);
    }
}


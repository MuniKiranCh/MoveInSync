package com.pm.employeeservice.controller;

import com.pm.employeeservice.dto.PackageAssignmentRequestDTO;
import com.pm.employeeservice.dto.PackageAssignmentResponseDTO;
import com.pm.employeeservice.service.PackageAssignmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/package-assignments")
@CrossOrigin(origins = "*")
public class PackageAssignmentController {

    @Autowired
    private PackageAssignmentService packageAssignmentService;

    @PostMapping
    public ResponseEntity<PackageAssignmentResponseDTO> assignPackage(
            @Valid @RequestBody PackageAssignmentRequestDTO requestDTO,
            @RequestHeader(value = "X-User-Id", required = false) String userIdHeader) {
        
        UUID assignedBy = userIdHeader != null ? UUID.fromString(userIdHeader) : null;
        PackageAssignmentResponseDTO response = packageAssignmentService.assignPackage(requestDTO, assignedBy);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<PackageAssignmentResponseDTO>> getEmployeeAssignments(@PathVariable UUID employeeId) {
        List<PackageAssignmentResponseDTO> assignments = packageAssignmentService.getEmployeeAssignments(employeeId);
        return ResponseEntity.ok(assignments);
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<PackageAssignmentResponseDTO>> getClientAssignments(@PathVariable UUID clientId) {
        List<PackageAssignmentResponseDTO> assignments = packageAssignmentService.getClientAssignments(clientId);
        return ResponseEntity.ok(assignments);
    }

    @DeleteMapping("/{assignmentId}")
    public ResponseEntity<Void> deleteAssignment(@PathVariable Long assignmentId) {
        packageAssignmentService.deleteAssignment(assignmentId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{assignmentId}/deactivate")
    public ResponseEntity<Void> deactivateAssignment(@PathVariable Long assignmentId) {
        packageAssignmentService.deactivateAssignment(assignmentId);
        return ResponseEntity.ok().build();
    }
}


package com.pm.employeeservice.controller;

import com.pm.employeeservice.dto.EmployeeRequestDTO;
import com.pm.employeeservice.dto.EmployeeResponseDTO;
import com.pm.employeeservice.service.EmployeeService;
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
@RequestMapping("/employees")
@Tag(name = "Employee Management", description = "APIs for managing employees in the MoveInSync platform")
@CrossOrigin(origins = "*")
public class EmployeeController {
    
    @Autowired
    private EmployeeService employeeService;
    
    @PostMapping
    @Operation(summary = "Create new employee", description = "Creates a new employee for a client organization")
    public ResponseEntity<EmployeeResponseDTO> createEmployee(@Valid @RequestBody EmployeeRequestDTO request) {
        EmployeeResponseDTO response = employeeService.createEmployee(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get employee by ID", description = "Retrieves employee details by ID")
    public ResponseEntity<EmployeeResponseDTO> getEmployeeById(@PathVariable UUID id) {
        EmployeeResponseDTO response = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping
    @Operation(summary = "Get all employees", description = "Retrieves all employees across all clients (Admin only)")
    public ResponseEntity<List<EmployeeResponseDTO>> getAllEmployees() {
        List<EmployeeResponseDTO> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }
    
    @GetMapping("/client/{clientId}")
    @Operation(summary = "Get employees by client", description = "Retrieves all employees for a specific client")
    public ResponseEntity<List<EmployeeResponseDTO>> getEmployeesByClient(@PathVariable UUID clientId) {
        List<EmployeeResponseDTO> employees = employeeService.getEmployeesByClientId(clientId);
        return ResponseEntity.ok(employees);
    }
    
    @GetMapping("/client/{clientId}/active")
    @Operation(summary = "Get active employees by client", description = "Retrieves only active employees for a specific client")
    public ResponseEntity<List<EmployeeResponseDTO>> getActiveEmployeesByClient(@PathVariable UUID clientId) {
        List<EmployeeResponseDTO> employees = employeeService.getActiveEmployeesByClientId(clientId);
        return ResponseEntity.ok(employees);
    }
    
    @GetMapping("/client/{clientId}/department/{department}")
    @Operation(summary = "Get employees by department", description = "Retrieves employees by client and department")
    public ResponseEntity<List<EmployeeResponseDTO>> getEmployeesByDepartment(
            @PathVariable UUID clientId,
            @PathVariable String department) {
        List<EmployeeResponseDTO> employees = employeeService.getEmployeesByDepartment(clientId, department);
        return ResponseEntity.ok(employees);
    }
    
    @GetMapping("/client/{clientId}/count")
    @Operation(summary = "Get active employee count", description = "Returns the count of active employees for a client")
    public ResponseEntity<Long> getActiveEmployeeCount(@PathVariable UUID clientId) {
        long count = employeeService.getActiveEmployeeCountByClient(clientId);
        return ResponseEntity.ok(count);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update employee", description = "Updates an existing employee's information")
    public ResponseEntity<EmployeeResponseDTO> updateEmployee(
            @PathVariable UUID id,
            @Valid @RequestBody EmployeeRequestDTO request) {
        EmployeeResponseDTO response = employeeService.updateEmployee(id, request);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete employee", description = "Permanently deletes an employee record")
    public ResponseEntity<Void> deleteEmployee(@PathVariable UUID id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }
    
    @PatchMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate employee", description = "Marks an employee as inactive without deleting")
    public ResponseEntity<EmployeeResponseDTO> deactivateEmployee(@PathVariable UUID id) {
        EmployeeResponseDTO response = employeeService.deactivateEmployee(id);
        return ResponseEntity.ok(response);
    }
    
    @PatchMapping("/{id}/reactivate")
    @Operation(summary = "Reactivate employee", description = "Marks an inactive employee as active again")
    public ResponseEntity<EmployeeResponseDTO> reactivateEmployee(@PathVariable UUID id) {
        EmployeeResponseDTO response = employeeService.reactivateEmployee(id);
        return ResponseEntity.ok(response);
    }
}


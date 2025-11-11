package com.pm.billingservice.controller;

import com.pm.billingservice.service.ReportGenerationService;
import com.pm.billingservice.service.ReportGenerationService.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.UUID;

@RestController
@RequestMapping("/reports")
@Tag(name = "Report Generation", description = "APIs for generating various types of reports for stakeholders")
@CrossOrigin(origins = "*")
public class ReportController {
    
    @Autowired
    private ReportGenerationService reportGenerationService;
    
    @GetMapping("/client/{clientId}")
    @Operation(summary = "Generate Client Report", description = "Generates monthly summary report for a client showing all trips, costs, and vendor breakdowns")
    public ResponseEntity<ClientReport> generateClientReport(
            @PathVariable UUID clientId,
            @RequestParam(required = false) String month) {
        
        YearMonth reportMonth = month != null ? YearMonth.parse(month) : YearMonth.now();
        ClientReport report = reportGenerationService.generateClientReport(clientId, reportMonth);
        return ResponseEntity.ok(report);
    }
    
    @GetMapping("/vendor/{vendorId}/client/{clientId}")
    @Operation(summary = "Generate Vendor Report", description = "Generates payment statement for a vendor showing payable trips and incentives")
    public ResponseEntity<VendorReport> generateVendorReport(
            @PathVariable UUID vendorId,
            @PathVariable UUID clientId,
            @RequestParam(required = false) String month) {
        
        YearMonth reportMonth = month != null ? YearMonth.parse(month) : YearMonth.now();
        VendorReport report = reportGenerationService.generateVendorReport(vendorId, clientId, reportMonth);
        return ResponseEntity.ok(report);
    }
    
    @GetMapping("/employee/{employeeId}/client/{clientId}")
    @Operation(summary = "Generate Employee Report", description = "Generates incentive summary report for an employee showing earned incentives and trip details")
    public ResponseEntity<EmployeeReport> generateEmployeeReport(
            @PathVariable UUID employeeId,
            @PathVariable UUID clientId,
            @RequestParam(required = false) String month) {
        
        YearMonth reportMonth = month != null ? YearMonth.parse(month) : YearMonth.now();
        EmployeeReport report = reportGenerationService.generateEmployeeReport(employeeId, clientId, reportMonth);
        return ResponseEntity.ok(report);
    }
    
    @GetMapping("/consolidated")
    @Operation(summary = "Generate Consolidated Report", description = "Generates platform-wide consolidated report for MoveInSync management")
    public ResponseEntity<ConsolidatedReport> generateConsolidatedReport(
            @RequestParam(required = false) String month) {
        
        YearMonth reportMonth = month != null ? YearMonth.parse(month) : YearMonth.now();
        ConsolidatedReport report = reportGenerationService.generateConsolidatedReport(reportMonth);
        return ResponseEntity.ok(report);
    }
}


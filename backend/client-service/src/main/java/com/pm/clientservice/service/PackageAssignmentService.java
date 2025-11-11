package com.pm.clientservice.service;

import com.pm.clientservice.dto.PackageAssignmentRequestDTO;
import com.pm.clientservice.dto.PackageAssignmentResponseDTO;
import com.pm.clientservice.model.EmployeePackageAssignment;
import com.pm.clientservice.repository.EmployeePackageAssignmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class PackageAssignmentService {

    @Autowired
    private EmployeePackageAssignmentRepository assignmentRepository;

    public PackageAssignmentResponseDTO assignPackage(PackageAssignmentRequestDTO requestDTO, UUID assignedBy) {
        // Check if this package is already assigned to the employee
        if (assignmentRepository.existsByEmployeeIdAndPackageIdAndActive(
                requestDTO.getEmployeeId(), requestDTO.getPackageId(), true)) {
            throw new IllegalArgumentException("This package is already assigned to the employee");
        }

        EmployeePackageAssignment assignment = new EmployeePackageAssignment();
        assignment.setEmployeeId(requestDTO.getEmployeeId());
        assignment.setClientId(requestDTO.getClientId());
        assignment.setVendorId(requestDTO.getVendorId());
        assignment.setPackageId(requestDTO.getPackageId());
        assignment.setPackageName(requestDTO.getPackageName());
        assignment.setPackageType(requestDTO.getPackageType());
        assignment.setAssignedBy(assignedBy);
        assignment.setValidFrom(requestDTO.getValidFrom());
        assignment.setValidUntil(requestDTO.getValidUntil());
        assignment.setNotes(requestDTO.getNotes());
        assignment.setActive(true);

        EmployeePackageAssignment savedAssignment = assignmentRepository.save(assignment);
        return mapToResponseDTO(savedAssignment);
    }

    public List<PackageAssignmentResponseDTO> getEmployeeAssignments(UUID employeeId) {
        return assignmentRepository.findByEmployeeIdAndActive(employeeId, true).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<PackageAssignmentResponseDTO> getClientAssignments(UUID clientId) {
        return assignmentRepository.findByClientIdAndActive(clientId, true).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public void deactivateAssignment(Long assignmentId) {
        EmployeePackageAssignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));
        assignment.setActive(false);
        assignmentRepository.save(assignment);
    }

    public void deleteAssignment(Long assignmentId) {
        assignmentRepository.deleteById(assignmentId);
    }

    private PackageAssignmentResponseDTO mapToResponseDTO(EmployeePackageAssignment assignment) {
        PackageAssignmentResponseDTO dto = new PackageAssignmentResponseDTO();
        dto.setId(assignment.getId());
        dto.setEmployeeId(assignment.getEmployeeId());
        dto.setClientId(assignment.getClientId());
        dto.setVendorId(assignment.getVendorId());
        dto.setPackageId(assignment.getPackageId());
        dto.setPackageName(assignment.getPackageName());
        dto.setPackageType(assignment.getPackageType());
        dto.setAssignedBy(assignment.getAssignedBy());
        dto.setAssignedDate(assignment.getAssignedDate());
        dto.setValidFrom(assignment.getValidFrom());
        dto.setValidUntil(assignment.getValidUntil());
        dto.setActive(assignment.getActive());
        dto.setNotes(assignment.getNotes());
        dto.setCreatedAt(assignment.getCreatedAt());
        dto.setUpdatedAt(assignment.getUpdatedAt());
        return dto;
    }
}


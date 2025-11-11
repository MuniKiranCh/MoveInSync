package com.pm.employeeservice.repository;

import com.pm.employeeservice.model.EmployeePackageAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EmployeePackageAssignmentRepository extends JpaRepository<EmployeePackageAssignment, Long> {
    
    List<EmployeePackageAssignment> findByEmployeeId(UUID employeeId);
    
    List<EmployeePackageAssignment> findByEmployeeIdAndActive(UUID employeeId, Boolean active);
    
    List<EmployeePackageAssignment> findByClientId(UUID clientId);
    
    List<EmployeePackageAssignment> findByClientIdAndActive(UUID clientId, Boolean active);
    
    List<EmployeePackageAssignment> findByVendorId(UUID vendorId);
    
    boolean existsByEmployeeIdAndPackageIdAndActive(UUID employeeId, Long packageId, Boolean active);
}


package com.pm.clientservice.repository;

import com.pm.clientservice.model.EmployeePackageAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EmployeePackageAssignmentRepository extends JpaRepository<EmployeePackageAssignment, Long> {
    
    List<EmployeePackageAssignment> findByEmployeeIdAndActive(UUID employeeId, Boolean active);
    
    List<EmployeePackageAssignment> findByClientIdAndActive(UUID clientId, Boolean active);
    
    boolean existsByEmployeeIdAndPackageIdAndActive(UUID employeeId, Long packageId, Boolean active);
}


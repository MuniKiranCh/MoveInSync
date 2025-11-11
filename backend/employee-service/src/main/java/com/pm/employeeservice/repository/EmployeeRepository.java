package com.pm.employeeservice.repository;

import com.pm.employeeservice.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
    
    // Multi-tenant queries
    List<Employee> findByClientId(UUID clientId);
    
    List<Employee> findByClientIdAndActive(UUID clientId, Boolean active);
    
    Optional<Employee> findByIdAndClientId(UUID id, UUID clientId);
    
    Optional<Employee> findByEmail(String email);
    
    Optional<Employee> findByEmployeeCode(String employeeCode);
    
    boolean existsByEmail(String email);
    
    boolean existsByEmployeeCode(String employeeCode);
    
    @Query("SELECT COUNT(e) FROM Employee e WHERE e.clientId = :clientId AND e.active = true")
    long countActiveEmployeesByClient(UUID clientId);
    
    @Query("SELECT e FROM Employee e WHERE e.clientId = :clientId AND e.department = :department")
    List<Employee> findByClientIdAndDepartment(UUID clientId, String department);
}


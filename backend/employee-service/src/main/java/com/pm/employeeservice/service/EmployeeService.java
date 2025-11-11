package com.pm.employeeservice.service;

import com.pm.employeeservice.dto.EmployeeRequestDTO;
import com.pm.employeeservice.dto.EmployeeResponseDTO;
import com.pm.employeeservice.exception.EmailAlreadyExistsException;
import com.pm.employeeservice.exception.EmployeeCodeAlreadyExistsException;
import com.pm.employeeservice.exception.EmployeeNotFoundException;
import com.pm.employeeservice.mapper.EmployeeMapper;
import com.pm.employeeservice.model.Employee;
import com.pm.employeeservice.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmployeeService {
    
    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Autowired
    private EmployeeMapper employeeMapper;
    
    public EmployeeResponseDTO createEmployee(EmployeeRequestDTO request) {
        // Check if email already exists
        if (employeeRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Employee with email " + request.getEmail() + " already exists");
        }
        
        // Check if employee code already exists
        if (employeeRepository.existsByEmployeeCode(request.getEmployeeCode())) {
            throw new EmployeeCodeAlreadyExistsException("Employee with code " + request.getEmployeeCode() + " already exists");
        }
        
        Employee employee = employeeMapper.toEntity(request);
        Employee savedEmployee = employeeRepository.save(employee);
        return employeeMapper.toDTO(savedEmployee);
    }
    
    @Transactional(readOnly = true)
    public EmployeeResponseDTO getEmployeeById(UUID id) {
        Employee employee = employeeRepository.findById(id)
            .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + id));
        return employeeMapper.toDTO(employee);
    }
    
    @Transactional(readOnly = true)
    public EmployeeResponseDTO getEmployeeByClientIdAndId(UUID clientId, UUID id) {
        Employee employee = employeeRepository.findByIdAndClientId(id, clientId)
            .orElseThrow(() -> new EmployeeNotFoundException("Employee not found for client"));
        return employeeMapper.toDTO(employee);
    }
    
    @Transactional(readOnly = true)
    public List<EmployeeResponseDTO> getAllEmployees() {
        return employeeRepository.findAll()
            .stream()
            .map(employeeMapper::toDTO)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<EmployeeResponseDTO> getEmployeesByClientId(UUID clientId) {
        return employeeRepository.findByClientId(clientId)
            .stream()
            .map(employeeMapper::toDTO)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<EmployeeResponseDTO> getActiveEmployeesByClientId(UUID clientId) {
        return employeeRepository.findByClientIdAndActive(clientId, true)
            .stream()
            .map(employeeMapper::toDTO)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<EmployeeResponseDTO> getEmployeesByDepartment(UUID clientId, String department) {
        return employeeRepository.findByClientIdAndDepartment(clientId, department)
            .stream()
            .map(employeeMapper::toDTO)
            .collect(Collectors.toList());
    }
    
    public EmployeeResponseDTO updateEmployee(UUID id, EmployeeRequestDTO request) {
        Employee employee = employeeRepository.findById(id)
            .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + id));
        
        // Check if email is being changed and if new email already exists
        if (!employee.getEmail().equals(request.getEmail()) && 
            employeeRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Employee with email " + request.getEmail() + " already exists");
        }
        
        // Check if employee code is being changed and if new code already exists
        if (!employee.getEmployeeCode().equals(request.getEmployeeCode()) && 
            employeeRepository.existsByEmployeeCode(request.getEmployeeCode())) {
            throw new EmployeeCodeAlreadyExistsException("Employee with code " + request.getEmployeeCode() + " already exists");
        }
        
        employeeMapper.updateEntityFromDTO(request, employee);
        Employee updatedEmployee = employeeRepository.save(employee);
        return employeeMapper.toDTO(updatedEmployee);
    }
    
    public void deleteEmployee(UUID id) {
        if (!employeeRepository.existsById(id)) {
            throw new EmployeeNotFoundException("Employee not found with id: " + id);
        }
        employeeRepository.deleteById(id);
    }
    
    public EmployeeResponseDTO deactivateEmployee(UUID id) {
        Employee employee = employeeRepository.findById(id)
            .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + id));
        employee.setActive(false);
        Employee updatedEmployee = employeeRepository.save(employee);
        return employeeMapper.toDTO(updatedEmployee);
    }
    
    public EmployeeResponseDTO reactivateEmployee(UUID id) {
        Employee employee = employeeRepository.findById(id)
            .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + id));
        employee.setActive(true);
        Employee updatedEmployee = employeeRepository.save(employee);
        return employeeMapper.toDTO(updatedEmployee);
    }
    
    @Transactional(readOnly = true)
    public long getActiveEmployeeCountByClient(UUID clientId) {
        return employeeRepository.countActiveEmployeesByClient(clientId);
    }
    
    // Methods for incentive tracking (called from Trip Service or Incentive Service)
    public void addIncentiveToEmployee(UUID employeeId, BigDecimal incentiveAmount) {
        Employee employee = employeeRepository.findById(employeeId)
            .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + employeeId));
        employee.addIncentive(incentiveAmount);
        employeeRepository.save(employee);
    }
    
    public void incrementEmployeeTripCount(UUID employeeId, BigDecimal distance) {
        Employee employee = employeeRepository.findById(employeeId)
            .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + employeeId));
        employee.incrementTripCount();
        employee.addDistance(distance);
        employeeRepository.save(employee);
    }
}


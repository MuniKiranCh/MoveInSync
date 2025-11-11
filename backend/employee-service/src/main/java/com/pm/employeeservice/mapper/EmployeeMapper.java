package com.pm.employeeservice.mapper;

import com.pm.employeeservice.dto.EmployeeRequestDTO;
import com.pm.employeeservice.dto.EmployeeResponseDTO;
import com.pm.employeeservice.model.Employee;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {
    
    public Employee toEntity(EmployeeRequestDTO dto) {
        Employee employee = new Employee();
        employee.setClientId(dto.getClientId());
        employee.setEmployeeCode(dto.getEmployeeCode());
        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setEmail(dto.getEmail());
        employee.setPhoneNumber(dto.getPhoneNumber());
        employee.setDepartment(dto.getDepartment());
        employee.setDesignation(dto.getDesignation());
        employee.setHomeAddress(dto.getHomeAddress());
        employee.setDateOfJoining(dto.getDateOfJoining());
        employee.setDateOfBirth(dto.getDateOfBirth());
        employee.setActive(dto.getActive() != null ? dto.getActive() : true);
        return employee;
    }
    
    public EmployeeResponseDTO toDTO(Employee employee) {
        EmployeeResponseDTO dto = new EmployeeResponseDTO();
        dto.setId(employee.getId());
        dto.setClientId(employee.getClientId());
        dto.setEmployeeCode(employee.getEmployeeCode());
        dto.setFirstName(employee.getFirstName());
        dto.setLastName(employee.getLastName());
        dto.setFullName(employee.getFullName());
        dto.setEmail(employee.getEmail());
        dto.setPhoneNumber(employee.getPhoneNumber());
        dto.setDepartment(employee.getDepartment());
        dto.setDesignation(employee.getDesignation());
        dto.setHomeAddress(employee.getHomeAddress());
        dto.setDateOfJoining(employee.getDateOfJoining());
        dto.setDateOfBirth(employee.getDateOfBirth());
        dto.setActive(employee.getActive());
        dto.setTotalIncentivesEarned(employee.getTotalIncentivesEarned());
        dto.setTotalTripsCompleted(employee.getTotalTripsCompleted());
        dto.setTotalDistanceTraveled(employee.getTotalDistanceTraveled());
        dto.setCreatedAt(employee.getCreatedAt());
        dto.setUpdatedAt(employee.getUpdatedAt());
        return dto;
    }
    
    public void updateEntityFromDTO(EmployeeRequestDTO dto, Employee employee) {
        employee.setEmployeeCode(dto.getEmployeeCode());
        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setEmail(dto.getEmail());
        employee.setPhoneNumber(dto.getPhoneNumber());
        employee.setDepartment(dto.getDepartment());
        employee.setDesignation(dto.getDesignation());
        employee.setHomeAddress(dto.getHomeAddress());
        employee.setDateOfJoining(dto.getDateOfJoining());
        employee.setDateOfBirth(dto.getDateOfBirth());
        if (dto.getActive() != null) {
            employee.setActive(dto.getActive());
        }
    }
}


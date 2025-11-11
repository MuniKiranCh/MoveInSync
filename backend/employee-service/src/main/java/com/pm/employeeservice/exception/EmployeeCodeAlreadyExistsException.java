package com.pm.employeeservice.exception;

public class EmployeeCodeAlreadyExistsException extends RuntimeException {
    public EmployeeCodeAlreadyExistsException(String message) {
        super(message);
    }
}


package com.pm.clientservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ClientRequestDTO {

    @NotNull(message = "Client name is required")
    @Size(min = 2, max = 100, message = "Client name must be between 2 and 100 characters")
    private String name;

    @NotNull(message = "Client code is required")
    @Size(min = 3, max = 20, message = "Client code must be between 3 and 20 characters")
    private String code;

    @Size(max = 50, message = "Industry must not exceed 50 characters")
    private String industry;

    @Size(max = 500, message = "Address must not exceed 500 characters")
    private String address;

    @Email(message = "Contact email must be valid")
    private String contactEmail;

    @Size(max = 20, message = "Contact phone must not exceed 20 characters")
    private String contactPhone;

    @Size(max = 100, message = "Contact person name must not exceed 100 characters")
    private String contactPerson;

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }
}


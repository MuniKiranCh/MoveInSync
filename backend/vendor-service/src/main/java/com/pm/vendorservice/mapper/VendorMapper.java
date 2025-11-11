package com.pm.vendorservice.mapper;

import com.pm.vendorservice.dto.VendorRequestDTO;
import com.pm.vendorservice.dto.VendorResponseDTO;
import com.pm.vendorservice.model.Vendor;
import org.springframework.stereotype.Component;

@Component
public class VendorMapper {

    public Vendor toEntity(VendorRequestDTO requestDTO) {
        Vendor vendor = new Vendor();
        vendor.setName(requestDTO.getName());
        vendor.setCode(requestDTO.getCode());
        vendor.setServiceType(requestDTO.getServiceType());
        vendor.setAddress(requestDTO.getAddress());
        vendor.setContactEmail(requestDTO.getContactEmail());
        vendor.setContactPhone(requestDTO.getContactPhone());
        vendor.setContactPerson(requestDTO.getContactPerson());
        vendor.setBankAccountDetails(requestDTO.getBankAccountDetails());
        vendor.setTaxId(requestDTO.getTaxId());
        vendor.setActive(true);
        return vendor;
    }

    public VendorResponseDTO toResponseDTO(Vendor vendor) {
        VendorResponseDTO responseDTO = new VendorResponseDTO();
        responseDTO.setId(vendor.getId());
        responseDTO.setName(vendor.getName());
        responseDTO.setCode(vendor.getCode());
        responseDTO.setServiceType(vendor.getServiceType());
        responseDTO.setAddress(vendor.getAddress());
        responseDTO.setContactEmail(vendor.getContactEmail());
        responseDTO.setContactPhone(vendor.getContactPhone());
        responseDTO.setContactPerson(vendor.getContactPerson());
        responseDTO.setTaxId(vendor.getTaxId());
        responseDTO.setActive(vendor.getActive());
        responseDTO.setCreatedAt(vendor.getCreatedAt());
        responseDTO.setUpdatedAt(vendor.getUpdatedAt());
        return responseDTO;
    }
}


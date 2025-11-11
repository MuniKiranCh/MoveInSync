package com.pm.vendorservice.service;

import com.pm.vendorservice.dto.SubscriptionPackageResponseDTO;
import com.pm.vendorservice.dto.VendorRequestDTO;
import com.pm.vendorservice.dto.VendorResponseDTO;
import com.pm.vendorservice.dto.VendorWithPackagesDTO;
import com.pm.vendorservice.exception.VendorNotFoundException;
import com.pm.vendorservice.mapper.VendorMapper;
import com.pm.vendorservice.model.Vendor;
import com.pm.vendorservice.repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class VendorService {

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private VendorMapper vendorMapper;
    
    @Autowired
    private SubscriptionPackageService packageService;

    public VendorResponseDTO createVendor(VendorRequestDTO requestDTO) {
        if (vendorRepository.existsByCode(requestDTO.getCode())) {
            throw new IllegalArgumentException("Vendor with code " + requestDTO.getCode() + " already exists");
        }

        Vendor vendor = vendorMapper.toEntity(requestDTO);
        Vendor savedVendor = vendorRepository.save(vendor);
        return vendorMapper.toResponseDTO(savedVendor);
    }

    public VendorResponseDTO getVendorById(UUID id) {
        Vendor vendor = vendorRepository.findById(id)
                .orElseThrow(() -> new VendorNotFoundException("Vendor not found with id: " + id));
        return vendorMapper.toResponseDTO(vendor);
    }

    public VendorResponseDTO getVendorByCode(String code) {
        Vendor vendor = vendorRepository.findByCode(code)
                .orElseThrow(() -> new VendorNotFoundException("Vendor not found with code: " + code));
        return vendorMapper.toResponseDTO(vendor);
    }

    public List<VendorResponseDTO> getAllVendors() {
        return vendorRepository.findAll().stream()
                .map(vendorMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<VendorResponseDTO> getActiveVendors() {
        return vendorRepository.findByActive(true).stream()
                .map(vendorMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<VendorResponseDTO> getVendorsByClient(UUID clientId) {
        return vendorRepository.findByClientId(clientId).stream()
                .map(vendorMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<VendorResponseDTO> getActiveVendorsByClient(UUID clientId) {
        return vendorRepository.findByClientIdAndActive(clientId, true).stream()
                .map(vendorMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public VendorResponseDTO updateVendor(UUID id, VendorRequestDTO requestDTO) {
        Vendor existingVendor = vendorRepository.findById(id)
                .orElseThrow(() -> new VendorNotFoundException("Vendor not found with id: " + id));

        existingVendor.setName(requestDTO.getName());
        existingVendor.setClientId(requestDTO.getClientId());
        existingVendor.setServiceType(requestDTO.getServiceType());
        existingVendor.setAddress(requestDTO.getAddress());
        existingVendor.setContactEmail(requestDTO.getContactEmail());
        existingVendor.setContactPhone(requestDTO.getContactPhone());
        existingVendor.setContactPerson(requestDTO.getContactPerson());
        existingVendor.setBankAccountDetails(requestDTO.getBankAccountDetails());
        existingVendor.setTaxId(requestDTO.getTaxId());
        existingVendor.setGstNumber(requestDTO.getGstNumber());

        Vendor updatedVendor = vendorRepository.save(existingVendor);
        return vendorMapper.toResponseDTO(updatedVendor);
    }

    public void deactivateVendor(UUID id) {
        Vendor vendor = vendorRepository.findById(id)
                .orElseThrow(() -> new VendorNotFoundException("Vendor not found with id: " + id));
        vendor.setActive(false);
        vendorRepository.save(vendor);
    }

    public void deleteVendor(UUID id) {
        if (!vendorRepository.existsById(id)) {
            throw new VendorNotFoundException("Vendor not found with id: " + id);
        }
        vendorRepository.deleteById(id);
    }
    
    public VendorWithPackagesDTO getVendorWithPackages(UUID id) {
        Vendor vendor = vendorRepository.findById(id)
                .orElseThrow(() -> new VendorNotFoundException("Vendor not found with id: " + id));
        
        VendorWithPackagesDTO dto = new VendorWithPackagesDTO();
        dto.setId(vendor.getId());
        dto.setName(vendor.getName());
        dto.setCode(vendor.getCode());
        dto.setClientId(vendor.getClientId());
        dto.setServiceType(vendor.getServiceType());
        dto.setAddress(vendor.getAddress());
        dto.setContactEmail(vendor.getContactEmail());
        dto.setContactPhone(vendor.getContactPhone());
        dto.setContactPerson(vendor.getContactPerson());
        dto.setTaxId(vendor.getTaxId());
        dto.setActive(vendor.getActive());
        dto.setCreatedAt(vendor.getCreatedAt());
        dto.setUpdatedAt(vendor.getUpdatedAt());
        
        // Get all active packages for this vendor
        List<SubscriptionPackageResponseDTO> packages = packageService.getActivePackagesByVendorId(id);
        dto.setPackages(packages);
        
        return dto;
    }
    
    public List<VendorWithPackagesDTO> getAllVendorsWithPackages() {
        List<Vendor> vendors = vendorRepository.findAll();
        return vendors.stream()
                .map(vendor -> getVendorWithPackages(vendor.getId()))
                .collect(Collectors.toList());
    }
    
    public VendorResponseDTO getVendorByEmail(String email) {
        Vendor vendor = vendorRepository.findByContactEmail(email)
                .orElseThrow(() -> new VendorNotFoundException("Vendor not found with email: " + email));
        return vendorMapper.toResponseDTO(vendor);
    }
    
    public VendorWithPackagesDTO getVendorWithPackagesByEmail(String email) {
        Vendor vendor = vendorRepository.findByContactEmail(email)
                .orElseThrow(() -> new VendorNotFoundException("Vendor not found with email: " + email));
        return getVendorWithPackages(vendor.getId());
    }
    
    public List<VendorWithPackagesDTO> getClientVendorsWithPackages(UUID clientId) {
        List<Vendor> vendors = vendorRepository.findByClientIdAndActive(clientId, true);
        return vendors.stream()
                .map(vendor -> getVendorWithPackages(vendor.getId()))
                .collect(Collectors.toList());
    }
}


package com.pm.vendorservice.service;

import com.pm.vendorservice.dto.SubscriptionPackageRequestDTO;
import com.pm.vendorservice.dto.SubscriptionPackageResponseDTO;
import com.pm.vendorservice.model.Vendor;
import com.pm.vendorservice.model.VendorSubscriptionPackage;
import com.pm.vendorservice.repository.VendorRepository;
import com.pm.vendorservice.repository.VendorSubscriptionPackageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class SubscriptionPackageService {

    @Autowired
    private VendorSubscriptionPackageRepository packageRepository;

    @Autowired
    private VendorRepository vendorRepository;

    public SubscriptionPackageResponseDTO createPackage(SubscriptionPackageRequestDTO requestDTO) {
        if (packageRepository.existsByPackageCode(requestDTO.getPackageCode())) {
            throw new IllegalArgumentException("Package with code " + requestDTO.getPackageCode() + " already exists");
        }

        VendorSubscriptionPackage packageEntity = mapToEntity(requestDTO);
        VendorSubscriptionPackage savedPackage = packageRepository.save(packageEntity);
        return mapToResponseDTO(savedPackage);
    }

    public SubscriptionPackageResponseDTO getPackageById(Long id) {
        VendorSubscriptionPackage packageEntity = packageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Package not found with id: " + id));
        return mapToResponseDTO(packageEntity);
    }

    public SubscriptionPackageResponseDTO getPackageByCode(String code) {
        VendorSubscriptionPackage packageEntity = packageRepository.findByPackageCode(code)
                .orElseThrow(() -> new RuntimeException("Package not found with code: " + code));
        return mapToResponseDTO(packageEntity);
    }

    public List<SubscriptionPackageResponseDTO> getAllPackages() {
        return packageRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<SubscriptionPackageResponseDTO> getPackagesByVendorId(UUID vendorId) {
        return packageRepository.findByVendorId(vendorId).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<SubscriptionPackageResponseDTO> getActivePackagesByVendorId(UUID vendorId) {
        return packageRepository.findByVendorIdAndActive(vendorId, true).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public SubscriptionPackageResponseDTO updatePackage(Long id, SubscriptionPackageRequestDTO requestDTO) {
        VendorSubscriptionPackage packageEntity = packageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Package not found with id: " + id));

        updateEntityFromDTO(packageEntity, requestDTO);
        VendorSubscriptionPackage updatedPackage = packageRepository.save(packageEntity);
        return mapToResponseDTO(updatedPackage);
    }

    public void deletePackage(Long id) {
        packageRepository.deleteById(id);
    }

    public void deactivatePackage(Long id) {
        VendorSubscriptionPackage packageEntity = packageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Package not found with id: " + id));
        packageEntity.setActive(false);
        packageRepository.save(packageEntity);
    }
    
    public List<SubscriptionPackageResponseDTO> getActivePackagesByVendorEmail(String email) {
        Optional<Vendor> vendorOpt = vendorRepository.findByContactEmail(email);
        if (vendorOpt.isEmpty()) {
            return new ArrayList<>();
        }
        return getActivePackagesByVendorId(vendorOpt.get().getId());
    }

    // Helper methods
    private VendorSubscriptionPackage mapToEntity(SubscriptionPackageRequestDTO dto) {
        VendorSubscriptionPackage entity = new VendorSubscriptionPackage();
        entity.setVendorId(dto.getVendorId());
        entity.setPackageCode(dto.getPackageCode());
        entity.setPackageName(dto.getPackageName());
        entity.setDescription(dto.getDescription());
        entity.setPackageType(dto.getPackageType());
        entity.setMonthlyRate(dto.getMonthlyRate());
        entity.setTripsIncluded(dto.getTripsIncluded());
        entity.setKmsIncluded(dto.getKmsIncluded());
        entity.setRatePerTrip(dto.getRatePerTrip());
        entity.setRatePerKm(dto.getRatePerKm());
        entity.setExtraTripRate(dto.getExtraTripRate());
        entity.setExtraKmRate(dto.getExtraKmRate());
        entity.setExtraHourRate(dto.getExtraHourRate());
        entity.setStandardTripKm(dto.getStandardTripKm());
        entity.setStandardTripHours(dto.getStandardTripHours());
        entity.setPeakHourMultiplier(dto.getPeakHourMultiplier());
        entity.setActive(true);
        return entity;
    }

    private void updateEntityFromDTO(VendorSubscriptionPackage entity, SubscriptionPackageRequestDTO dto) {
        entity.setPackageName(dto.getPackageName());
        entity.setDescription(dto.getDescription());
        entity.setPackageType(dto.getPackageType());
        entity.setMonthlyRate(dto.getMonthlyRate());
        entity.setTripsIncluded(dto.getTripsIncluded());
        entity.setKmsIncluded(dto.getKmsIncluded());
        entity.setRatePerTrip(dto.getRatePerTrip());
        entity.setRatePerKm(dto.getRatePerKm());
        entity.setExtraTripRate(dto.getExtraTripRate());
        entity.setExtraKmRate(dto.getExtraKmRate());
        entity.setExtraHourRate(dto.getExtraHourRate());
        entity.setStandardTripKm(dto.getStandardTripKm());
        entity.setStandardTripHours(dto.getStandardTripHours());
        entity.setPeakHourMultiplier(dto.getPeakHourMultiplier());
    }

    private SubscriptionPackageResponseDTO mapToResponseDTO(VendorSubscriptionPackage entity) {
        SubscriptionPackageResponseDTO dto = new SubscriptionPackageResponseDTO();
        dto.setId(entity.getId());
        dto.setVendorId(entity.getVendorId());
        
        // Get vendor name
        vendorRepository.findById(entity.getVendorId()).ifPresent(vendor -> {
            dto.setVendorName(vendor.getName());
        });
        
        dto.setPackageCode(entity.getPackageCode());
        dto.setPackageName(entity.getPackageName());
        dto.setDescription(entity.getDescription());
        dto.setPackageType(entity.getPackageType());
        dto.setMonthlyRate(entity.getMonthlyRate());
        dto.setTripsIncluded(entity.getTripsIncluded());
        dto.setKmsIncluded(entity.getKmsIncluded());
        dto.setRatePerTrip(entity.getRatePerTrip());
        dto.setRatePerKm(entity.getRatePerKm());
        dto.setExtraTripRate(entity.getExtraTripRate());
        dto.setExtraKmRate(entity.getExtraKmRate());
        dto.setExtraHourRate(entity.getExtraHourRate());
        dto.setStandardTripKm(entity.getStandardTripKm());
        dto.setStandardTripHours(entity.getStandardTripHours());
        dto.setPeakHourMultiplier(entity.getPeakHourMultiplier());
        dto.setActive(entity.getActive());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }
}


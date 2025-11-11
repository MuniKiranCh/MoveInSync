package com.pm.vendorservice.repository;

import com.pm.vendorservice.model.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, UUID> {
    
    Optional<Vendor> findByCode(String code);
    
    Optional<Vendor> findByContactEmail(String contactEmail);
    
    List<Vendor> findByActive(Boolean active);
    
    List<Vendor> findByClientId(UUID clientId);
    
    List<Vendor> findByClientIdAndActive(UUID clientId, Boolean active);
    
    boolean existsByCode(String code);
}


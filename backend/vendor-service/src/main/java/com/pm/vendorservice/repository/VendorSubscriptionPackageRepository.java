package com.pm.vendorservice.repository;

import com.pm.vendorservice.model.VendorSubscriptionPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VendorSubscriptionPackageRepository extends JpaRepository<VendorSubscriptionPackage, Long> {
    
    List<VendorSubscriptionPackage> findByVendorId(UUID vendorId);
    
    List<VendorSubscriptionPackage> findByVendorIdAndActive(UUID vendorId, Boolean active);
    
    Optional<VendorSubscriptionPackage> findByPackageCode(String packageCode);
    
    boolean existsByPackageCode(String packageCode);
}


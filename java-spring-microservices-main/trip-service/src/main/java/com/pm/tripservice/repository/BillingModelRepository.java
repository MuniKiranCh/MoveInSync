package com.pm.tripservice.repository;

import com.pm.tripservice.model.BillingModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BillingModelRepository extends JpaRepository<BillingModel, UUID> {
    
    List<BillingModel> findByClientId(UUID clientId);
    
    List<BillingModel> findByVendorId(UUID vendorId);
    
    Optional<BillingModel> findByClientIdAndVendorId(UUID clientId, UUID vendorId);
    
    List<BillingModel> findByClientIdAndActive(UUID clientId, Boolean active);
    
    List<BillingModel> findByVendorIdAndActive(UUID vendorId, Boolean active);
}


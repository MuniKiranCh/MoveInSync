package com.pm.tripservice.repository;

import com.pm.tripservice.model.Trip;
import com.pm.tripservice.model.TripStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public interface TripRepository extends JpaRepository<Trip, UUID> {
    
    List<Trip> findByClientId(UUID clientId);
    
    List<Trip> findByEmployeeId(UUID employeeId);
    
    List<Trip> findByVendorId(UUID vendorId);
    
    List<Trip> findByStatus(TripStatus status);
    
    @Query("SELECT t FROM Trip t WHERE t.clientId = :clientId " +
           "AND t.vendorId = :vendorId " +
           "AND t.tripStartTime >= :startDate " +
           "AND t.tripStartTime < :endDate")
    List<Trip> findByClientVendorAndDateRange(
        @Param("clientId") UUID clientId,
        @Param("vendorId") UUID vendorId,
        @Param("startDate") Instant startDate,
        @Param("endDate") Instant endDate
    );
    
    @Query("SELECT t FROM Trip t WHERE t.employeeId = :employeeId " +
           "AND t.tripStartTime >= :startDate " +
           "AND t.tripStartTime < :endDate")
    List<Trip> findByEmployeeAndDateRange(
        @Param("employeeId") UUID employeeId,
        @Param("startDate") Instant startDate,
        @Param("endDate") Instant endDate
    );
    
    @Query("SELECT COUNT(t) FROM Trip t WHERE t.clientId = :clientId " +
           "AND t.tripStartTime >= :startOfMonth " +
           "AND t.tripStartTime < :endOfMonth")
    Integer countByClientAndMonth(
        @Param("clientId") UUID clientId,
        @Param("startOfMonth") Instant startOfMonth,
        @Param("endOfMonth") Instant endOfMonth
    );
    
    @Query("SELECT t FROM Trip t WHERE t.clientId = :clientId " +
           "AND t.billed = false")
    List<Trip> findUnbilledTripsByClient(@Param("clientId") UUID clientId);
    
    @Query("SELECT t FROM Trip t WHERE t.status = 'COMPLETED' AND t.billed = false")
    List<Trip> findCompletedUnbilledTrips();
}


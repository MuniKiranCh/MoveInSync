package com.pm.tripservice.service;

import com.pm.tripservice.dto.TripRequestDTO;
import com.pm.tripservice.dto.TripResponseDTO;
import com.pm.tripservice.exception.TripNotFoundException;
import com.pm.tripservice.mapper.TripMapper;
import com.pm.tripservice.model.Trip;
import com.pm.tripservice.model.TripStatus;
import com.pm.tripservice.repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class TripService {

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private TripMapper tripMapper;

    public TripResponseDTO createTrip(TripRequestDTO requestDTO) {
        // Validate employee ID (optional but recommended - call Employee Service to verify)
        // Note: In production, you would call Employee Service REST API to validate:
        // 1. Employee exists
        // 2. Employee belongs to the specified clientId
        // 3. Employee is active
        // Example validation (commented as it requires REST call to Employee Service):
        /*
        ResponseEntity<EmployeeDTO> employeeResponse = restTemplate.getForEntity(
            "http://localhost:4035/employees/" + requestDTO.getEmployeeId(), 
            EmployeeDTO.class
        );
        if (!employeeResponse.getStatusCode().is2xxSuccessful()) {
            throw new IllegalArgumentException("Employee not found with ID: " + requestDTO.getEmployeeId());
        }
        if (!employeeResponse.getBody().getClientId().equals(requestDTO.getClientId())) {
            throw new IllegalArgumentException("Employee does not belong to the specified client");
        }
        */
        
        Trip trip = tripMapper.toEntity(requestDTO);
        Trip savedTrip = tripRepository.save(trip);
        return tripMapper.toResponseDTO(savedTrip);
    }

    public TripResponseDTO getTripById(UUID id) {
        Trip trip = tripRepository.findById(id)
                .orElseThrow(() -> new TripNotFoundException("Trip not found with id: " + id));
        return tripMapper.toResponseDTO(trip);
    }

    public List<TripResponseDTO> getAllTrips() {
        return tripRepository.findAll().stream()
                .map(tripMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<TripResponseDTO> getTripsByClient(UUID clientId) {
        return tripRepository.findByClientId(clientId).stream()
                .map(tripMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<TripResponseDTO> getTripsByEmployee(UUID employeeId) {
        return tripRepository.findByEmployeeId(employeeId).stream()
                .map(tripMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<TripResponseDTO> getTripsByVendor(UUID vendorId) {
        return tripRepository.findByVendorId(vendorId).stream()
                .map(tripMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<TripResponseDTO> getTripsByClientVendorAndDateRange(
            UUID clientId, UUID vendorId, LocalDate startDate, LocalDate endDate) {
        
        Instant startInstant = startDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant endInstant = endDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant();
        
        return tripRepository.findByClientVendorAndDateRange(clientId, vendorId, startInstant, endInstant)
                .stream()
                .map(tripMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<TripResponseDTO> getTripsByEmployeeAndDateRange(
            UUID employeeId, LocalDate startDate, LocalDate endDate) {
        
        Instant startInstant = startDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant endInstant = endDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant();
        
        return tripRepository.findByEmployeeAndDateRange(employeeId, startInstant, endInstant)
                .stream()
                .map(tripMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public TripResponseDTO updateTrip(UUID id, TripRequestDTO requestDTO) {
        Trip existingTrip = tripRepository.findById(id)
                .orElseThrow(() -> new TripNotFoundException("Trip not found with id: " + id));

        // Update fields
        if (requestDTO.getVehicleNumber() != null) {
            existingTrip.setVehicleNumber(requestDTO.getVehicleNumber());
        }
        if (requestDTO.getDriverName() != null) {
            existingTrip.setDriverName(requestDTO.getDriverName());
        }
        if (requestDTO.getDriverPhone() != null) {
            existingTrip.setDriverPhone(requestDTO.getDriverPhone());
        }
        if (requestDTO.getTripEndTime() != null) {
            existingTrip.setTripEndTime(requestDTO.getTripEndTime());
        }
        if (requestDTO.getDropLocation() != null) {
            existingTrip.setDropLocation(requestDTO.getDropLocation());
        }
        if (requestDTO.getDistanceKm() != null) {
            existingTrip.setDistanceKm(requestDTO.getDistanceKm());
        }
        if (requestDTO.getStatus() != null) {
            existingTrip.setStatus(requestDTO.getStatus());
        }
        if (requestDTO.getNotes() != null) {
            existingTrip.setNotes(requestDTO.getNotes());
        }

        Trip updatedTrip = tripRepository.save(existingTrip);
        return tripMapper.toResponseDTO(updatedTrip);
    }

    public TripResponseDTO completeTrip(UUID id, BigDecimal distance, String dropLocation) {
        Trip trip = tripRepository.findById(id)
                .orElseThrow(() -> new TripNotFoundException("Trip not found with id: " + id));

        trip.completeTrip(Instant.now(), distance, dropLocation);
        Trip completedTrip = tripRepository.save(trip);
        return tripMapper.toResponseDTO(completedTrip);
    }

    public TripResponseDTO cancelTrip(UUID id, String reason) {
        Trip trip = tripRepository.findById(id)
                .orElseThrow(() -> new TripNotFoundException("Trip not found with id: " + id));

        trip.setStatus(TripStatus.CANCELLED);
        if (reason != null) {
            trip.setNotes(trip.getNotes() != null ? 
                trip.getNotes() + "; Cancelled: " + reason : "Cancelled: " + reason);
        }

        Trip cancelledTrip = tripRepository.save(trip);
        return tripMapper.toResponseDTO(cancelledTrip);
    }

    public void deleteTrip(UUID id) {
        if (!tripRepository.existsById(id)) {
            throw new TripNotFoundException("Trip not found with id: " + id);
        }
        tripRepository.deleteById(id);
    }

    public List<TripResponseDTO> getUnbilledTripsByClient(UUID clientId) {
        return tripRepository.findUnbilledTripsByClient(clientId).stream()
                .map(tripMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public Integer countTripsByClientAndMonth(UUID clientId, LocalDate month) {
        LocalDate startOfMonth = month.withDayOfMonth(1);
        LocalDate endOfMonth = month.withDayOfMonth(month.lengthOfMonth());
        
        Instant startInstant = startOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant endInstant = endOfMonth.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant();
        
        return tripRepository.countByClientAndMonth(clientId, startInstant, endInstant);
    }
}


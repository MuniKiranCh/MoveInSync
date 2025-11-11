package com.pm.tripservice.mapper;

import com.pm.tripservice.dto.TripRequestDTO;
import com.pm.tripservice.dto.TripResponseDTO;
import com.pm.tripservice.model.Trip;
import org.springframework.stereotype.Component;

@Component
public class TripMapper {

    public Trip toEntity(TripRequestDTO requestDTO) {
        Trip trip = new Trip();
        trip.setClientId(requestDTO.getClientId());
        trip.setVendorId(requestDTO.getVendorId());
        trip.setEmployeeId(requestDTO.getEmployeeId());
        trip.setVehicleNumber(requestDTO.getVehicleNumber());
        trip.setDriverName(requestDTO.getDriverName());
        trip.setDriverPhone(requestDTO.getDriverPhone());
        trip.setTripStartTime(requestDTO.getTripStartTime());
        trip.setTripEndTime(requestDTO.getTripEndTime());
        trip.setPickupLocation(requestDTO.getPickupLocation());
        trip.setDropLocation(requestDTO.getDropLocation());
        trip.setDistanceKm(requestDTO.getDistanceKm());
        trip.setTripType(requestDTO.getTripType());
        trip.setNotes(requestDTO.getNotes());
        
        if (requestDTO.getStatus() != null) {
            trip.setStatus(requestDTO.getStatus());
        }
        
        return trip;
    }

    public TripResponseDTO toResponseDTO(Trip trip) {
        TripResponseDTO responseDTO = new TripResponseDTO();
        responseDTO.setId(trip.getId());
        responseDTO.setClientId(trip.getClientId());
        responseDTO.setVendorId(trip.getVendorId());
        responseDTO.setEmployeeId(trip.getEmployeeId());
        responseDTO.setVehicleNumber(trip.getVehicleNumber());
        responseDTO.setDriverName(trip.getDriverName());
        responseDTO.setDriverPhone(trip.getDriverPhone());
        responseDTO.setTripStartTime(trip.getTripStartTime());
        responseDTO.setTripEndTime(trip.getTripEndTime());
        responseDTO.setPickupLocation(trip.getPickupLocation());
        responseDTO.setDropLocation(trip.getDropLocation());
        responseDTO.setDistanceKm(trip.getDistanceKm());
        responseDTO.setDurationHours(trip.getDurationHours());
        responseDTO.setTripType(trip.getTripType());
        responseDTO.setStatus(trip.getStatus());
        responseDTO.setBaseCost(trip.getBaseCost());
        responseDTO.setExtraKmCost(trip.getExtraKmCost());
        responseDTO.setExtraHourCost(trip.getExtraHourCost());
        responseDTO.setTotalCost(trip.getTotalCost());
        responseDTO.setBilled(trip.getBilled());
        responseDTO.setNotes(trip.getNotes());
        responseDTO.setCreatedAt(trip.getCreatedAt());
        responseDTO.setUpdatedAt(trip.getUpdatedAt());
        return responseDTO;
    }
}


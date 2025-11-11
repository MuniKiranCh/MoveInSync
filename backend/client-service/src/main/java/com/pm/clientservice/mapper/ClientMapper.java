package com.pm.clientservice.mapper;

import com.pm.clientservice.dto.ClientRequestDTO;
import com.pm.clientservice.dto.ClientResponseDTO;
import com.pm.clientservice.model.Client;
import org.springframework.stereotype.Component;

@Component
public class ClientMapper {

    public Client toEntity(ClientRequestDTO requestDTO) {
        Client client = new Client();
        client.setName(requestDTO.getName());
        client.setCode(requestDTO.getCode());
        client.setIndustry(requestDTO.getIndustry());
        client.setAddress(requestDTO.getAddress());
        client.setContactEmail(requestDTO.getContactEmail());
        client.setContactPhone(requestDTO.getContactPhone());
        client.setContactPerson(requestDTO.getContactPerson());
        client.setActive(true);
        return client;
    }

    public ClientResponseDTO toResponseDTO(Client client) {
        ClientResponseDTO responseDTO = new ClientResponseDTO();
        responseDTO.setId(client.getId());
        responseDTO.setName(client.getName());
        responseDTO.setCode(client.getCode());
        responseDTO.setIndustry(client.getIndustry());
        responseDTO.setAddress(client.getAddress());
        responseDTO.setContactEmail(client.getContactEmail());
        responseDTO.setContactPhone(client.getContactPhone());
        responseDTO.setContactPerson(client.getContactPerson());
        responseDTO.setActive(client.getActive());
        responseDTO.setCreatedAt(client.getCreatedAt());
        responseDTO.setUpdatedAt(client.getUpdatedAt());
        return responseDTO;
    }
}


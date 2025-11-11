package com.pm.clientservice.service;

import com.pm.clientservice.dto.ClientRequestDTO;
import com.pm.clientservice.dto.ClientResponseDTO;
import com.pm.clientservice.exception.ClientNotFoundException;
import com.pm.clientservice.mapper.ClientMapper;
import com.pm.clientservice.model.Client;
import com.pm.clientservice.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ClientMapper clientMapper;

    public ClientResponseDTO createClient(ClientRequestDTO requestDTO) {
        if (clientRepository.existsByCode(requestDTO.getCode())) {
            throw new IllegalArgumentException("Client with code " + requestDTO.getCode() + " already exists");
        }

        Client client = clientMapper.toEntity(requestDTO);
        Client savedClient = clientRepository.save(client);
        return clientMapper.toResponseDTO(savedClient);
    }

    public ClientResponseDTO getClientById(UUID id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("Client not found with id: " + id));
        return clientMapper.toResponseDTO(client);
    }

    public ClientResponseDTO getClientByCode(String code) {
        Client client = clientRepository.findByCode(code)
                .orElseThrow(() -> new ClientNotFoundException("Client not found with code: " + code));
        return clientMapper.toResponseDTO(client);
    }

    public List<ClientResponseDTO> getAllClients() {
        return clientRepository.findAll().stream()
                .map(clientMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<ClientResponseDTO> getActiveClients() {
        return clientRepository.findByActive(true).stream()
                .map(clientMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public ClientResponseDTO updateClient(UUID id, ClientRequestDTO requestDTO) {
        Client existingClient = clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("Client not found with id: " + id));

        existingClient.setName(requestDTO.getName());
        existingClient.setIndustry(requestDTO.getIndustry());
        existingClient.setAddress(requestDTO.getAddress());
        existingClient.setContactEmail(requestDTO.getContactEmail());
        existingClient.setContactPhone(requestDTO.getContactPhone());
        existingClient.setContactPerson(requestDTO.getContactPerson());

        Client updatedClient = clientRepository.save(existingClient);
        return clientMapper.toResponseDTO(updatedClient);
    }

    public void deactivateClient(UUID id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("Client not found with id: " + id));
        client.setActive(false);
        clientRepository.save(client);
    }

    public void deleteClient(UUID id) {
        if (!clientRepository.existsById(id)) {
            throw new ClientNotFoundException("Client not found with id: " + id);
        }
        clientRepository.deleteById(id);
    }
}


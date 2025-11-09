package com.pm.clientservice.controller;

import com.pm.clientservice.dto.ClientRequestDTO;
import com.pm.clientservice.dto.ClientResponseDTO;
import com.pm.clientservice.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/clients")
@Tag(name = "Client", description = "Client organization management endpoints")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @PostMapping
    @Operation(summary = "Create a new client")
    public ResponseEntity<ClientResponseDTO> createClient(@Valid @RequestBody ClientRequestDTO requestDTO) {
        ClientResponseDTO response = clientService.createClient(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get client by ID")
    public ResponseEntity<ClientResponseDTO> getClientById(@PathVariable UUID id) {
        ClientResponseDTO response = clientService.getClientById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/code/{code}")
    @Operation(summary = "Get client by code")
    public ResponseEntity<ClientResponseDTO> getClientByCode(@PathVariable String code) {
        ClientResponseDTO response = clientService.getClientByCode(code);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Get all clients")
    public ResponseEntity<List<ClientResponseDTO>> getAllClients() {
        List<ClientResponseDTO> clients = clientService.getAllClients();
        return ResponseEntity.ok(clients);
    }

    @GetMapping("/active")
    @Operation(summary = "Get all active clients")
    public ResponseEntity<List<ClientResponseDTO>> getActiveClients() {
        List<ClientResponseDTO> clients = clientService.getActiveClients();
        return ResponseEntity.ok(clients);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update client")
    public ResponseEntity<ClientResponseDTO> updateClient(
            @PathVariable UUID id,
            @Valid @RequestBody ClientRequestDTO requestDTO) {
        ClientResponseDTO response = clientService.updateClient(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate client")
    public ResponseEntity<Void> deactivateClient(@PathVariable UUID id) {
        clientService.deactivateClient(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete client")
    public ResponseEntity<Void> deleteClient(@PathVariable UUID id) {
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }
}


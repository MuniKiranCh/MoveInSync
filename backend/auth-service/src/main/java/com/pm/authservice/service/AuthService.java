package com.pm.authservice.service;

import com.pm.authservice.dto.LoginRequestDTO;
import com.pm.authservice.dto.LoginResponseDTO;
import com.pm.authservice.dto.RegisterRequestDTO;
import com.pm.authservice.model.User;
import com.pm.authservice.model.UserRole;
import com.pm.authservice.util.JwtUtil;
import io.jsonwebtoken.JwtException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthService {

  private final UserService userService;
  private final PasswordEncoder passwordEncoder;
  private final JwtUtil jwtUtil;
  private final RestTemplate restTemplate;
  
  @Value("${client.service.url:http://localhost:4010}")
  private String clientServiceUrl;

  public AuthService(UserService userService, PasswordEncoder passwordEncoder,
      JwtUtil jwtUtil) {
    this.userService = userService;
    this.passwordEncoder = passwordEncoder;
    this.jwtUtil = jwtUtil;
    this.restTemplate = new RestTemplate();
  }

  public Optional<LoginResponseDTO> register(RegisterRequestDTO registerRequestDTO) {
    // Check if user already exists
    if (userService.findByEmail(registerRequestDTO.getEmail()).isPresent()) {
      return Optional.empty();
    }

    // Step 1: Create Client record in client-service
    UUID clientId = createClientRecord(registerRequestDTO);
    if (clientId == null) {
      // Failed to create client record
      return Optional.empty();
    }

    // Step 2: Create CLIENT role user linked to the new client
    User user = new User();
    user.setEmail(registerRequestDTO.getEmail());
    user.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
    user.setFirstName(registerRequestDTO.getFirstName());
    user.setLastName(registerRequestDTO.getLastName());
    user.setRole(UserRole.CLIENT); // Always CLIENT role for company registration
    user.setTenantId(clientId); // Link to the created client
    user.setActive(true);

    // Save user
    User savedUser = userService.save(user);

    // Generate token
    String token = jwtUtil.generateToken(savedUser);
    String name = savedUser.getFirstName() + " " + savedUser.getLastName();

    return Optional.of(new LoginResponseDTO(
        token,
        savedUser.getRole().toString(),
        savedUser.getTenantId(),
        savedUser.getId(),
        name
    ));
  }

  private UUID createClientRecord(RegisterRequestDTO registerRequestDTO) {
    try {
      // Prepare client data
      Map<String, Object> clientData = new HashMap<>();
      clientData.put("name", registerRequestDTO.getCompanyName());
      clientData.put("code", generateClientCode(registerRequestDTO.getCompanyName()));
      clientData.put("industry", registerRequestDTO.getIndustry());
      clientData.put("address", registerRequestDTO.getAddress());
      clientData.put("contactEmail", registerRequestDTO.getEmail());
      clientData.put("contactPhone", registerRequestDTO.getContactPhone());
      clientData.put("contactPerson", registerRequestDTO.getFirstName() + " " + registerRequestDTO.getLastName());

      // Set headers
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);

      // Make REST call to client-service
      HttpEntity<Map<String, Object>> request = new HttpEntity<>(clientData, headers);
      ResponseEntity<Map> response = restTemplate.postForEntity(
          clientServiceUrl + "/clients",
          request,
          Map.class
      );

      if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
        String clientIdStr = (String) response.getBody().get("id");
        return UUID.fromString(clientIdStr);
      }
      
      return null;
    } catch (Exception e) {
      System.err.println("Failed to create client record: " + e.getMessage());
      e.printStackTrace();
      return null;
    }
  }

  private String generateClientCode(String companyName) {
    // Generate a unique code from company name
    String baseCode = companyName.replaceAll("[^A-Za-z0-9]", "")
        .toUpperCase()
        .substring(0, Math.min(companyName.length(), 6));
    return baseCode + String.format("%03d", (int)(Math.random() * 1000));
  }

  public Optional<LoginResponseDTO> authenticate(LoginRequestDTO loginRequestDTO) {
    return userService.findByEmail(loginRequestDTO.getEmail())
        .filter(u -> passwordEncoder.matches(loginRequestDTO.getPassword(), u.getPassword()))
        .filter(User::getActive)
        .map(u -> {
          String token = jwtUtil.generateToken(u);
          String name = (u.getFirstName() != null && u.getLastName() != null) 
              ? u.getFirstName() + " " + u.getLastName() 
              : u.getEmail();
          
          return new LoginResponseDTO(
              token,
              u.getRole().toString(),
              u.getTenantId(),
              u.getId(),
              name
          );
        });
  }

  public boolean validateToken(String token) {
    try {
      jwtUtil.validateToken(token);
      return true;
    } catch (JwtException e){
      return false;
    }
  }
}

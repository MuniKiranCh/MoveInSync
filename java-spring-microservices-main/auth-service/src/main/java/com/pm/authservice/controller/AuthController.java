package com.pm.authservice.controller;

import com.pm.authservice.dto.LoginRequestDTO;
import com.pm.authservice.dto.LoginResponseDTO;
import com.pm.authservice.dto.RegisterRequestDTO;
import com.pm.authservice.model.User;
import com.pm.authservice.model.UserRole;
import com.pm.authservice.service.AuthService;
import com.pm.authservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

  private final AuthService authService;
  private final UserService userService;
  private final PasswordEncoder passwordEncoder;

  public AuthController(AuthService authService, UserService userService, PasswordEncoder passwordEncoder) {
    this.authService = authService;
    this.userService = userService;
    this.passwordEncoder = passwordEncoder;
  }

  @Operation(summary = "Register a new company")
  @PostMapping("/register")
  public ResponseEntity<LoginResponseDTO> register(
      @RequestBody RegisterRequestDTO registerRequestDTO) {

    Optional<LoginResponseDTO> responseOptional = authService.register(registerRequestDTO);

    if (responseOptional.isEmpty()) {
      return ResponseEntity.status(HttpStatus.CONFLICT).build(); // User already exists
    }

    return ResponseEntity.status(HttpStatus.CREATED).body(responseOptional.get());
  }

  @Operation(summary = "Create a new user (Admin/Vendor/Employee)")
  @PostMapping("/create-user")
  public ResponseEntity<?> createUser(@RequestBody Map<String, String> request) {
    try {
      String email = request.get("email");
      String password = request.get("password");
      String firstName = request.get("firstName");
      String lastName = request.get("lastName");
      String role = request.get("role");
      String tenantIdStr = request.get("tenantId");
      String vendorIdStr = request.get("vendorId");
      String phone = request.get("phone");
      String employeeId = request.get("employeeId");
      String department = request.get("department");

      // Check if user already exists
      if (userService.findByEmail(email).isPresent()) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(Map.of("error", "User with this email already exists"));
      }

      // Create new user
      User user = new User();
      user.setEmail(email);
      user.setPassword(passwordEncoder.encode(password));
      user.setFirstName(firstName);
      user.setLastName(lastName);
      user.setRole(UserRole.valueOf(role));
      user.setTenantId(UUID.fromString(tenantIdStr));
      
      if (vendorIdStr != null && !vendorIdStr.isEmpty()) {
        user.setVendorId(UUID.fromString(vendorIdStr));
      }
      
      if (phone != null && !phone.isEmpty()) {
        user.setPhone(phone);
      }
      
      if (employeeId != null && !employeeId.isEmpty()) {
        user.setEmployeeId(employeeId);
      }
      
      if (department != null && !department.isEmpty()) {
        user.setDepartment(department);
      }
      
      user.setActive(true);

      User savedUser = userService.save(user);

      return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
          "message", "User created successfully",
          "userId", savedUser.getId().toString(),
          "email", savedUser.getEmail(),
          "role", savedUser.getRole().toString()
      ));

    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(Map.of("error", "Failed to create user: " + e.getMessage()));
    }
  }

  @Operation(summary = "Generate token on user login")
  @PostMapping("/login")
  public ResponseEntity<LoginResponseDTO> login(
      @RequestBody LoginRequestDTO loginRequestDTO) {

    Optional<LoginResponseDTO> responseOptional = authService.authenticate(loginRequestDTO);

    if (responseOptional.isEmpty()) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    return ResponseEntity.ok(responseOptional.get());
  }

  @Operation(summary = "Validate Token")
  @GetMapping("/validate")
  public ResponseEntity<Void> validateToken(
      @RequestHeader("Authorization") String authHeader) {

    // Authorization: Bearer <token>
    if(authHeader == null || !authHeader.startsWith("Bearer ")) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    return authService.validateToken(authHeader.substring(7))
        ? ResponseEntity.ok().build()
        : ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
  }
  
  @Operation(summary = "Get all users")
  @GetMapping("/users")
  public ResponseEntity<List<User>> getAllUsers() {
    try {
      List<User> users = userService.findAll();
      return ResponseEntity.ok(users);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }
  
  @Operation(summary = "Get users by tenant ID")
  @GetMapping("/users/tenant/{tenantId}")
  public ResponseEntity<List<User>> getUsersByTenant(@PathVariable String tenantId) {
    try {
      UUID tenantUuid = UUID.fromString(tenantId);
      List<User> users = userService.findByTenantId(tenantUuid);
      return ResponseEntity.ok(users);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }
  
  @Operation(summary = "Update user details")
  @PutMapping("/users/{userId}")
  public ResponseEntity<?> updateUser(@PathVariable String userId, @RequestBody Map<String, String> request) {
    try {
      UUID userUuid = UUID.fromString(userId);
      Optional<User> userOptional = userService.findById(userUuid);
      
      if (userOptional.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(Map.of("error", "User not found"));
      }
      
      User user = userOptional.get();
      
      // Update fields if provided
      if (request.containsKey("firstName") && request.get("firstName") != null) {
        user.setFirstName(request.get("firstName"));
      }
      if (request.containsKey("lastName") && request.get("lastName") != null) {
        user.setLastName(request.get("lastName"));
      }
      if (request.containsKey("phone") && request.get("phone") != null) {
        user.setPhone(request.get("phone"));
      }
      if (request.containsKey("employeeId") && request.get("employeeId") != null) {
        user.setEmployeeId(request.get("employeeId"));
      }
      if (request.containsKey("department") && request.get("department") != null) {
        user.setDepartment(request.get("department"));
      }
      
      User updatedUser = userService.save(user);
      
      return ResponseEntity.ok(Map.of(
          "message", "User updated successfully",
          "userId", updatedUser.getId().toString()
      ));
      
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(Map.of("error", "Invalid user ID"));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(Map.of("error", "Failed to update user: " + e.getMessage()));
    }
  }
}

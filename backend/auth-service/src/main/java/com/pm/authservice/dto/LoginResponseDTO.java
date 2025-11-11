package com.pm.authservice.dto;

import java.util.UUID;

public class LoginResponseDTO {
  private final String token;
  private final String role;
  private final UUID tenantId;
  private final UUID userId;
  private final String name;

  public LoginResponseDTO(String token, String role, UUID tenantId, UUID userId, String name) {
    this.token = token;
    this.role = role;
    this.tenantId = tenantId;
    this.userId = userId;
    this.name = name;
  }

  public String getToken() {
    return token;
  }

  public String getRole() {
    return role;
  }

  public UUID getTenantId() {
    return tenantId;
  }

  public UUID getUserId() {
    return userId;
  }

  public String getName() {
    return name;
  }
}

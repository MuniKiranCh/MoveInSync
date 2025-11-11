package com.pm.authservice.util;

import com.pm.authservice.model.User;
import com.pm.authservice.model.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

  private final Key secretKey;
  private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24; // 24 hours

  public JwtUtil(@Value("${jwt.secret}") String secret) {
    byte[] keyBytes = Base64.getDecoder()
        .decode(secret.getBytes(StandardCharsets.UTF_8));
    this.secretKey = Keys.hmacShaKeyFor(keyBytes);
  }

  public String generateToken(User user) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("userId", user.getId().toString());
    claims.put("tenantId", user.getTenantId().toString());
    claims.put("role", user.getRole().toString());
    
    if (user.getVendorId() != null) {
      claims.put("vendorId", user.getVendorId().toString());
    }
    
    if (user.getFirstName() != null && user.getLastName() != null) {
      claims.put("name", user.getFirstName() + " " + user.getLastName());
    }

    return Jwts.builder()
        .setClaims(claims)
        .setSubject(user.getEmail())
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
        .signWith(secretKey)
        .compact();
  }

  public void validateToken(String token) {
    try {
      Jwts.parser().verifyWith((SecretKey) secretKey)
          .build()
          .parseSignedClaims(token);
    } catch (SignatureException e) {
      throw new JwtException("Invalid JWT signature");
    } catch (JwtException e) {
      throw new JwtException("Invalid JWT");
    }
  }

  public String extractEmail(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public UUID extractUserId(String token) {
    return UUID.fromString(extractClaim(token, claims -> claims.get("userId", String.class)));
  }

  public UUID extractTenantId(String token) {
    return UUID.fromString(extractClaim(token, claims -> claims.get("tenantId", String.class)));
  }

  public UserRole extractRole(String token) {
    return UserRole.valueOf(extractClaim(token, claims -> claims.get("role", String.class)));
  }

  public UUID extractVendorId(String token) {
    String vendorIdStr = extractClaim(token, claims -> claims.get("vendorId", String.class));
    return vendorIdStr != null ? UUID.fromString(vendorIdStr) : null;
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parser()
        .verifyWith((SecretKey) secretKey)
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }
}

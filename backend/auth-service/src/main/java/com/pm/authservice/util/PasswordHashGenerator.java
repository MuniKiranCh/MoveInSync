package com.pm.authservice.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHashGenerator {
  public static void main(String[] args) {
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    
    System.out.println("Generating BCrypt hashes...");
    System.out.println();
    
    String[] passwords = {"admin123", "vendor123", "employee123"};
    
    for (String password : passwords) {
      String hash = encoder.encode(password);
      System.out.println("Password: " + password);
      System.out.println("Hash: " + hash);
      System.out.println();
    }
  }
}


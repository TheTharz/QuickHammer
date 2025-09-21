package com.devnerd.auth_service.utils;

import java.security.Key;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TokenUtils {
  private final String secret;
  private Key secretKey;
  private final Long expiration;

  // init method to build key once
  private Key getSecretKey() {
      if (secretKey == null) {
          secretKey = Keys.hmacShaKeyFor(secret.getBytes());
      }
      return secretKey;
  }
    
  //generate jwt
  public String generateToken(String userId, String email,String role,String sessionId) {
    long now = System.currentTimeMillis();
    return Jwts.builder()
        .setSubject(userId)
        .claim("email", email)
        .claim("role", role)
        .claim("sessionId", sessionId)
        .setIssuedAt(new java.util.Date(now))
        .setExpiration(new java.util.Date(now + expiration))
        .signWith(getSecretKey(), SignatureAlgorithm.HS256)
        .compact();
  }

  //generate session id
  public String generateSessionId() {
    return java.util.UUID.randomUUID().toString();
  }
}

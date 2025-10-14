package com.devnerd.api_gateway.utils;

import java.security.Key;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TokenUtils {
  private final Key secretKey;

  public TokenUtils(String secret) {
    this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
  }

  public Claims validateToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}

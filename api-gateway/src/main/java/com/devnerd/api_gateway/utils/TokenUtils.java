package com.devnerd.api_gateway.utils;

import java.security.Key;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TokenUtils {
  private Key secretKey;
  private final String secret;

  private Key getSecretKey(){
    if(secretKey == null){
      secretKey = Keys.hmacShaKeyFor(secret.getBytes());
    }

    return secretKey;
  }

  public Claims validateToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}

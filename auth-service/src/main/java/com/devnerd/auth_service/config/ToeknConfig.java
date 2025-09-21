package com.devnerd.auth_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.devnerd.auth_service.utils.TokenUtils;

@Configuration
public class ToeknConfig {
  @Value("${jwt.secret}")
  private String secret;

  @Value("${jwt.expiration}")
  private long expiration;

  @Bean
  public TokenUtils tokenUtils() {
    return new TokenUtils(secret, expiration);
  }
}

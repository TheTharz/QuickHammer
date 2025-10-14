package com.devnerd.api_gateway.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.devnerd.api_gateway.utils.TokenUtils;

@Configuration
public class TokenConfigs {
  @Value("${jwt.secret}")
  private String secret;

  @Bean
  public TokenUtils tokenUtils() {
    return new TokenUtils(secret);
  }
}

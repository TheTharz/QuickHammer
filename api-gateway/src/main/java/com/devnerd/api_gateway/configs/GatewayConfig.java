package com.devnerd.api_gateway.configs;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.devnerd.api_gateway.filters.JwtAuthFilter;

import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
public class GatewayConfig {
  private final JwtAuthFilter jwtAuthFilter;

  @Bean
  public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
            .route("auth-service", r -> r.path("/api/v1/auth/**")
                .uri("lb://auth-service"))
            .route("job-service", r -> r.path("/api/v1/jobs/**")
                .filters(f -> f.filter(jwtAuthFilter))
                .uri("lb://job-service"))
            .build();
    }
}

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
                // AUTH SERVICE
                .route("auth-service", r -> r
                        .path("/api/v1/auth/**", "/auth-service/**")   // Add swagger route
                        .filters(f -> f.stripPrefix(1))                // remove "/auth-service" prefix
                        .uri("lb://auth-service"))

                // JOB SERVICE
                .route("job-service", r -> r
                        .path("/api/v1/jobs/**", "/job-service/**")    // Add swagger route
                        .filters(f -> f.filter(jwtAuthFilter).stripPrefix(1))
                        .uri("lb://job-service"))

                // USER SERVICE
                .route("user-service", r -> r
                        .path("/user-service/**")                      // Add swagger route
                        .filters(f -> f.stripPrefix(1))
                        .uri("lb://user-service"))

                // BID SERVICE
                .route("bid-service", r -> r
                        .path("/bid-service/**")                       // Add swagger route
                        .filters(f -> f.stripPrefix(1))
                        .uri("lb://bid-service"))

                // NOTIFICATION SERVICE
                .route("notification-service", r -> r
                        .path("/notification-service/**")              // Add swagger route
                        .filters(f -> f.stripPrefix(1))
                        .uri("lb://notification-service"))

                // PAYMENT SERVICE
                .route("payment-service", r -> r
                        .path("/payment-service/**")                   // Add swagger route
                        .filters(f -> f.stripPrefix(1))
                        .uri("lb://payment-service"))
            .build();
    }
}

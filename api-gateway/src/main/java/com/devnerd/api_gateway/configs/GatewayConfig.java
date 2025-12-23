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
                                .route("auth-service-exposed", r -> r
                                                .path("/api/v1/auth/register", "/api/v1/auth/login")
                                                .uri("lb://auth-service"))

                                .route("auth-service-swagger", r -> r
                                                .path("/auth-service/**")
                                                .filters(f -> f.stripPrefix(1))
                                                .uri("lb://auth-service"))

                                .route("auth-service-secured", r -> r
                                                .path("/api/v1/auth/**")
                                                .filters(f -> f.filter(jwtAuthFilter))
                                                .uri("lb://auth-service"))

                                // JOB SERVICE
                                .route("job-service-swagger", r -> r
                                                .path(
                                                                "/job-service/api-docs/**",
                                                                "/job-service/swagger-ui.html",
                                                                "/job-service/swagger-ui/**")
                                                .filters(f -> f.stripPrefix(1))
                                                .uri("lb://job-service"))

                                .route("job-service-secured", r -> r
                                                .path("/api/v1/jobs/**")
                                                .filters(f -> f.filter(jwtAuthFilter))
                                                .uri("lb://job-service"))

                                // USER SERVICE
                                .route("user-service-swagger", r -> r
                                                .path("/user-service/api-docs/**",
                                                                "/user-service/swagger-ui.html",
                                                                "/user-service/swagger-ui/**")
                                                .filters(f -> f.stripPrefix(1))
                                                .uri("lb://user-service"))

                                .route("user-service-secured", r -> r
                                                .path("/api/v1/users/**")
                                                .filters(f -> f.filter(jwtAuthFilter))
                                                .uri("lb://user-service"))

                                // BID SERVICE
                                .route("bid-service-swagger", r -> r
                                                .path("/bid-service/api-docs/**",
                                                                "/bid-service/swagger-ui.html",
                                                                "/bid-service/swagger-ui/**")
                                                .filters(f -> f.stripPrefix(1))
                                                .uri("lb://bid-service"))

                                .route("bid-service-secured", r -> r
                                                .path("/api/v1/bids/**")
                                                .filters(f -> f.filter(jwtAuthFilter))
                                                .uri("lb://bid-service"))

                                // NOTIFICATION SERVICE
                                .route("notification-service-swagger", r -> r
                                                .path("/notification-service/api-docs/**",
                                                                "/notification-service/swagger-ui.html",
                                                                "/notification-service/swagger-ui/**")
                                                .filters(f -> f.stripPrefix(1))
                                                .uri("lb://notification-service"))

                                .route("notification-service-secured", r -> r
                                                .path("/api/v1/notifications/**")
                                                .filters(f -> f.filter(jwtAuthFilter))
                                                .uri("lb://notification-service"))

                                // PAYMENT SERVICE
                                .route("payment-service-swagger", r -> r
                                                .path("/payment-service/api-docs/**",
                                                                "/payment-service/swagger-ui.html",
                                                                "/payment-service/swagger-ui/**")
                                                .filters(f -> f.stripPrefix(1))
                                                .uri("lb://payment-service"))

                                .route("payment-service-secured", r -> r
                                                .path("/api/v1/payments/**")
                                                .filters(f -> f.filter(jwtAuthFilter))
                                                .uri("lb://payment-service"))

                                .build();
        }
}

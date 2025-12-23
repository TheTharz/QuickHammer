package com.devnerd.api_gateway.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

        @Bean
        public SecurityWebFilterChain security(ServerHttpSecurity http) {

                return http
                                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                                .authorizeExchange(ex -> ex
                                                .pathMatchers(
                                                                "/swagger-ui.html",
                                                                "/swagger-ui/**",
                                                                "/v3/api-docs/**",
                                                                "/webjars/**",

                                                                "/job-service/api-docs/**",
                                                                "/job-service/swagger-ui.html",
                                                                "/job-service/swagger-ui/**",

                                                                "/user-service/**",
                                                                "/auth-service/**",
                                                                "/api/v1/auth/**",
                                                                "/bid-service/**",
                                                                "/job-service/**",
                                                                "/notification-service/**",
                                                                "/payment-service/**")
                                                .permitAll() // here all the routes have been opened later remove routes
                                                             // that need the authentication

                                                .anyExchange().authenticated())
                                .build();
        }
}
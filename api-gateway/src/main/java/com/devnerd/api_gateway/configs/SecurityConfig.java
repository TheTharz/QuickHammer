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
                                "/swagger-ui/**",
                                "/v3/api-docs/**",

                                "/user-service/**",
                                "/auth-service/**",
                                "/bid-service/**",
                                "/job-service/**",
                                "/notification-service/**",
                                "/payment-service/**"
                        ).permitAll()

                        .anyExchange().authenticated()
                )
                .build();
    }
}
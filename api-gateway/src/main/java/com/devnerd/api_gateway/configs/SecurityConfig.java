package com.devnerd.api_gateway.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

        @SuppressWarnings("removal")
        @Bean
        public SecurityWebFilterChain security(ServerHttpSecurity http) {

                return http
                                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                                .httpBasic().disable()
                                .formLogin().disable()
                                .authorizeExchange(ex -> ex
                                                .pathMatchers(
                                                                "/swagger-ui.html",
                                                                "/swagger-ui/**",
                                                                "/v3/api-docs/**",
                                                                "/webjars/**",
                                                                "/api/v1/auth/**").permitAll()
                                                .anyExchange().permitAll())
                                .build();
        }
}
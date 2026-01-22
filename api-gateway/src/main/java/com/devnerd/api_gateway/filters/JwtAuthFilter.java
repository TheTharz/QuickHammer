package com.devnerd.api_gateway.filters;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.devnerd.api_gateway.utils.TokenUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
@Slf4j
public class JwtAuthFilter implements GatewayFilter,Ordered{
  private final TokenUtils tokenUtils;

  @Override
  public int getOrder() {
    return -1;
  }

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    ServerHttpRequest request = exchange.getRequest();
    log.info("JwtAuthFilter TRIGGERED - Path: {}, Method: {}", request.getURI().getPath(), request.getMethod());

    if(exchange.getRequest().getMethod() == HttpMethod.OPTIONS){
        log.info("OPTIONS request - bypassing auth");
        return chain.filter(exchange);
    }

    if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
            log.warn("Missing Authorization header");
            return onError(exchange, "Missing Authorization header", HttpStatus.UNAUTHORIZED);
    }

    final String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        log.warn("Invalid Authorization header format");
        return onError(exchange, "Invalid Authorization header", HttpStatus.UNAUTHORIZED);
    }

    final String token = authHeader.substring(7);
    log.info("Validating token: {}...", token.substring(0, Math.min(20, token.length())));

    try {
      Claims claims = tokenUtils.validateToken(token);
      log.info("Token validated - User ID: {}, Role: {}", claims.getSubject(), claims.get("role", String.class));

      // Add user info headers
        ServerHttpRequest mutatedRequest = request.mutate()
                .header("X-User-Id", claims.getSubject())
                .header("X-User-Role", claims.get("role", String.class))
                .header("X-Session-Id", claims.get("sessionId", String.class))
                .build();

        return chain.filter(exchange.mutate().request(mutatedRequest).build());

    } catch (JwtException e) {
        log.error("Token validation failed: {}", e.getMessage());
        return onError(exchange, "Invalid or expired token", HttpStatus.UNAUTHORIZED);
    }
    
  }

  private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus status) {
        exchange.getResponse().setStatusCode(status);
        return exchange.getResponse().setComplete();
  }
  
}

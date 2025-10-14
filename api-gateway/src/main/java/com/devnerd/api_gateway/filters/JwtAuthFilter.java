package com.devnerd.api_gateway.filters;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.devnerd.api_gateway.utils.TokenUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter implements GatewayFilter,Ordered{
  private final TokenUtils tokenUtils;

  @Override
  public int getOrder() {
    return -1;
  }

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    ServerHttpRequest request = exchange.getRequest();

    if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
            return onError(exchange, "Missing Authorization header", HttpStatus.UNAUTHORIZED);
    }

    final String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        return onError(exchange, "Invalid Authorization header", HttpStatus.UNAUTHORIZED);
    }

    final String token = authHeader.substring(7);

    try {
      Claims claims = tokenUtils.validateToken(token);
      // Optionally, add user info to headers for downstream services
      request.mutate()
          .header("X-User-Id", claims.getSubject())
          .header("X-User-Role", claims.get("role", String.class))
          .build();

    } catch (JwtException e) {
        return onError(exchange, "Invalid or expired token", HttpStatus.UNAUTHORIZED);
    }
    
    return chain.filter(exchange.mutate().request(request).build());
  }

  private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus status) {
        exchange.getResponse().setStatusCode(status);
        return exchange.getResponse().setComplete();
  }
  
}

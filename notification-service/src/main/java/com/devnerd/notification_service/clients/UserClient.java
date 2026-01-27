package com.devnerd.notification_service.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.devnerd.notification_service.dto.UserDetailsReponseDTO;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;

/**
 * Feign client for User Service with Circuit Breaker protection
 */
@FeignClient("USER-SERVICE")
public interface UserClient {
  
  /**
   * Fetch user details to get email address for notifications
   */
  @CircuitBreaker(name = "userService", fallbackMethod = "getUserFallback")
  @GetMapping("/api/v1/user/{userId}")
  UserDetailsReponseDTO getUser(@PathVariable Long userId);
  
  /**
   * Fallback for getUser - returns null to allow graceful degradation
   */
  default UserDetailsReponseDTO getUserFallback(Long userId, Throwable throwable) {
    // Log the failure for monitoring
    System.err.println("Circuit breaker activated for user-service. UserId: " + userId + 
                       ", Error: " + throwable.getMessage());
    
    // Return null - caller will handle gracefully by skipping email
    // Notification will still be saved to DB
    return null;
  }
}

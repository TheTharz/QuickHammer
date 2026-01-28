package com.devnerd.auth_service.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.devnerd.auth_service.dto.RegisterUserRequestDTO;
import com.devnerd.auth_service.dto.UserDetailsResponseDTO;
import com.devnerd.auth_service.dto.UserReponseDTO;

import com.devnerd.auth_service.exception.DuplicateResourceException;
import com.devnerd.auth_service.exception.ServiceUnavailableException;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

/**
 * Feign client for User Service with Circuit Breaker protection
 */
@FeignClient(name = "USER-SERVICE")
public interface UserClient {
  
  /**
   * Register a new user in User Service
   * 
   * Circuit Breaker Configuration:
   * - name: "userService" - identifies this circuit breaker instance
   * - fallbackMethod: called when circuit is open or service fails
   * 
   * Interview Point: "For critical write operations, circuit breaker helps us
   * fail fast instead of letting threads wait for timeouts, preventing thread
   * pool exhaustion"
   */
  @CircuitBreaker(name = "userService", fallbackMethod = "registerUserFallback")
  @PostMapping("/api/v1/user/create-user")
  UserReponseDTO registerUser(@RequestBody RegisterUserRequestDTO request);

  /**
   * Fetch user details by ID
   */
  @CircuitBreaker(name = "userService", fallbackMethod = "getUserFallback")
  @GetMapping("/api/v1/user/{userId}")
  UserDetailsResponseDTO getUser(@PathVariable Long userId);
  
  /**
   * Fallback method signatures must match the original method
   * with an additional Throwable parameter
   */
  default UserReponseDTO registerUserFallback(RegisterUserRequestDTO request, Throwable throwable) {
    // Check if this is a FeignException with a 409 status (Duplicate Resource)
    if (throwable instanceof feign.FeignException) {
      feign.FeignException feignException = (feign.FeignException) throwable;
      if (feignException.status() == 409) {
        // Extract and re-throw as DuplicateResourceException with the original message
        String message = feignException.contentUTF8();
        if (message != null && message.contains("Email already exists")) {
          throw new DuplicateResourceException("Email already exists");
        } else if (message != null && message.contains("Username already exists")) {
          throw new DuplicateResourceException("Username already exists");
        }
        throw new DuplicateResourceException("Duplicate resource");
      }
    }
    
    // For actual service unavailability (timeouts, connection errors, circuit open)
    throw new ServiceUnavailableException(
      "User registration service is temporarily unavailable. Please try again later.",
      throwable
    );
  }
  
  default UserDetailsResponseDTO getUserFallback(Long userId, Throwable throwable) {
    throw new ServiceUnavailableException(
      "Unable to fetch user details. User service is temporarily unavailable.",
      throwable
    );
  }
}

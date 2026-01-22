package com.devnerd.auth_service.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devnerd.auth_service.dto.AuthenticationRequestDTO;
import com.devnerd.auth_service.dto.AuthenticationResponseDTO;
import com.devnerd.auth_service.dto.RegisterRequestDTO;
import com.devnerd.auth_service.dto.RegisterResponseDTO;
import com.devnerd.auth_service.dto.UserDetailsResponseDTO;
import com.devnerd.auth_service.services.AuthService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
  
  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/register")
  public ResponseEntity<RegisterResponseDTO> register(@RequestBody RegisterRequestDTO request) {
    RegisterResponseDTO registerResponseDTO = authService.registerUser(request);
    return ResponseEntity.ok(registerResponseDTO);
  }

  @PostMapping("/login")
  public ResponseEntity<AuthenticationResponseDTO> login(@RequestBody AuthenticationRequestDTO request) {
    AuthenticationResponseDTO authenticationResponseDTO = authService.authenticateUser(request);
    return ResponseEntity.ok(authenticationResponseDTO);
  }

  @GetMapping("/me")
  public ResponseEntity<UserDetailsResponseDTO> getCurrentUser(
      @RequestHeader("X-Session-Id") String sessionId,
      @RequestHeader("X-User-Id") Long userId) {
    UserDetailsResponseDTO currentUser = authService.getCurrentUser(sessionId, userId);
    return ResponseEntity.ok(currentUser);
  }
}

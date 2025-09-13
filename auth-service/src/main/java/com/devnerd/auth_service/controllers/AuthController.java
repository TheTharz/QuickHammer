package com.devnerd.auth_service.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devnerd.auth_service.dto.RegisterRequestDTO;
import com.devnerd.auth_service.dto.RegisterResponseDTO;
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
}

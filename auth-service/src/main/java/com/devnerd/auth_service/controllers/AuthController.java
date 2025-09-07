package com.devnerd.auth_service.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devnerd.auth_service.dto.RegisterRequestDTO;
import com.devnerd.auth_service.services.AuthService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
  
  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/register")
  public String register(@RequestBody RegisterRequestDTO request) {
    authService.registerUser(request);
    return "register";
  }
}

package com.devnerd.auth_service.services;

import org.springframework.stereotype.Service;

import com.devnerd.auth_service.clients.UserClient;
import com.devnerd.auth_service.dto.RegisterRequestDTO;
import com.devnerd.auth_service.dto.UserReponseDTO;

@Service
public class AuthService {
  private final UserClient userClient;

  public AuthService(UserClient userClient) {
    this.userClient = userClient;
  }
  public String registerUser(RegisterRequestDTO registerRequestDTO) {

    //call the user service to create the actual user
    UserReponseDTO userResponse = userClient.registerUser();
    return "register";
  }
}

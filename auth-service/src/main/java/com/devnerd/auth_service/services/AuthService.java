package com.devnerd.auth_service.services;

import org.springframework.stereotype.Service;

import com.devnerd.auth_service.clients.UserClient;
import com.devnerd.auth_service.dto.RegisterRequestDTO;
import com.devnerd.auth_service.dto.RegisterUserRequestDTO;
import com.devnerd.auth_service.dto.UserReponseDTO;
import com.devnerd.auth_service.utils.PasswordUtils;

@Service
public class AuthService {
  private final UserClient userClient;

  public AuthService(UserClient userClient) {
    this.userClient = userClient;
  }
  public String registerUser(RegisterRequestDTO registerRequestDTO) {

    //hash the password
    String hasedPassword = PasswordUtils.hashPassword(registerRequestDTO.getPassword());

    RegisterUserRequestDTO requestToUser = new RegisterUserRequestDTO(
      registerRequestDTO.getUsername(),
      registerRequestDTO.getEmail(),
      registerRequestDTO.getFullName(),
      registerRequestDTO.getPhoneNumber()
    );
    

    //call the user service to create the actual user
    UserReponseDTO userResponse = userClient.registerUser(requestToUser);
    
    return "register";
  }
}

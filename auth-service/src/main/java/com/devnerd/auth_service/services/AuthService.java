package com.devnerd.auth_service.services;

import org.springframework.stereotype.Service;

import com.devnerd.auth_service.clients.UserClient;
import com.devnerd.auth_service.dto.RegisterRequestDTO;
import com.devnerd.auth_service.dto.RegisterResponseDTO;
import com.devnerd.auth_service.dto.RegisterUserRequestDTO;
import com.devnerd.auth_service.dto.UserReponseDTO;
import com.devnerd.auth_service.exception.WeakPasswordException;
import com.devnerd.auth_service.model.AuthUser;
import com.devnerd.auth_service.repositories.AuthUserRepository;
import com.devnerd.auth_service.utils.PasswordUtils;

@Service
public class AuthService {
  private final UserClient userClient;
  private final AuthUserRepository authUserRepository;

  public AuthService(UserClient userClient, AuthUserRepository authUserRepository) {
    this.userClient = userClient;
    this.authUserRepository = authUserRepository;
  }

  public RegisterResponseDTO registerUser(RegisterRequestDTO registerRequestDTO) {

    //validate the password
    if(!PasswordUtils.isStrongPassword(registerRequestDTO.getPassword())){
      throw new WeakPasswordException("Password does not meet the required criteria");
    }

    //hash the password
    String hasedPassword = PasswordUtils.hashPassword(registerRequestDTO.getPassword());

    RegisterUserRequestDTO requestToUser = new RegisterUserRequestDTO(
      registerRequestDTO.getUsername(),
      registerRequestDTO.getEmail(),
      registerRequestDTO.getFirstName(),
      registerRequestDTO.getLastName(),
      registerRequestDTO.getPhoneNumber()
    );

    //call the user service to create the actual user
    UserReponseDTO userResponse = userClient.registerUser(requestToUser);

    //save the user in the database
    AuthUser authUser = new AuthUser(
      Long.parseLong(userResponse.getUserId()),
      hasedPassword,
      "user"
    );
    
    AuthUser savedUser = authUserRepository.save(authUser);

    RegisterResponseDTO registerResponseDTO = new RegisterResponseDTO(
      savedUser.getUserId().toString()
    );

    return registerResponseDTO;
  }
}

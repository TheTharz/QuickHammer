package com.devnerd.auth_service.services;

import org.springframework.stereotype.Service;

import com.devnerd.auth_service.clients.UserClient;
import com.devnerd.auth_service.dto.AuthenticationRequestDTO;
import com.devnerd.auth_service.dto.AuthenticationResponseDTO;
import com.devnerd.auth_service.dto.RegisterRequestDTO;
import com.devnerd.auth_service.dto.RegisterResponseDTO;
import com.devnerd.auth_service.dto.RegisterUserRequestDTO;
import com.devnerd.auth_service.dto.UserDetailsResponseDTO;
import com.devnerd.auth_service.dto.UserReponseDTO;
import com.devnerd.auth_service.exception.AuthenticationException;
import com.devnerd.auth_service.exception.WeakPasswordException;
import com.devnerd.auth_service.model.AuthUser;
import com.devnerd.auth_service.model.UserSession;
import com.devnerd.auth_service.repositories.jpa.AuthUserRepository;
import com.devnerd.auth_service.utils.PasswordUtils;
import com.devnerd.auth_service.utils.TokenUtils;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthService {
  private final UserClient userClient;
  private final AuthUserRepository authUserRepository;
  private final TokenUtils tokenUtils;
  private final SessionStorageService sessionStorageService;

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
      "user",
      registerRequestDTO.getEmail()
    );
    
    AuthUser savedUser = authUserRepository.save(authUser);

    RegisterResponseDTO registerResponseDTO = new RegisterResponseDTO(
      savedUser.getUserId()
    );

    return registerResponseDTO;
  }

  public AuthenticationResponseDTO authenticateUser(AuthenticationRequestDTO request) {
    //verify email is there
    AuthUser authUser = authUserRepository.findByEmail(request.getEmail())
        .orElseThrow(() -> new AuthenticationException("User not found"));

    //if user is there verify
    if(!PasswordUtils.verifyPassword(request.getPassword(), authUser.getPasswordHash())){
      throw new AuthenticationException("Invalid credentials");
    }

    String sessionId = tokenUtils.generateSessionId();

    //genereate jwt and session id
    String jwtToken = tokenUtils.generateToken(
      authUser.getUserId().toString(),
      authUser.getEmail(),
      authUser.getRole(),
      sessionId
    );

    AuthenticationResponseDTO response = new AuthenticationResponseDTO(
      jwtToken
    );

    //call the user service and get the user details
    UserDetailsResponseDTO userResponse = userClient.getUser(authUser.getUserId());

    UserSession session = UserSession.builder()
      .sessionId(sessionId)
      .userId(authUser.getUserId())
      .phoneNumber(userResponse.getPhoneNumber())
      .firstName(userResponse.getFirstName())
      .lastName(userResponse.getLastName())
      .email(userResponse.getEmail())
      .userName(userResponse.getUserName())
      .expiry(0L)
      .build();

    sessionStorageService.createSession(session);

    return response;
  }
}

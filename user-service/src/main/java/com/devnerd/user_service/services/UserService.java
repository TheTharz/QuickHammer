package com.devnerd.user_service.services;

import org.springframework.stereotype.Service;

import com.devnerd.user_service.dto.RegisterUserRequestDTO;
import com.devnerd.user_service.dto.RegisterUserResponseDTO;
import com.devnerd.user_service.exception.DuplicateResourceException;
import com.devnerd.user_service.models.UserModel;
import com.devnerd.user_service.repositories.jpa.UserRepository;

@Service
public class UserService {

  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public RegisterUserResponseDTO createUser(RegisterUserRequestDTO registerUserRequestDTO) {
    // Check for duplicate email or username
    if (userRepository.existsByEmail(registerUserRequestDTO.getEmail())) {
        throw new DuplicateResourceException("Email already exists");
    }
    if (userRepository.existsByUsername(registerUserRequestDTO.getUsername())) {
        throw new DuplicateResourceException("Username already exists");
    }
    
    // Map DTO to entity
    UserModel user = UserModel.builder()
            .username(registerUserRequestDTO.getUsername())
            .email(registerUserRequestDTO.getEmail())
            .phoneNumber(registerUserRequestDTO.getPhoneNumber())
            .firstName(registerUserRequestDTO.getFirstName())
            .lastName(registerUserRequestDTO.getLastName())
            .build();

    UserModel savedUser = userRepository.save(user);

    //map to dto
    RegisterUserResponseDTO registerUserResponseDTO = RegisterUserResponseDTO.builder()
            .userId(savedUser.getId().toString())
            .build();

    return registerUserResponseDTO;
  }
  
}

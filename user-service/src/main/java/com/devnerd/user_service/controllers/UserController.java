package com.devnerd.user_service.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devnerd.user_service.dto.RegisterUserRequestDTO;
import com.devnerd.user_service.dto.RegisterUserResponseDTO;
import com.devnerd.user_service.dto.UserDetailsResponseDTO;
import com.devnerd.user_service.services.UserService;

@RestController
@RequestMapping("api/v1/user")
public class UserController {
  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/create-user")
  public RegisterUserResponseDTO createUser(@RequestBody RegisterUserRequestDTO registerUserRequestDTO) {
    RegisterUserResponseDTO response = userService.createUser(registerUserRequestDTO);
    return response;
  }

  @PostMapping("/get-user-details")
  public UserDetailsResponseDTO getUser(@RequestBody Long userId) {
    UserDetailsResponseDTO response = userService.getUserDetails(userId);
    return response;
  }
}

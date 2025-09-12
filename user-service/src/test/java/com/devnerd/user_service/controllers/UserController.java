package com.devnerd.user_service.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devnerd.user_service.dto.RegisterUserRequestDTO;
import com.devnerd.user_service.services.UserService;

@RestController
@RequestMapping("api/v2/user")
public class UserController {
  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/create-user")
  public String createUser(@RequestBody RegisterUserRequestDTO registerUserRequestDTO) {
    userService.createUser(registerUserRequestDTO);
    return "User created successfully";
  }
}

package com.devnerd.auth_service.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.devnerd.auth_service.dto.RegisterUserRequestDTO;
import com.devnerd.auth_service.dto.UserDetailsResponseDTO;
import com.devnerd.auth_service.dto.UserReponseDTO;

@FeignClient(name = "USER-SERVICE")
public interface UserClient {
  @PostMapping("/api/v1/user/create-user")
  UserReponseDTO registerUser(@RequestBody RegisterUserRequestDTO request);

  @GetMapping("/api/v1/user/{userId}")
  UserDetailsResponseDTO getUser(@PathVariable Long userId);
}

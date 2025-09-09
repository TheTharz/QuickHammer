package com.devnerd.auth_service.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.devnerd.auth_service.dto.RegisterUserRequestDTO;
import com.devnerd.auth_service.dto.UserReponseDTO;

@FeignClient(name = "user-service", url = "http://localhost:8081/api/v1/users")
public interface UserClient {
  @PostMapping("/create-user")
  UserReponseDTO registerUser(@RequestBody RegisterUserRequestDTO request);
}

package com.devnerd.notification_service.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.devnerd.notification_service.dto.UserDetailsReponseDTO;

@FeignClient("USER-SERVICE")
public interface UserClient {
  @PostMapping("/api/v1/user/get-user-details")
  UserDetailsReponseDTO getUser(@RequestBody Long userId);
}

package com.devnerd.auth_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterUserRequestDTO {
  private String username;
  private String email;
  private String fullName;
  private String phoneNumber;
}

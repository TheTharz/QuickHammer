package com.devnerd.auth_service.dto;

import lombok.Data;

@Data
public class AuthenticationRequestDTO {
  private String email;
  private String password;
}

package com.devnerd.auth_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthenticationResponseDTO {
  private String jwtTokent;
}

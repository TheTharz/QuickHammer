package com.devnerd.user_service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterUserResponseDTO {
  private String userId;
}

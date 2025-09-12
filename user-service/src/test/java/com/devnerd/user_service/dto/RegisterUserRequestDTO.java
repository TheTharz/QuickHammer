package com.devnerd.user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserRequestDTO {
  private String username;
  private String email;
  private String firstName;
  private String lastName;
  private String phoneNumber;
}

package com.devnerd.auth_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsResponseDTO {
  private Long userId;
  private String phoneNumber;
  private String firstName;
  private String lastName;
  private String email;
  private String userName;
}

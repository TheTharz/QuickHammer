package com.devnerd.user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UserDetailsResponseDTO {
  private Long userId;
  private String phoneNumber;
  private String firstName;
  private String lastName;
  private String email;
  private String userName;
}

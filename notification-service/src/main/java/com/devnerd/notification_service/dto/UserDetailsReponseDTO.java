package com.devnerd.notification_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsReponseDTO {
  private Long userId;
  private String phoneNumber;
  private String firstName;
  private String lastName;
  private String email;
  private String userName;
}

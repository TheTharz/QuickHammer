package com.devnerd.auth_service.events.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginEvent {
  private long userId;
  private String sessionId;
  private long timestamp;
}

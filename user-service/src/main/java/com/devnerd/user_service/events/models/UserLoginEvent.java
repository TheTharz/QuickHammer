package com.devnerd.user_service.events.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class UserLoginEvent {
  private long userId;
  private String sessionId;
  private long timestamp;
}

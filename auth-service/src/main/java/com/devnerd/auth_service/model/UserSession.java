package com.devnerd.auth_service.model;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@RedisHash("UserSession")
public class UserSession implements Serializable{
  @Id
  private String sessionId;
  private Long userId;
  private String phoneNumber;
  private String firstName;
  private String lastName;
  private String email;
  private String userName;
  private Long expiry;
}

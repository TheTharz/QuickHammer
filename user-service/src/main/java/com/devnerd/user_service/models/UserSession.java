package com.devnerd.user_service.models;

import java.io.Serializable;

import org.springframework.data.redis.core.RedisHash;

import jakarta.persistence.Id;
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
  private Long userId;
  private String sessionId;
  private String phoneNumber;
  private String firstName;
  private String lastName;
  private String email;
  private String userName;
  private Long expiry;
}

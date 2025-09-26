package com.devnerd.user_service.events.consumers;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.devnerd.user_service.events.models.UserLoginEvent;
import com.devnerd.user_service.services.SessionStorageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserEventConsumer {
  private final SessionStorageService sessionStorageService;
  
  @KafkaListener(topics = "user.login", groupId = "user-service-group")
  public void handleUserLoginEvent(UserLoginEvent event){
    try {
      log.info("Received UserLoginEvent for user: {}", event.getUserId());
      sessionStorageService.enrichSessionOnLogin(event);
    } catch (Exception e) {
      log.error("Error processing UserLoginEvent for user: {}", event.getUserId(), e);
    }
  }
}

package com.devnerd.auth_service.events.producers;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.devnerd.auth_service.events.models.UserLoginEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventProducer {
  private final KafkaTemplate<String,Object> kafkaTemplate;

  private static final String USER_LOGIN_TOPIC = "user.login";

  public void publishUserLoginEvent(UserLoginEvent event){
    try {
      kafkaTemplate.send(USER_LOGIN_TOPIC, event);
      log.info("Published UserLoginEvent for user: {}",event.getUserId());
    } catch (Exception e) {
      log.error("Failed to publish UserLoginEvent for user: {}",event.getUserId(),e);
    }
  }
  
}

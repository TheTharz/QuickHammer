package com.devnerd.user_service.services;

import org.springframework.stereotype.Service;

import com.devnerd.user_service.events.models.UserLoginEvent;
import com.devnerd.user_service.models.UserModel;
import com.devnerd.user_service.models.UserSession;
import com.devnerd.user_service.repositories.UserRepository;
import com.devnerd.user_service.repositories.UserSessionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SessionStorageService {
  private final UserSessionRepository userSessionRepository;
  private final UserRepository userRepository;

  public void enrichSessionOnLogin(UserLoginEvent event){
    //get the user details
    UserModel user = userRepository.findById(event.getUserId()).orElse(null);

    if (user == null) {
      log.error("User not found for session enrichment: {}", event.getUserId());
      return;
    }

    //update the session storage with the new user details
    UserSession existingSession = userSessionRepository.findByUserIdAndSessionId(event.getUserId(), event.getSessionId()).orElse(null);

    if (existingSession != null) {
      // Update existing session with enriched details
      existingSession.setEmail(user.getEmail());
      existingSession.setFirstName(user.getFirstName());
      existingSession.setLastName(user.getLastName());
      existingSession.setUserName(user.getUsername());
      existingSession.setPhoneNumber(user.getPhoneNumber());
      
      userSessionRepository.save(existingSession);
      log.info("Updated existing session for user: {} with sessionId: {}", 
              event.getUserId(), event.getSessionId());
    } else {
      log.info("No session found for received event for login: {} with sessionId: {}", 
              event.getUserId(), event.getSessionId());
    }
  }
}

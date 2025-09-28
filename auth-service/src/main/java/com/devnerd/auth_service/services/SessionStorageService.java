package com.devnerd.auth_service.services;

import org.springframework.stereotype.Service;

import com.devnerd.auth_service.model.UserSession;
import com.devnerd.auth_service.repositories.redis.UserSessionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SessionStorageService {
  private final UserSessionRepository userSessionRepository;

  public void createSession(UserSession session) {
    userSessionRepository.save(session);
    log.info("Created basic session for user: {} with sessionId: {}", session.getUserId(), session.getSessionId());
  }
}

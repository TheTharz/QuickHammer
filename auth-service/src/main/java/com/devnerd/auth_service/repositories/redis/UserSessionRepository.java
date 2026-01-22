package com.devnerd.auth_service.repositories.redis;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.devnerd.auth_service.model.UserSession;

public interface UserSessionRepository extends CrudRepository<UserSession,String>{
  Optional<UserSession> findBySessionId(String sessionId);
}

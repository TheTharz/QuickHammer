package com.devnerd.user_service.repositories.redis;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.devnerd.user_service.models.UserSession;

public interface UserSessionRepository extends CrudRepository<UserSession,Long>{
  Optional<UserSession> findByUserId(String userId);
  Optional<UserSession> findByUserIdAndSessionId(Long userId, String sessionId);
  void deleteByUserId(String userId);
}

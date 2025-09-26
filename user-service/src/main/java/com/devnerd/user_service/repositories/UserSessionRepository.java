package com.devnerd.user_service.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devnerd.user_service.models.UserSession;

public interface UserSessionRepository extends JpaRepository<UserSession,Long>{
  Optional<UserSession> findByUserId(String userId);
  Optional<UserSession> findByUserIdAndSessionId(Long userId, String sessionId);
  void deleteByUserId(String userId);
}

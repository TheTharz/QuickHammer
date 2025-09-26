package com.devnerd.auth_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devnerd.auth_service.model.UserSession;

public interface UserSessionRepository extends JpaRepository<UserSession,Long>{
  
}

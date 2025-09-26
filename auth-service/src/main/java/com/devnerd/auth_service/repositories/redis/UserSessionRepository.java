package com.devnerd.auth_service.repositories.redis;

import org.springframework.data.repository.CrudRepository;

import com.devnerd.auth_service.model.UserSession;

public interface UserSessionRepository extends CrudRepository<UserSession,Long>{
  
}

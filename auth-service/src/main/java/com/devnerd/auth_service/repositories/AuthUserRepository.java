package com.devnerd.auth_service.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devnerd.auth_service.model.AuthUser;

public interface AuthUserRepository extends JpaRepository<AuthUser, Long> {
  Optional<AuthUser> findByUserId(Long userId);

  Optional<AuthUser> findByEmail(String email);
}

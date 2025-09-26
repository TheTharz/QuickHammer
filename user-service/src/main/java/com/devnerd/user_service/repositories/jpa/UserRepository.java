package com.devnerd.user_service.repositories.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devnerd.user_service.models.UserModel;

public interface UserRepository extends JpaRepository<UserModel, Long> {
  Optional <UserModel> findByUsername(String username);

  boolean existsByEmail(String email);

  boolean existsByUsername(String username);
}

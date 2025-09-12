package com.devnerd.user_service.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserModel {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY) 
  private Long id;

  @Column(unique = true)
  private String username;

  @Column(unique = true)
  @Email(message = "Invalid email format")
  private String email;

  @Column
  private String phoneNumber;

  @Column
  private String firstName;

  @Column
  private String lastName;

  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(nullable = false)
  private LocalDateTime updatedAt;

  @PrePersist
  protected void onCreate() {
      this.createdAt = LocalDateTime.now();
      this.updatedAt = LocalDateTime.now();
  }
  
  @PreUpdate
  protected void onUpdate() {
      this.updatedAt = LocalDateTime.now();
  }
}

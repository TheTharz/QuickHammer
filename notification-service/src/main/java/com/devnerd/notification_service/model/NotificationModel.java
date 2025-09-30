package com.devnerd.notification_service.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notification")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class NotificationModel {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long notificationId;

  @Column(nullable = false)
  private Long recipientId;

  @Column(nullable = false, length = 1000)
  private String message;

  @Column(nullable = false)
  private LocalDateTime createdAt;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private NotificationMedium medium;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public enum NotificationMedium {
        EMAIL, SMS , PUSH, WHATSAPP
    }
}

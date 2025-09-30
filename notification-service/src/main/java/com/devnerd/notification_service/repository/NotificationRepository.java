package com.devnerd.notification_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devnerd.notification_service.model.NotificationModel;

public interface NotificationRepository extends JpaRepository<NotificationModel, Long> {
  
}

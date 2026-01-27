package com.devnerd.notification_service.services;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.devnerd.notification_service.model.NotificationModel;
import com.devnerd.notification_service.model.NotificationModel.NotificationMedium;
import com.devnerd.notification_service.repository.NotificationRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service responsible for persisting notifications to database
 * */
@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationPersistenceService {
    
    private final NotificationRepository notificationRepository;
    
    /**
     * Save notification to database
     * 
     * @param recipientId User ID receiving the notification
     * @param message Notification message
     * @return Saved notification
     */
    public NotificationModel saveNotification(Long recipientId, String message) {
        log.debug("Saving notification for recipientId: {}", recipientId);
        
        NotificationModel notification = NotificationModel.builder()
            .recipientId(recipientId)
            .message(message)
            .createdAt(LocalDateTime.now())
            .medium(NotificationMedium.EMAIL)
            .build();
        
        NotificationModel saved = notificationRepository.save(notification);
        
        log.info("Notification saved with id: {} for recipientId: {}", 
                 saved.getNotificationId(), recipientId);
        
        return saved;
    }
}

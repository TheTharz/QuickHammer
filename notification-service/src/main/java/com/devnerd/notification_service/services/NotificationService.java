package com.devnerd.notification_service.services;

import org.springframework.stereotype.Service;

import com.devnerd.events.models.JobAssignedEvent;
import com.devnerd.events.models.JobCompletedEvent;
import com.devnerd.notification_service.dto.UserDetailsReponseDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Orchestrator service for notification processing
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
  
  private final UserDetailsService userDetailsService;
  private final EmailTemplateService emailTemplateService;
  private final EmailService emailService;
  private final NotificationPersistenceService notificationPersistenceService;

  /**
   * Process job assignment notification
   */
  public void sendJobAssignedEmail(JobAssignedEvent event) {
    log.info("Processing job assignment notification for userId: {}", event.getAssignedToId());
    
    // Step 1: Fetch user details (may return null if circuit breaker is open)
    UserDetailsReponseDTO user = userDetailsService.getUserDetails(event.getAssignedToId());

    // Step 2: Handle circuit breaker fallback
    if (user == null) {
      log.warn("User service unavailable. Saving pending notification for userId: {}", 
               event.getAssignedToId());
      
      String pendingMessage = emailTemplateService.buildPendingNotificationMessage(
        event.getJobTitle(), 
        "user service unavailable"
      );
      
      notificationPersistenceService.saveNotification(
        event.getAssignedToId(), 
        pendingMessage
      );
      
      return; // Exit gracefully
    }

    // Step 3: Compose email
    String subject = emailTemplateService.buildJobAssignedSubject(event);
    String body = emailTemplateService.buildJobAssignedBody(event, user);

    // Step 4: Send email
    boolean emailSent = emailService.sendEmail(user.getEmail(), subject, body);

    // Step 5: Persist notification
    String notificationMessage = emailSent ? body : body + " (Email failed)";
    notificationPersistenceService.saveNotification(user.getUserId(), notificationMessage);
    
    log.info("Job assignment notification processed for userId: {}", user.getUserId());
  }

  /**
   * Process job completion notification
   */
  public void sendJobCompletedEmail(JobCompletedEvent event) {
    log.info("Processing job completion notification for client: {}", event.getClientId());
    
    // Step 1: Fetch user details (both client and freelancer)
    UserDetailsReponseDTO client = userDetailsService.getUserDetails(event.getClientId());
    UserDetailsReponseDTO freelancer = userDetailsService.getUserDetails(event.getCompletedById());

    // Step 2: Handle circuit breaker fallback
    if (client == null || freelancer == null) {
      log.warn("User service unavailable. Saving pending notification. ClientId: {}, FreelancerId: {}", 
               event.getClientId(), event.getCompletedById());
      
      String pendingMessage = emailTemplateService.buildPendingNotificationMessage(
        event.getJobTitle(), 
        "user service unavailable"
      );
      
      notificationPersistenceService.saveNotification(
        event.getClientId(), 
        pendingMessage
      );
      
      return; // Exit gracefully
    }

    // Step 3: Compose email
    String subject = emailTemplateService.buildJobCompletedSubject(event);
    String body = emailTemplateService.buildJobCompletedBody(event, client, freelancer);

    // Step 4: Send email
    boolean emailSent = emailService.sendEmail(client.getEmail(), subject, body);

    // Step 5: Persist notification
    String notificationMessage = emailSent ? body : body + " (Email failed)";
    notificationPersistenceService.saveNotification(client.getUserId(), notificationMessage);
    
    log.info("Job completion notification processed for client: {}", client.getUserId());
  }
}

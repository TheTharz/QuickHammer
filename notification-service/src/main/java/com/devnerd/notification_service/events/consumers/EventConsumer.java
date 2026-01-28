package com.devnerd.notification_service.events.consumers;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.devnerd.events.models.BidRejectedEvent;
import com.devnerd.events.models.JobAssignedEvent;
import com.devnerd.events.models.JobCompletedEvent;
import com.devnerd.notification_service.services.NotificationService;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * Event Consumer for Choreography Saga Pattern
 * Listens to saga events and sends notifications
 */
@Data
@Service
@Slf4j
public class EventConsumer {
  private final NotificationService emailService;
  
  @KafkaListener(topics = "job.assigned", groupId = "notification-service-group")
  public void handleJobAssignedEvent(JobAssignedEvent event){
    try {
      log.info("Received JobAssignedEvent for job: {}",event.getJobId());
      emailService.sendJobAssignedEmail(event);
    } catch (Exception e) {
      log.error("Failed to handle JobAssignedEvent for job: {}",event.getJobId(),e);
    }
  }

  @KafkaListener(topics = "job.completed", groupId = "notification-service-group")
  public void handleJobCompletedEvent(JobCompletedEvent event){
    try {
      log.info("Received JobCompletedEvent for job: {}, client: {}",event.getJobId(), event.getClientId());
      emailService.sendJobCompletedEmail(event);
    } catch (Exception e) {
      log.error("Failed to handle JobCompletedEvent for job: {}",event.getJobId(),e);
    }
  }
  
  /**
   * Choreography Saga Participant - Handle Bid Rejected Event
   * Part of Bid Acceptance Saga
   */
  @KafkaListener(topics = "bid.rejected", groupId = "notification-service-group")
  public void handleBidRejectedEvent(BidRejectedEvent event){
    try {
      log.info("[SAGA:{}] Received BidRejectedEvent for bid: {}, user: {}", 
               event.getSagaId(), event.getBidId(), event.getBidderId());
      emailService.sendBidRejectedEmail(event);
      log.info("[SAGA:{}] Sent bid rejection notification to user: {}", 
               event.getSagaId(), event.getBidderId());
    } catch (Exception e) {
      log.error("[SAGA:{}] Failed to handle BidRejectedEvent for bid: {}", 
                event.getSagaId(), event.getBidId(), e);
    }
  }
}

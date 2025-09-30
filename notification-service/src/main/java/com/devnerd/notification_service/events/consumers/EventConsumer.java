package com.devnerd.notification_service.events.consumers;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.devnerd.events.models.JobAssignedEvent;
import com.devnerd.notification_service.services.NotificationService;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

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
}

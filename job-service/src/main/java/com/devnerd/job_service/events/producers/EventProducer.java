package com.devnerd.job_service.events.producers;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.devnerd.events.models.BidAcceptanceRollbackEvent;
import com.devnerd.events.models.JobAssignedEvent;
import com.devnerd.events.models.JobCompletedEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Event Producer for Choreography Saga Pattern
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EventProducer {
  private final KafkaTemplate<String,Object> kafkaTemplate;

  private static final String JOB_ASSIGNED_TOPIC = "job.assigned";
  private static final String JOB_COMPLETED_TOPIC = "job.completed";
  private static final String BID_ACCEPTANCE_ROLLBACK_TOPIC = "bid.acceptance.rollback";

  public void publishJobAssignedEvent(JobAssignedEvent event){
    try {
      kafkaTemplate.send(JOB_ASSIGNED_TOPIC, event);
      log.info("Published JobAssignedEvent for job: {}",event.getJobId());
    } catch (Exception e) {
      log.error("Failed to publish JobAssignedEvent for job: {}",event.getJobId(),e);
    }
  }

  public void publishJobCompletedEvent(JobCompletedEvent event){
    try {
      kafkaTemplate.send(JOB_COMPLETED_TOPIC, event);
      log.info("Published JobCompletedEvent for job: {}, completedBy: {}",event.getJobId(), event.getCompletedById());
    } catch (Exception e) {
      log.error("Failed to publish JobCompletedEvent for job: {}",event.getJobId(),e);
    }
  }
  
  /**
   * Publish rollback event to compensate bid acceptance
   * Part of Choreography Saga compensation logic
   */
  public void publishBidAcceptanceRollbackEvent(Long bidId, Long jobId, Long freelancerId, String sagaId, String reason){
    try {
      BidAcceptanceRollbackEvent event = BidAcceptanceRollbackEvent.builder()
          .bidId(bidId)
          .jobId(jobId)
          .bidderId(freelancerId)
          .rollbackReason(reason)
          .sagaId(sagaId)
          .build();
      
      kafkaTemplate.send(BID_ACCEPTANCE_ROLLBACK_TOPIC, event);
      log.warn("[SAGA:{}] Published BidAcceptanceRollbackEvent for bid: {}. Reason: {}", 
               sagaId, bidId, reason);
    } catch (Exception e) {
      log.error("[SAGA:{}] Failed to publish BidAcceptanceRollbackEvent for bid: {}", 
                sagaId, bidId, e);
    }
  }
}

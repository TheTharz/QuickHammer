package com.devnerd.bid_service.events.consumers;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.devnerd.bid_service.services.BidService;
import com.devnerd.events.models.BidAcceptanceRollbackEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Event Consumer for Choreography Saga Pattern - Compensation Logic
 * Listens for rollback events from Job Service
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EventConsumer {
  private final BidService bidService;
  
  /**
   * Handle bid acceptance rollback when job assignment fails
   * This is the compensating transaction for the saga
   */
  @KafkaListener(topics = "bid.acceptance.rollback", groupId = "bid-service-group")
  public void handleBidAcceptanceRollback(BidAcceptanceRollbackEvent event) {
    try {
      log.warn("[SAGA:{}] Received BidAcceptanceRollbackEvent for bid: {}. Reason: {}", 
               event.getSagaId(), event.getBidId(), event.getRollbackReason());
      bidService.rollbackBidAcceptance(event.getBidId(), event.getSagaId(), event.getRollbackReason());
      log.info("[SAGA:{}] Successfully rolled back bid acceptance for bid: {}", 
               event.getSagaId(), event.getBidId());
    } catch (Exception e) {
      log.error("[SAGA:{}] Failed to rollback bid acceptance for bid: {}", 
                event.getSagaId(), event.getBidId(), e);
    }
  }
}

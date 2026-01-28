package com.devnerd.bid_service.events.producers;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.devnerd.events.models.BidAcceptedEvent;
import com.devnerd.events.models.BidRejectedEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Event Producer for Choreography Saga Pattern
 * Publishes events to Kafka for bid-related saga flows
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EventProducer {
  private final KafkaTemplate<String,Object> kafkaTemplate;

  private static final String BID_ACCEPTED_TOPIC = "bid.accepted";
  private static final String BID_REJECTED_TOPIC = "bid.rejected";

  public void publishBidAcceptedEvent(BidAcceptedEvent event){
    try {
      kafkaTemplate.send(BID_ACCEPTED_TOPIC, event);
      log.info("Published BidAcceptedEvent for bid: {} for job: {}",event.getBidId(),event.getJobId());
    } catch (Exception e) {
      log.error("Failed to publish BidAcceptedEvent for bid: {} for job: {}",event.getBidId(),event.getJobId(),e);
    }
  }
  
  public void publishBidRejectedEvent(BidRejectedEvent event){
    try {
      kafkaTemplate.send(BID_REJECTED_TOPIC, event);
      log.info("[SAGA:{}] Published BidRejectedEvent for bid: {} for job: {}", 
               event.getSagaId(), event.getBidId(), event.getJobId());
    } catch (Exception e) {
      log.error("[SAGA:{}] Failed to publish BidRejectedEvent for bid: {} for job: {}", 
                event.getSagaId(), event.getBidId(), event.getJobId(), e);
    }
  }
}

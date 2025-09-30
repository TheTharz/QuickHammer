package com.devnerd.job_service.events.consumers;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.devnerd.events.models.BidAcceptedEvent;
import com.devnerd.job_service.services.JobService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventConsumer {
  private final JobService jobService;
  @KafkaListener(topics = "bid.accepted", groupId = "job-service-group")
  public void handleBidAcceptedEvent(BidAcceptedEvent event){
    try {
      log.info("Received BidAcceptedEvent for bid: {} for job: {}",event.getBidId(),event.getJobId());
      jobService.updateJobOnBIdAccept(event.getJobId(),event.getBidId());
    } catch (Exception e) {
      log.error("Failed to handle BidAcceptedEvent for bid: {} for job: {}",event.getBidId(),event.getJobId(),e);
    }
  }
}

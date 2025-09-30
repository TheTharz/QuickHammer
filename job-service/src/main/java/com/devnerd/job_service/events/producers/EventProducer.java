package com.devnerd.job_service.events.producers;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.devnerd.events.models.JobAssignedEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventProducer {
  private final KafkaTemplate<String,Object> kafkaTemplate;

  private static final String JOB_ASSIGNED_TOPIC = "job.assigned";

  public void publishJobAssignedEvent(JobAssignedEvent event){
    try {
      kafkaTemplate.send(JOB_ASSIGNED_TOPIC, event);
      log.info("Published JobAssignedEvent for job: {}",event.getJobId());
    } catch (Exception e) {
      log.error("Failed to publish JobAssignedEvent for job: {}",event.getJobId(),e);
    }
  }
}

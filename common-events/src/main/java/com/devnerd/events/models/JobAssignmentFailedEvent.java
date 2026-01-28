package com.devnerd.events.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Event to rollback job assignment when saga fails
 * Part of Choreography Saga: Bid Acceptance Flow - Compensation
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobAssignmentFailedEvent {
  private Long jobId;
  private Long bidId;
  private Long freelancerId;
  private String failureReason;
  private String sagaId;
}

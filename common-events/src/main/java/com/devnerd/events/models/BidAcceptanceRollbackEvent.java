package com.devnerd.events.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Event to compensate/rollback bid acceptance
 * Part of Choreography Saga: Bid Acceptance Flow - Compensation
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BidAcceptanceRollbackEvent {
  private Long bidId;
  private Long jobId;
  private Long bidderId;
  private String rollbackReason;
  private String sagaId;
}

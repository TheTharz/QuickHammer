package com.devnerd.events.models;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Event published when a bid is rejected (due to another bid being accepted)
 * Part of Choreography Saga: Bid Acceptance Flow
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BidRejectedEvent {
  private Long bidId;
  private Long jobId;
  private Long bidderId;
  private BigDecimal bidAmount;
  private String rejectionReason;
  private Long acceptedBidId; // The bid that was accepted instead
  private String sagaId; // Links all events in this saga instance
}

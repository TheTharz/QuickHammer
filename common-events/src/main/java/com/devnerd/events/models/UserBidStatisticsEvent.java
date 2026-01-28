package com.devnerd.events.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Event to update user statistics when they win/lose a bid
 * Part of Choreography Saga: Bid Acceptance Flow
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserBidStatisticsEvent {
  private Long userId;
  private Long jobId;
  private Long bidId;
  private boolean bidWon; // true if won, false if lost
  private String sagaId;
}

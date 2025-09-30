package com.devnerd.events.models;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BidAcceptedEvent {
  private Long jobId;
  private Long bidId;
  private Long assignedToId;
  private BigDecimal bidBudget;
}

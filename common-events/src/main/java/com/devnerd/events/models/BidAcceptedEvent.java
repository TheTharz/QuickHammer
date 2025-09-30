package com.devnerd.events.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BidAcceptedEvent {
  private Long jobId;
  private Long bidId;
}

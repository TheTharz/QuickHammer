package com.devnerd.events.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BidAcceptedEvent {
  private Long jobId;
  private Long bidId;
}

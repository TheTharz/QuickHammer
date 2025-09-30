package com.devnerd.bid_service.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface BidSummaryDTO {
  Long getJobId();
  Long getBidId();
  Long getBidderId();
  String getMessage();
  BigDecimal getAmount();
  String getStatus();
  LocalDateTime getUpdatedAt();
}

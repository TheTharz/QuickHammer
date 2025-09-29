package com.devnerd.bid_service.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateBidRequestDTO {
  private String jobId;
  private String bidderId;
  private BigDecimal amount;
  private String message;
}

package com.devnerd.bid_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class CreateBidResponseDTO {
  private final Long bidId;
}

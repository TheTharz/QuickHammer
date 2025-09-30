package com.devnerd.bid_service.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class GetBidsByJobResponseDTO {
  private List<BidSummaryDTO> bids;
  private int page;
  private int size;
  private long totalElements;
  private int totalPages;
}

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
public class JobAssignedEvent {
  private Long jobId;
  private Long bidId;
  private Long assignedToId;
  private Long clientId;
  private String jobTitle;
  private String jobDescription;
  private BigDecimal jobBudget;
  private BigDecimal agreedBidBudget;
  private String jobCategory;
}

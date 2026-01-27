package com.devnerd.events.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobCompletedEvent {
  private Long jobId;
  private String jobTitle;
  private String jobDescription;
  private Long clientId;
  private Long completedById;
  private BigDecimal agreedBudget;
  private LocalDateTime completedAt;
  private String completionNotes;
}

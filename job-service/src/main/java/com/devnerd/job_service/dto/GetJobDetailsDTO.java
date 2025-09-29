package com.devnerd.job_service.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.devnerd.job_service.models.JobModel.JobStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetJobDetailsDTO {
  private Long jobId;
  private String title;
  private String description;
  private BigDecimal budget;
  private JobStatus status;
  private String category;
  private Long clientId;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}

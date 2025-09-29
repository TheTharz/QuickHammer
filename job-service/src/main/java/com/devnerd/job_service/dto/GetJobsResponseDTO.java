package com.devnerd.job_service.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetJobsResponseDTO {
  private List<JobSummaryDTO> jobs;
  private int page;
  private int size;
  private long totalElements;
  private int totalPages;
}

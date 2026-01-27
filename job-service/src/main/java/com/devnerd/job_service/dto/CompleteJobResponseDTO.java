package com.devnerd.job_service.dto;

import com.devnerd.job_service.models.JobModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompleteJobResponseDTO {
    private Long jobId;
    private String title;
    private JobModel.JobStatus status;
    private LocalDateTime completedAt;
    private String message;
}

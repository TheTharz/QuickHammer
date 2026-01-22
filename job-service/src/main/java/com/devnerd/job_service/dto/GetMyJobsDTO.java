package com.devnerd.job_service.dto;

import com.devnerd.job_service.models.JobModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetMyJobsDTO {
    private List<JobModel> jobs;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
}

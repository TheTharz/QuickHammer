package com.devnerd.job_service.services;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.devnerd.job_service.dto.GetJobsResponseDTO;
import com.devnerd.job_service.dto.JobCreateRequestDTO;
import com.devnerd.job_service.dto.JobCreatedResponseDTO;
import com.devnerd.job_service.dto.JobSummaryDTO;
import com.devnerd.job_service.models.JobModel;
import com.devnerd.job_service.repository.JobRepository;

@Service
public class JobService {
  private final JobRepository jobRepository;

  public JobService(JobRepository jobRepository) {
    this.jobRepository = jobRepository;
  }

  public JobCreatedResponseDTO createJob(JobCreateRequestDTO jobCreateRequestDTO) {

    JobModel jobModel = JobModel.builder()
      .title(jobCreateRequestDTO.getTitle())
      .description(jobCreateRequestDTO.getDescription())
      .budget(jobCreateRequestDTO.getBudget())
      .category(jobCreateRequestDTO.getCategory())
      .clientId(jobCreateRequestDTO.getClientId())
      .createdAt(LocalDateTime.now())
      .updatedAt(LocalDateTime.now())
      .status(JobModel.JobStatus.OPEN)
      .build();

    //save in the database
    JobModel job = jobRepository.save(jobModel);

    JobCreatedResponseDTO response = JobCreatedResponseDTO.builder().jobId(job.getJobId()).build();
    return response;

  }

  public GetJobsResponseDTO getAllJobSummaries(Integer page, Integer size) {
    Page<JobSummaryDTO> jobPage = jobRepository.findAllBy(
                PageRequest.of(page, size, Sort.by("createdAt").descending())
        );
    GetJobsResponseDTO response = GetJobsResponseDTO.builder()
            .jobs(jobPage.getContent())
            .page(jobPage.getNumber())
            .size(jobPage.getSize())
            .totalElements(jobPage.getTotalElements())
            .totalPages(jobPage.getTotalPages())
            .build();
            
    return response;
  }
  
}

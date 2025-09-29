package com.devnerd.job_service.services;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.devnerd.job_service.dto.JobCreateRequestDTO;
import com.devnerd.job_service.dto.JobCreatedResponseDTO;
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

    JobCreatedResponseDTO response = new JobCreatedResponseDTO(job.getJobId());
    return response;

  }
  
}

package com.devnerd.job_service.services;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.devnerd.events.models.BidAcceptedEvent;
import com.devnerd.events.models.JobAssignedEvent;
import com.devnerd.job_service.dto.GetJobDetailsDTO;
import com.devnerd.job_service.dto.GetJobsResponseDTO;
import com.devnerd.job_service.dto.JobCreateRequestDTO;
import com.devnerd.job_service.dto.JobCreatedResponseDTO;
import com.devnerd.job_service.dto.JobSummaryDTO;
import com.devnerd.job_service.events.producers.EventProducer;
import com.devnerd.job_service.models.JobModel;
import com.devnerd.job_service.repository.JobRepository;

import lombok.AllArgsConstructor;
import lombok.Data;

@Service
@Data
@AllArgsConstructor
public class JobService {
  private final JobRepository jobRepository;
  private final EventProducer eventProducer;

  public JobCreatedResponseDTO createJob(JobCreateRequestDTO jobCreateRequestDTO) {

    JobModel jobModel = JobModel.builder()
      .title(jobCreateRequestDTO.getTitle())
      .description(jobCreateRequestDTO.getDescription())
      .budget(jobCreateRequestDTO.getBudget())
      .category(jobCreateRequestDTO.getCategory())
      .clientId(jobCreateRequestDTO.getClientId())
      .assignedToId(null)
      .agreedBidBudget(null)
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

  public GetJobDetailsDTO getJobDetails(Long jobId) {

    JobModel job = jobRepository.findById(jobId)
            .orElseThrow(() -> new RuntimeException("Job not found"));
    GetJobDetailsDTO response = GetJobDetailsDTO.builder()
            .jobId(job.getJobId())
            .title(job.getTitle())
            .description(job.getDescription())
            .budget(job.getBudget())
            .status(job.getStatus())
            .category(job.getCategory())
            .clientId(job.getClientId())
            .createdAt(job.getCreatedAt())
            .updatedAt(job.getUpdatedAt())
            .build();
    return response;
  }

  public void updateJobOnBidAccept(BidAcceptedEvent event){ {
    JobModel job = jobRepository.findById(event.getJobId())
            .orElseThrow(() -> new RuntimeException("Job not found"));
    job.setStatus(JobModel.JobStatus.IN_PROGRESS);
    job.setAssignedToId(event.getAssignedToId());
    job.setAgreedBidBudget(event.getBidBudget());
    jobRepository.save(job);

    //publish the event of assigned job
    eventProducer.publishJobAssignedEvent(JobAssignedEvent.builder()
            .jobId(event.getJobId())
            .assignedToId(event.getAssignedToId())
            .bidId(event.getBidId())
            .clientId(job.getClientId())
            .jobTitle(job.getTitle())
            .jobDescription(job.getDescription())
            .jobBudget(job.getBudget())
            .agreedBidBudget(event.getBidBudget())
            .jobCategory(job.getCategory())
            .build());
  }
}
}

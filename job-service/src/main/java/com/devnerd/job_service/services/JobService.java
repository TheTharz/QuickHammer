package com.devnerd.job_service.services;

import java.time.LocalDateTime;

import com.devnerd.job_service.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.devnerd.events.models.BidAcceptedEvent;
import com.devnerd.events.models.JobAssignedEvent;
import com.devnerd.events.models.JobCompletedEvent;
import com.devnerd.job_service.events.producers.EventProducer;
import com.devnerd.job_service.models.JobModel;
import com.devnerd.job_service.repository.JobRepository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Service
@Data
@AllArgsConstructor
@Slf4j
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
    Page<JobSummaryDTO> jobPage = jobRepository.findByStatus(JobModel.JobStatus.OPEN,
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

  /**
   * Choreography Saga Participant - Handle Bid Accepted Event
   * Step 2 of Bid Acceptance Saga
   * 
   * If successful: Updates job status and publishes JobAssignedEvent
   * If fails: Publishes BidAcceptanceRollbackEvent to trigger compensation
   */
  public void updateJobOnBidAccept(BidAcceptedEvent event) {
    String sagaId = "BID_ACCEPT_" + event.getBidId(); // Generate saga ID based on bid
    
    try {
      log.info("[SAGA:{}] Processing BidAcceptedEvent for job: {}, bidId: {}", 
               sagaId, event.getJobId(), event.getBidId());
      
      JobModel job = jobRepository.findById(event.getJobId())
              .orElseThrow(() -> new RuntimeException("Job not found"));
      
      // Validate job state
      if (job.getStatus() != JobModel.JobStatus.OPEN) {
        throw new RuntimeException("Job is not in OPEN status. Current status: " + job.getStatus());
      }
      
      // Update job status to IN_PROGRESS
      job.setStatus(JobModel.JobStatus.IN_PROGRESS);
      job.setAssignedToId(event.getAssignedToId());
      job.setAgreedBidBudget(event.getBidBudget());
      jobRepository.save(job);
      
      log.info("[SAGA:{}] Job {} assigned to freelancer {}", 
               sagaId, event.getJobId(), event.getAssignedToId());

      // Publish JobAssignedEvent - continues the saga
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
      
      log.info("[SAGA:{}] Published JobAssignedEvent successfully", sagaId);
      
    } catch (Exception e) {
      log.error("[SAGA:{}] Failed to update job on bid accept. Triggering compensation. Error: {}", 
                sagaId, e.getMessage(), e);
      
      // Trigger compensation - rollback bid acceptance
      eventProducer.publishBidAcceptanceRollbackEvent(
          event.getBidId(), 
          event.getJobId(), 
          event.getAssignedToId(), 
          sagaId, 
          "Job assignment failed: " + e.getMessage()
      );
    }
  }

    public GetMyJobsDTO getMyJobs(Long userId,Integer page, Integer size) {
        Page<JobModel> jobPage = jobRepository.findByClientId(
                userId,
                PageRequest.of(page, size, Sort.by("createdAt").descending())
        );

        GetMyJobsDTO response = GetMyJobsDTO.builder()
                .jobs(jobPage.getContent())
                .page(jobPage.getNumber())
                .size(jobPage.getSize())
                .totalElements(jobPage.getTotalElements())
                .totalPages(jobPage.getTotalPages())
                .build();

        return response;
    }

    public GetMyJobsDTO getJobsAssignedToMe(Long userId, int page, int size) {
      Page<JobModel> jobPage = jobRepository.findByAssignedToId(
              userId,
              PageRequest.of(page, size, Sort.by("createdAt").descending()));
        GetMyJobsDTO response = GetMyJobsDTO.builder()
                .jobs(jobPage.getContent())
                .page(jobPage.getNumber())
                .size(jobPage.getSize())
                .totalElements(jobPage.getTotalElements())
                .totalPages(jobPage.getTotalPages())
                .build();
        return response;
    }

    public CompleteJobResponseDTO completeJob(Long jobId, Long userId, CompleteJobRequestDTO request) {
        log.info("Attempting to complete job. JobId: {}, UserId: {}", jobId, userId);
        
        JobModel job = jobRepository.findById(jobId)
                .orElseThrow(() -> {
                    log.error("Job not found. JobId: {}", jobId);
                    return new RuntimeException("Job not found");
                });

        // Validate that the user is assigned to this job
        if (job.getAssignedToId() == null || !job.getAssignedToId().equals(userId)) {
            log.error("Unauthorized: User {} is not assigned to job {}", userId, jobId);
            throw new RuntimeException("You are not assigned to this job");
        }

        // Validate job status
        if (job.getStatus() != JobModel.JobStatus.IN_PROGRESS) {
            log.error("Invalid job status for completion. JobId: {}, Status: {}", jobId, job.getStatus());
            throw new RuntimeException("Only jobs in IN_PROGRESS status can be completed. Current status: " + job.getStatus());
        }

        // Update job status to COMPLETED
        job.setStatus(JobModel.JobStatus.COMPLETED);
        job.setUpdatedAt(LocalDateTime.now());
        JobModel completedJob = jobRepository.save(job);
        
        log.info("Job completed successfully. JobId: {}, AssignedTo: {}, CompletedAt: {}", 
                 jobId, userId, completedJob.getUpdatedAt());

        // Publish job completed event
        JobCompletedEvent event = JobCompletedEvent.builder()
                .jobId(completedJob.getJobId())
                .jobTitle(completedJob.getTitle())
                .jobDescription(completedJob.getDescription())
                .clientId(completedJob.getClientId())
                .completedById(userId)
                .agreedBudget(completedJob.getAgreedBidBudget())
                .completedAt(completedJob.getUpdatedAt())
                .completionNotes(request.getCompletionNotes())
                .build();
        eventProducer.publishJobCompletedEvent(event);

        return CompleteJobResponseDTO.builder()
                .jobId(completedJob.getJobId())
                .title(completedJob.getTitle())
                .status(completedJob.getStatus())
                .completedAt(completedJob.getUpdatedAt())
                .message("Job completed successfully")
                .build();
    }
}

package com.devnerd.job_service.controllers;

import com.devnerd.job_service.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.devnerd.job_service.services.JobService;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("api/v1/jobs")
@Slf4j
public class JobController {
  private final JobService jobService;

  public JobController(JobService jobService) {
    this.jobService = jobService;
  }
  
  @PostMapping("/create-job")
  public ResponseEntity<JobCreatedResponseDTO> createJob(@RequestBody JobCreateRequestDTO jobCreateRequestDTO) {
    JobCreatedResponseDTO response = jobService.createJob(jobCreateRequestDTO);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/get-all-jobs")
  public ResponseEntity<GetJobsResponseDTO> getAllJobs(@RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size){
    GetJobsResponseDTO response = jobService.getAllJobSummaries(page, size);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/get-job-details")
  public ResponseEntity<GetJobDetailsDTO> getJobDetails(@RequestParam Long jobId) {
    GetJobDetailsDTO response = jobService.getJobDetails(jobId);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/get-my-jobs")
  public ResponseEntity<GetMyJobsDTO> getMyJobs(@RequestHeader("X-User-Id") Long userId,@RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {
      GetMyJobsDTO response = jobService.getMyJobs(userId, page, size);
      return ResponseEntity.ok(response);
  }

  @GetMapping("/jobs-assigned-to-me")
  public ResponseEntity<GetMyJobsDTO> getJobsAssignedToMe(@RequestHeader("X-User-Id") Long userId,@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
      GetMyJobsDTO response = jobService.getJobsAssignedToMe(userId, page, size);
      return ResponseEntity.ok(response);
  }

  @PostMapping("/complete")
  public ResponseEntity<CompleteJobResponseDTO> completeJob(
          @RequestHeader("X-User-Id") Long userId,
          @RequestBody CompleteJobRequestDTO request) {
      log.info("Received complete job request from userId: {} for jobId: {}", userId, request.getJobId());
      CompleteJobResponseDTO response = jobService.completeJob(request.getJobId(), userId, request);
      log.info("Job completion successful. JobId: {}, Status: {}", response.getJobId(), response.getStatus());
      return ResponseEntity.ok(response);
  }
}

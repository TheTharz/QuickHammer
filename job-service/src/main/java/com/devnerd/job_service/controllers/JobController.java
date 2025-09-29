package com.devnerd.job_service.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devnerd.job_service.dto.JobCreateRequestDTO;
import com.devnerd.job_service.dto.JobCreatedResponseDTO;
import com.devnerd.job_service.services.JobService;

@RestController
@RequestMapping("api/v1/job")
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
}

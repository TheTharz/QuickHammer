package com.devnerd.job_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devnerd.job_service.models.JobModel;

public interface JobRepository extends JpaRepository<JobModel, Long> {
  
}

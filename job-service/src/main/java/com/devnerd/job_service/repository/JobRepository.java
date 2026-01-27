package com.devnerd.job_service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.devnerd.job_service.dto.JobSummaryDTO;
import com.devnerd.job_service.models.JobModel;

public interface JobRepository extends JpaRepository<JobModel, Long> {
  Page<JobSummaryDTO> findAllBy(Pageable pageable);

    Page<JobModel> findByClientId(Long clientId, PageRequest createdAt);

    Page<JobSummaryDTO> findByStatus(JobModel.JobStatus status,PageRequest createdAt);

    Page<JobModel> findByAssignedToId(Long userId, PageRequest createdAt);
}

package com.devnerd.job_service.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "jobs")
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class JobModel {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long jobId;
  
  @Column(nullable = false)
  private String title;

  @Column(columnDefinition = "TEXT")
  private String description;

  @Column(nullable = false)
  private BigDecimal budget;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private JobStatus status;

  @Column(nullable = false)
  private String category;

  @Column(nullable = false)
  private Long clientId;

  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(nullable = false)
  private LocalDateTime updatedAt;

  @PrePersist
  protected void onCreate() {
      this.createdAt = LocalDateTime.now();
      this.status = JobStatus.OPEN;
  }
  @PreUpdate
  protected void onUpdate() {
      this.updatedAt = LocalDateTime.now();
  }

  public enum JobStatus {
      OPEN,
      IN_PROGRESS,
      COMPLETED,
      CANCELLED
  }
}

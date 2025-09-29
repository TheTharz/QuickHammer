package com.devnerd.bid_service.models;

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
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Table(
  name = "bids",
  uniqueConstraints = {
    @UniqueConstraint(columnNames = {"jobId", "bidderId"})
  }
)

@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Data
public class BidModel {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long bidId;

  private Long jobId;          // foreign key (job-service owns jobs)

  private Long bidderId;   // foreign key (auth-service owns users)

  private BigDecimal amount;

  @Column(columnDefinition = "TEXT")
  private String message;

  @Enumerated(EnumType.STRING)
  private BidStatus status; // PENDING, ACCEPTED, REJECTED, WITHDRAWN

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;

  @PrePersist
  protected void onCreate() {
    this.createdAt = LocalDateTime.now();
    this.status = BidStatus.PENDING;
  }

  @PreUpdate
  protected void onUpdate() {
    this.updatedAt = LocalDateTime.now();
  }

  public enum BidStatus {
    PENDING,
    ACCEPTED,
    REJECTED,
    WITHDRAWN
  }
}

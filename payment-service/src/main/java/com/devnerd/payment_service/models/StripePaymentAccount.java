package com.devnerd.payment_service.models;

import java.time.LocalDateTime;

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
import lombok.NoArgsConstructor;

@Entity
@Table(name = "StripePaymentAccount")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class StripePaymentAccount {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long freelancerId;
   
  private String stripeAccountId;
  
  @Enumerated(EnumType.STRING)
  private StripeAccountStatus status;

  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  @PrePersist
  protected void onCreate() {
    this.createdAt = LocalDateTime.now();
    this.status = StripeAccountStatus.PENDING;
  }

  @PreUpdate
  protected void onUpdate() {
    this.updatedAt = LocalDateTime.now();
  }

  public enum StripeAccountStatus {
        PENDING,
        VERIFIED,
        REJECTED,
        ACTIVE
    }
}

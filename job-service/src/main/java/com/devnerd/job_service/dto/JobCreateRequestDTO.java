package com.devnerd.job_service.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class JobCreateRequestDTO {
  private String title;
  private String description;
  private BigDecimal budget;
  private String category;
  private Long clientId;
}

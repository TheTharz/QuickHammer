package com.devnerd.job_service.dto;

import java.math.BigDecimal;

public interface JobSummaryDTO {

  Long getJobId();       // must match entity field name
  String getTitle();
  BigDecimal getBudget();
}
package com.devnerd.payment_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OnBoardRequestDTO {
  private Long freelancerId;
  private String email;
  private String frontendUrl;
}

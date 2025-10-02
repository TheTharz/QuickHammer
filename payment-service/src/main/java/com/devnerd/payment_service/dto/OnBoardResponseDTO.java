package com.devnerd.payment_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class OnBoardResponseDTO {
  private String stripeRedirectURL;
}

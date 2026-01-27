package com.devnerd.payment_service.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devnerd.payment_service.dto.OnBoardRequestDTO;
import com.devnerd.payment_service.dto.OnBoardResponseDTO;
import com.devnerd.payment_service.services.PaymentService;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("api/v1/payments")
@Data
@AllArgsConstructor
@Slf4j
public class PaymentController {

  private final PaymentService paymentService;

  @PostMapping("/onboard")
  public ResponseEntity<OnBoardResponseDTO> onBoard(@RequestBody OnBoardRequestDTO onBoardRequestDTO) {
    log.info("Received onboarding request for freelancerId: {}", onBoardRequestDTO.getFreelancerId());
    OnBoardResponseDTO onBoardResponseDTO = paymentService.onBoardFreelancer(onBoardRequestDTO);
    log.info("Onboarding response prepared successfully for freelancerId: {}", onBoardRequestDTO.getFreelancerId());
    return ResponseEntity.ok(onBoardResponseDTO);
  }
}

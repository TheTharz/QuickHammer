package com.devnerd.payment_service.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devnerd.payment_service.dto.OnBoardRequestDTO;
import com.devnerd.payment_service.dto.OnBoardResponseDTO;
import com.devnerd.payment_service.services.PaymentService;

import lombok.AllArgsConstructor;
import lombok.Data;

@RestController
@RequestMapping("api/v1/payment")
@Data
@AllArgsConstructor
public class PaymentController {

  private final PaymentService paymentService;

  @PostMapping("/onboard")
  public ResponseEntity<OnBoardResponseDTO> onBoard(OnBoardRequestDTO onBoardRequestDTO) {
    OnBoardResponseDTO onBoardResponseDTO = paymentService.onBoardFreelancer(onBoardRequestDTO);
    return ResponseEntity.ok(onBoardResponseDTO);
  }
}

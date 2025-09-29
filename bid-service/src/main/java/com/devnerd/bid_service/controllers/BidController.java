package com.devnerd.bid_service.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devnerd.bid_service.dto.CreateBidRequestDTO;
import com.devnerd.bid_service.dto.CreateBidResponseDTO;
import com.devnerd.bid_service.services.BidService;

@RestController
@RequestMapping("/api/v1/bid")
public class BidController {

  private final BidService bidService;

  public BidController(BidService bidService) {
    this.bidService = bidService;
  }
  
  @PostMapping("/create-bid")
  public ResponseEntity<CreateBidResponseDTO> createBid(@RequestBody CreateBidRequestDTO createBidRequestDTO) {
    CreateBidResponseDTO response = bidService.createBid(createBidRequestDTO);
    return ResponseEntity.ok(response);
  }
}

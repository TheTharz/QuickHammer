package com.devnerd.bid_service.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.devnerd.bid_service.dto.CreateBidRequestDTO;
import com.devnerd.bid_service.dto.CreateBidResponseDTO;
import com.devnerd.bid_service.dto.UpdateBidRequestDTO;
import com.devnerd.bid_service.dto.UpdateBidResponseDTO;
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

  @PutMapping("/update-bid")
  public ResponseEntity<UpdateBidResponseDTO> updateBid(@RequestParam Long bidId, @RequestBody UpdateBidRequestDTO updateBidRequestDTO) {
    UpdateBidResponseDTO response = bidService.updateBid(updateBidRequestDTO, bidId);
    return ResponseEntity.ok(response);
  }
}

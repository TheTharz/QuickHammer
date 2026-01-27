package com.devnerd.bid_service.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.devnerd.bid_service.dto.CreateBidRequestDTO;
import com.devnerd.bid_service.dto.CreateBidResponseDTO;
import com.devnerd.bid_service.dto.GetBidsByJobResponseDTO;
import com.devnerd.bid_service.dto.UpdateBidRequestDTO;
import com.devnerd.bid_service.dto.UpdateBidResponseDTO;
import com.devnerd.bid_service.services.BidService;

@RestController
@RequestMapping("/api/v1/bids")
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

  @GetMapping("/bids-by-job")
  public ResponseEntity<GetBidsByJobResponseDTO> getBidsByJob(@RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,@RequestParam Long jobId) {
    GetBidsByJobResponseDTO response = bidService.getBidsByJob(jobId, page, size);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/accept-bid")
  public ResponseEntity<UpdateBidResponseDTO> acceptBid(@RequestParam Long bidId) {
    UpdateBidResponseDTO response = bidService.acceptBid(bidId);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/bids-placed-by-me")
  public ResponseEntity<GetBidsByJobResponseDTO> getBidsPlacedByMe(@RequestHeader("X-User-Id") Long userId, @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        GetBidsByJobResponseDTO response = bidService.getBidsPlacedByMe(userId, page, size);
        return ResponseEntity.ok(response);
    }
}

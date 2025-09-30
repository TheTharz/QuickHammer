package com.devnerd.bid_service.services;

import org.springframework.stereotype.Service;

import com.devnerd.bid_service.dto.CreateBidRequestDTO;
import com.devnerd.bid_service.dto.CreateBidResponseDTO;
import com.devnerd.bid_service.dto.UpdateBidRequestDTO;
import com.devnerd.bid_service.dto.UpdateBidResponseDTO;
import com.devnerd.bid_service.models.BidModel;
import com.devnerd.bid_service.models.BidModel.BidStatus;
import com.devnerd.bid_service.repository.BidRepository;

@Service
public class BidService {

  private final BidRepository bidRepository;

  public BidService(BidRepository bidRepository) {
    this.bidRepository = bidRepository;
  }

  public CreateBidResponseDTO createBid(CreateBidRequestDTO createBidRequestDTO) {
    BidModel bidModel = BidModel.builder()
      .jobId(Long.parseLong(createBidRequestDTO.getJobId()))
      .bidderId(Long.parseLong(createBidRequestDTO.getBidderId()))
      .amount(createBidRequestDTO.getAmount())
      .message(createBidRequestDTO.getMessage())
      .build();

    // save to db
    BidModel savedBid = bidRepository.save(bidModel);

    CreateBidResponseDTO response = CreateBidResponseDTO.builder().bidId(savedBid.getBidId()).build();
    return response;
  }

  public UpdateBidResponseDTO updateBid(UpdateBidRequestDTO updateBidRequestDTO, Long bidId) {
    BidModel bid =bidRepository.findById(bidId)
      .orElseThrow(() -> new RuntimeException("Bid not found"));

    if(bid.getStatus() != BidStatus.PENDING){
      throw new RuntimeException("Only pending bids can be updated");
    }

    bid.setAmount(updateBidRequestDTO.getAmount());
    bid.setMessage(updateBidRequestDTO.getMessage());
    bidRepository.save(bid);

    UpdateBidResponseDTO response = UpdateBidResponseDTO.builder().bidId(bid.getBidId()).build();
    return response;
  }
  
}

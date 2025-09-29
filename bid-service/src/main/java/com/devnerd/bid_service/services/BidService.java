package com.devnerd.bid_service.services;

import org.springframework.stereotype.Service;

import com.devnerd.bid_service.dto.CreateBidRequestDTO;
import com.devnerd.bid_service.dto.CreateBidResponseDTO;
import com.devnerd.bid_service.models.BidModel;
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
  
}

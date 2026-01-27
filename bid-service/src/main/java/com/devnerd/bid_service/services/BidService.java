package com.devnerd.bid_service.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.devnerd.bid_service.dto.BidSummaryDTO;
import com.devnerd.bid_service.dto.CreateBidRequestDTO;
import com.devnerd.bid_service.dto.CreateBidResponseDTO;
import com.devnerd.bid_service.dto.GetBidsByJobResponseDTO;
import com.devnerd.bid_service.dto.UpdateBidRequestDTO;
import com.devnerd.bid_service.dto.UpdateBidResponseDTO;
import com.devnerd.bid_service.events.producers.EventProducer;
import com.devnerd.bid_service.models.BidModel;
import com.devnerd.bid_service.models.BidModel.BidStatus;
import com.devnerd.bid_service.repository.BidRepository;
import com.devnerd.events.models.BidAcceptedEvent;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BidService {

  private final BidRepository bidRepository;
  private final EventProducer eventProducer;

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

  public GetBidsByJobResponseDTO getBidsByJob(Long jobId, Integer page, Integer size) {
    Page<BidSummaryDTO> bids = bidRepository.findByJobId(
      jobId,
      PageRequest.of(page, size, Sort.by("updatedAt").descending())
    );
    GetBidsByJobResponseDTO response = GetBidsByJobResponseDTO.builder().bids(
      bids.getContent())
      .page(bids.getNumber())
      .size(bids.getSize())
      .totalElements(bids.getTotalElements())
      .totalPages(bids.getTotalPages())
      .build();

    return response;
  }

  public UpdateBidResponseDTO acceptBid(Long bidId) {
    BidModel bid =bidRepository.findById(bidId)
      .orElseThrow(() -> new RuntimeException("Bid not found"));

    if(bid.getStatus() != BidStatus.PENDING){
      throw new RuntimeException("Only pending bids can be accepted");
    }

    Long jobId = bid.getJobId();

    bid.setStatus(BidStatus.ACCEPTED);

    //update job status to inprogress emit event
    eventProducer.publishBidAcceptedEvent(BidAcceptedEvent.builder().jobId(jobId).bidId(bidId).assignedToId(bid.getBidderId()).bidBudget(bid.getAmount()).build());

    //invalidate other bids on this jobId other than this bidId
    bidRepository.rejectOtherBids(jobId, bidId);

    bidRepository.save(bid);

    UpdateBidResponseDTO response = UpdateBidResponseDTO.builder().bidId(bid.getBidId()).build();
    return response;
  }

    public GetBidsByJobResponseDTO getBidsPlacedByMe(Long userId, int page, int size) {
        Page<BidSummaryDTO> bids = bidRepository.findByBidderId(
                userId,
                PageRequest.of(page, size, Sort.by("updatedAt").descending())
        );
        GetBidsByJobResponseDTO response = GetBidsByJobResponseDTO.builder().bids(
                        bids.getContent())
                .page(bids.getNumber())
                .size(bids.getSize())
                .totalElements(bids.getTotalElements())
                .totalPages(bids.getTotalPages())
                .build();

        return response;
    }
}

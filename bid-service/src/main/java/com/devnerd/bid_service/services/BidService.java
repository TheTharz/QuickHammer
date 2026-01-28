package com.devnerd.bid_service.services;

import java.util.List;
import java.util.UUID;

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
import com.devnerd.events.models.BidRejectedEvent;
import com.devnerd.events.models.UserBidStatisticsEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Bid Service implementing Choreography Saga Pattern
 * 
 * Saga Flow: Bid Acceptance -> Job Assignment -> Bid Rejections -> Notifications
 * Compensation: If job assignment fails, bid acceptance is rolled back
 */
@Service
@RequiredArgsConstructor
@Slf4j
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

  /**
   * Accept a bid - Start of Choreography Saga
   * 
   * Saga Steps:
   * 1. Accept bid and save to DB
   * 2. Publish BidAcceptedEvent with sagaId
   * 3. Reject other bids and publish BidRejectedEvent for each
   * 4. Publish UserBidStatisticsEvent for winner
   * 
   * Downstream services will listen to these events:
   * - Job Service: Will update job status to IN_PROGRESS
   * - Notification Service: Will send notifications
   * - User Service: Will update user statistics
   */
  public UpdateBidResponseDTO acceptBid(Long bidId) {
    // Generate unique Saga ID to track this transaction
    String sagaId = UUID.randomUUID().toString();
    
    log.info("[SAGA:{}] Starting Bid Acceptance Saga for bidId: {}", sagaId, bidId);
    
    BidModel bid = bidRepository.findById(bidId)
      .orElseThrow(() -> new RuntimeException("Bid not found"));

    if(bid.getStatus() != BidStatus.PENDING){
      throw new RuntimeException("Only pending bids can be accepted");
    }

    Long jobId = bid.getJobId();
    Long winnerId = bid.getBidderId();

    // Step 1: Update bid status to ACCEPTED
    bid.setStatus(BidStatus.ACCEPTED);
    bidRepository.save(bid);
    log.info("[SAGA:{}] Bid {} accepted successfully", sagaId, bidId);

    // Step 2: Publish BidAcceptedEvent - triggers job assignment in Job Service
    BidAcceptedEvent bidAcceptedEvent = BidAcceptedEvent.builder()
        .jobId(jobId)
        .bidId(bidId)
        .assignedToId(winnerId)
        .bidBudget(bid.getAmount())
        .build();
    eventProducer.publishBidAcceptedEvent(bidAcceptedEvent);
    log.info("[SAGA:{}] Published BidAcceptedEvent for job: {}", sagaId, jobId);

    // Step 3: Reject other bids and publish events
    List<BidModel> otherBids = bidRepository.findPendingBidsByJobExcluding(jobId, bidId);
    log.info("[SAGA:{}] Found {} other bids to reject for job: {}", sagaId, otherBids.size(), jobId);
    
    for (BidModel otherBid : otherBids) {
      otherBid.setStatus(BidStatus.REJECTED);
      bidRepository.save(otherBid);
      
      // Publish BidRejectedEvent for each rejected bid
      BidRejectedEvent rejectedEvent = BidRejectedEvent.builder()
          .bidId(otherBid.getBidId())
          .jobId(jobId)
          .bidderId(otherBid.getBidderId())
          .bidAmount(otherBid.getAmount())
          .rejectionReason("Another bid was accepted")
          .acceptedBidId(bidId)
          .sagaId(sagaId)
          .build();
      eventProducer.publishBidRejectedEvent(rejectedEvent);
      
      log.info("[SAGA:{}] Rejected bid: {} for user: {}", sagaId, otherBid.getBidId(), otherBid.getBidderId());
    }

    log.info("[SAGA:{}] Bid Acceptance Saga completed successfully for bid: {}", sagaId, bidId);

    UpdateBidResponseDTO response = UpdateBidResponseDTO.builder().bidId(bid.getBidId()).build();
    return response;
  }
  
  /**
   * Compensating transaction - Rollback bid acceptance
   * This is called when job assignment fails in Job Service
   */
  public void rollbackBidAcceptance(Long bidId, String sagaId, String reason) {
    log.warn("[SAGA:{}] Rolling back bid acceptance for bidId: {}. Reason: {}", sagaId, bidId, reason);
    
    BidModel bid = bidRepository.findById(bidId).orElse(null);
    if (bid != null && bid.getStatus() == BidStatus.ACCEPTED) {
      bid.setStatus(BidStatus.PENDING);
      bidRepository.save(bid);
      log.info("[SAGA:{}] Bid {} rolled back to PENDING status", sagaId, bidId);
      
      // Re-enable other bids that were rejected
      List<BidModel> rejectedBids = bidRepository.findRejectedBidsByJob(bid.getJobId());
      for (BidModel rejectedBid : rejectedBids) {
        rejectedBid.setStatus(BidStatus.PENDING);
        bidRepository.save(rejectedBid);
        log.info("[SAGA:{}] Bid {} restored to PENDING status", sagaId, rejectedBid.getBidId());
      }
    }
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

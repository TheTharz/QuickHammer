package com.devnerd.bid_service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.devnerd.bid_service.dto.BidSummaryDTO;
import com.devnerd.bid_service.models.BidModel;

import jakarta.transaction.Transactional;

public interface BidRepository extends JpaRepository<BidModel, Long> {

  Page<BidSummaryDTO> findByJobId(Long jobId,Pageable pageable);

  @Transactional
  @Modifying
  @Query("UPDATE BidModel b SET b.status = 'REJECTED' WHERE b.jobId = :jobId AND b.bidId <> :acceptedBidId AND b.status = 'PENDING'")
  int rejectOtherBids(Long jobId, Long acceptedBidId);

    Page<BidSummaryDTO> findByBidderId(Long userId, PageRequest updatedAt);
}

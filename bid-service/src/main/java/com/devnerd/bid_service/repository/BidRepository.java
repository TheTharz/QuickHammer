package com.devnerd.bid_service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.devnerd.bid_service.dto.BidSummaryDTO;
import com.devnerd.bid_service.models.BidModel;

public interface BidRepository extends JpaRepository<BidModel, Long> {

  Page<BidSummaryDTO> findByJobId(Long jobId,Pageable pageable);
}

package com.devnerd.bid_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devnerd.bid_service.models.BidModel;

public interface BidRepository extends JpaRepository<BidModel, Long> {

  
}

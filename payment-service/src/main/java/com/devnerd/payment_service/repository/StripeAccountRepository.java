package com.devnerd.payment_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devnerd.payment_service.models.StripePaymentAccount;

public interface StripeAccountRepository extends JpaRepository<StripePaymentAccount, Long> {
  StripePaymentAccount findByStripeAccountId(String stripeAccountId);
}

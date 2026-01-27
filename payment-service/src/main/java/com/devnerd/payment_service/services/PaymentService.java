package com.devnerd.payment_service.services;

import org.springframework.stereotype.Service;

import com.devnerd.payment_service.config.StripeConfiguration;
import com.devnerd.payment_service.dto.OnBoardRequestDTO;
import com.devnerd.payment_service.dto.OnBoardResponseDTO;
import com.devnerd.payment_service.exceptions.StripeServiceException;
import com.devnerd.payment_service.models.StripePaymentAccount;
import com.devnerd.payment_service.repository.StripeAccountRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.Account;
import com.stripe.model.AccountLink;
import com.stripe.net.RequestOptions;
import com.stripe.param.AccountCreateParams;
import com.stripe.param.AccountLinkCreateParams;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class PaymentService {
  private final StripeConfiguration paymentConfig;
  private final RequestOptions requestOptions;
  private final StripeAccountRepository stripeAccountRepository;

  public OnBoardResponseDTO onBoardFreelancer(OnBoardRequestDTO onBoardRequestDTO) {
    log.info("Starting freelancer onboarding for freelancerId: {}, email: {}", 
             onBoardRequestDTO.getFreelancerId(), onBoardRequestDTO.getEmail());
    
    try {
      AccountCreateParams accountCreateParams = AccountCreateParams.builder()
      .setType(AccountCreateParams.Type.EXPRESS)
                  .setCountry("US")
                  .setEmail(onBoardRequestDTO.getEmail())
                  .setCapabilities(
                          AccountCreateParams.Capabilities.builder()
                                  .setCardPayments(
                                          AccountCreateParams.Capabilities.CardPayments.builder().setRequested(true).build()
                                  )
                                  .setTransfers(
                                          AccountCreateParams.Capabilities.Transfers.builder().setRequested(true).build()
                                  )
                                  .build()
                  ).build();

      Account account = Account.create(accountCreateParams, requestOptions);
      log.info("Stripe account created successfully. AccountId: {}", account.getId());

      StripePaymentAccount stripePaymentAccount = StripePaymentAccount.builder()
                  .freelancerId(onBoardRequestDTO.getFreelancerId())
                  .stripeAccountId(account.getId())
                  .build();
      stripeAccountRepository.save(stripePaymentAccount);
      log.info("Stripe payment account saved to database for freelancerId: {}", onBoardRequestDTO.getFreelancerId());

      AccountLinkCreateParams linkParams = AccountLinkCreateParams.builder()
                  .setAccount(account.getId())
                  .setRefreshUrl(paymentConfig.getFrontendUrl() + "/onboarding/refresh")
                  .setReturnUrl(paymentConfig.getFrontendUrl() + "/onboarding/success")
                  .setType(AccountLinkCreateParams.Type.ACCOUNT_ONBOARDING)
                  .build();
      AccountLink accountLink = AccountLink.create(linkParams, requestOptions);

      OnBoardResponseDTO response = OnBoardResponseDTO.builder()
                  .stripeRedirectURL(accountLink.getUrl())
                  .build();

      log.info("Freelancer onboarding completed successfully. FreelancerId: {}, RedirectURL generated", 
               onBoardRequestDTO.getFreelancerId());
      return response;
    } catch (StripeException e) {
      log.error("Stripe error during freelancer onboarding for freelancerId: {}", 
                onBoardRequestDTO.getFreelancerId(), e);
      throw new StripeServiceException("Stripe error during freelancer onboarding", e);
    }
    
    
  }
  
}

package com.devnerd.payment_service.handlers;

import org.springframework.stereotype.Component;

import com.devnerd.payment_service.models.StripePaymentAccount;
import com.devnerd.payment_service.repository.StripeAccountRepository;
import com.stripe.model.Account;
import com.stripe.model.Event;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@AllArgsConstructor
@Slf4j
public class AccountUpdatedHandler implements StripeEventHandler {
  private final StripeAccountRepository stripeAccountRepository;

  @Override
  public String getEventType() {
    return "account.updated";
  }

  @Override
  public void handle(Event event) {
    log.info("Handling account.updated event: {}", event.getId());
    Account account = (Account) event.getDataObjectDeserializer().getObject().orElse(null);
        if (account != null && Boolean.TRUE.equals(account.getChargesEnabled())) {
            log.info("Account charges enabled for Stripe accountId: {}", account.getId());
            StripePaymentAccount stripeAccount = stripeAccountRepository.findByStripeAccountId(account.getId());
            if (stripeAccount != null) {
                stripeAccount.setStatus(StripePaymentAccount.StripeAccountStatus.ACTIVE);
                stripeAccountRepository.save(stripeAccount);
                log.info("Stripe account status updated to ACTIVE for freelancerId: {}, stripeAccountId: {}", 
                         stripeAccount.getFreelancerId(), account.getId());
            } else {
                log.warn("No StripePaymentAccount found for stripeAccountId: {}", account.getId());
            }
        } else {
            log.debug("Account update event received but charges not enabled yet for accountId: {}", 
                     account != null ? account.getId() : "null");
        }
  }
  
}

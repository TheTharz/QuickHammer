package com.devnerd.payment_service.handlers;

import org.springframework.stereotype.Component;

import com.devnerd.payment_service.models.StripePaymentAccount;
import com.devnerd.payment_service.repository.StripeAccountRepository;
import com.stripe.model.Account;
import com.stripe.model.Event;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class AccountUpdatedHandler implements StripeEventHandler {
  private final StripeAccountRepository stripeAccountRepository;

  @Override
  public String getEventType() {
    return "account.updated";
  }

  @Override
  public void handle(Event event) {
    Account account = (Account) event.getDataObjectDeserializer().getObject().orElse(null);
        if (account != null && Boolean.TRUE.equals(account.getChargesEnabled())) {
            StripePaymentAccount stripeAccount = stripeAccountRepository.findByStripeAccountId(account.getId());
            if (stripeAccount != null) {
                stripeAccount.setStatus(StripePaymentAccount.StripeAccountStatus.ACTIVE);
                stripeAccountRepository.save(stripeAccount);
            }
        }
  }
  
}

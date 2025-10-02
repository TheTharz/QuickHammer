package com.devnerd.payment_service.handlers;

import com.stripe.model.Event;

public interface StripeEventHandler {
  void handle(Event event);
  String getEventType();
}

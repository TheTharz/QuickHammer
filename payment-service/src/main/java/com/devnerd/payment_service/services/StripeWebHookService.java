package com.devnerd.payment_service.services;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.devnerd.payment_service.handlers.StripeEventHandler;
import com.stripe.model.Event;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class StripeWebHookService {
  private final Map<String, StripeEventHandler> handlerRegistry;

  public StripeWebHookService(List<StripeEventHandler> eventHandlers) {
        // Map event type -> handler
        this.handlerRegistry = eventHandlers.stream()
                .collect(Collectors.toMap(StripeEventHandler::getEventType, h -> h));
    }
  
  public void handleEvent(Event event) {
    String eventType = event.getType();
    StripeEventHandler stripeEventHandler = handlerRegistry.get(eventType);
    if (stripeEventHandler != null) {
      stripeEventHandler.handle(event);
    }else{
      log.warn("No handler found for event type: {}", eventType);
    }
  }
}

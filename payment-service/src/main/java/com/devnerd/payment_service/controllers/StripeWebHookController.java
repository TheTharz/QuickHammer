package com.devnerd.payment_service.controllers;

import java.io.BufferedReader;
import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.devnerd.payment_service.services.StripeWebHookService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;

@RestController("/api/v1/stripe-webhook")
@AllArgsConstructor
public class StripeWebHookController {
  private final StripeWebHookService webhookService;
  private final String endpointSecret = System.getenv("STRIPE_WEBHOOK_SECRET");

    @PostMapping
    public ResponseEntity<String> handleWebhook(HttpServletRequest request,@RequestHeader("Stripe-Signature") String sigHeader) throws SignatureVerificationException, IOException {
    
      //re do the signature validation
      //re do the exception handling
      String payload = new BufferedReader(request.getReader())
              .lines()
              .reduce("", (acc, line) -> acc + line);
      Event event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
      webhookService.handleEvent(event);
      return ResponseEntity.ok("Received");
    }
}

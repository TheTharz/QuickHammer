package com.devnerd.payment_service.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devnerd.payment_service.exceptions.StripeServiceException;
import com.devnerd.payment_service.services.StripeWebHookService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/stripe-webhook")
@RequiredArgsConstructor
public class StripeWebHookController {
  private final StripeWebHookService webhookService;

  @Value("${payment.webhook-secret}")
  private String endpointSecret;

    @PostMapping
    public ResponseEntity<String> handleWebhook(HttpServletRequest request,@RequestHeader("Stripe-Signature") String sigHeader) {
      try{
      String payload = request.getReader()
                .lines()
                .reduce("", (acc, line) -> acc + line);
      Event event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
      webhookService.handleEvent(event);
      return ResponseEntity.ok("Received");
      }catch (SignatureVerificationException e) {
        throw new StripeServiceException("Invalid signature", e);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
}

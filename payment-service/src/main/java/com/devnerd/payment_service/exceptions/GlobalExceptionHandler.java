package com.devnerd.payment_service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.stripe.exception.StripeException;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleAuthenticationException(Exception ex) {
    log.error("Unhandled exception occurred: {}", ex.getMessage(), ex);
    ErrorResponse error = new ErrorResponse(ex.getMessage(), 500, System.currentTimeMillis());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
  }

  @ExceptionHandler(StripeException.class)
  public ResponseEntity<ErrorResponse> handleStripeException(StripeException ex) {
    log.error("Stripe exception occurred: {}", ex.getMessage(), ex);
    ErrorResponse error = new ErrorResponse(ex.getMessage(), 500, System.currentTimeMillis());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
  }
  
  @ExceptionHandler(StripeServiceException.class)
  public ResponseEntity<ErrorResponse> handleStripeServiceException(StripeServiceException ex) {
    log.error("Stripe service exception occurred: {}", ex.getMessage(), ex);
    ErrorResponse error = new ErrorResponse(ex.getMessage(), 500, System.currentTimeMillis());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
  }
}

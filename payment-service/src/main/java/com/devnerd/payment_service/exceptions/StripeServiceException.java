package com.devnerd.payment_service.exceptions;

public class StripeServiceException extends RuntimeException {
   public StripeServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}

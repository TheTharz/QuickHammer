package com.devnerd.auth_service.exception;

public class WeakPasswordException extends RuntimeException{
  public WeakPasswordException(String message) {
        super(message);
    }
}

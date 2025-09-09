package com.devnerd.auth_service.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordUtils {

  private static final PasswordEncoder encoder = new BCryptPasswordEncoder();

  public static String hashPassword(String password) {
    return encoder.encode(password);
  }

  public static boolean isStrongPassword(String password) {
        // minimum 8 chars, at least one uppercase, one number, one special char
        String regex = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        return password.matches(regex);
  }
}

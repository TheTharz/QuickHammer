package com.devnerd.payment_service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.stripe.net.RequestOptions;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "payment")
@Data
public class StripeConfiguration {
  private String stripeSecretKey;
  private String frontendUrl;
  private String webhookSecret;

  @Bean
  public RequestOptions requestOptions() {
    return RequestOptions.builder()
                .setApiKey(stripeSecretKey)
                .build();
  }
}

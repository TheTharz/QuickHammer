package com.devnerd.user_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@EnableRedisRepositories(basePackages = "com.devnerd.user_service.repositories.redis")
@Configuration
public class RedisConfig 
{
  @Value("${redis.host}")
  private String host;

  @Value("${redis.port}")
  private int port;

  @Bean
  public RedisConnectionFactory redisConnectionFactory(){
    return new LettuceConnectionFactory(
      host,
      port
    );
  } 

  @Bean
  public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
    RedisTemplate<String, Object> template = new RedisTemplate<>();
    template.setConnectionFactory(connectionFactory);
        
    // Use String serializer for keys
    template.setKeySerializer(new StringRedisSerializer());
    template.setHashKeySerializer(new StringRedisSerializer());
      
    // Use JSON serializer for values
    template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
    template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
      
    return template;
  }

}

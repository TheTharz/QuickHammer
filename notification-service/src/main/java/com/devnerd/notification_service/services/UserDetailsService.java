package com.devnerd.notification_service.services;

import org.springframework.stereotype.Service;

import com.devnerd.notification_service.clients.UserClient;
import com.devnerd.notification_service.dto.UserDetailsReponseDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service responsible for fetching user details
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsService {
    
    private final UserClient userClient;
    
    /**
     * Fetch user details with circuit breaker protection
     * 
     * @param userId User ID to fetch
     * @return UserDetailsReponseDTO or null if service unavailable
     */
    public UserDetailsReponseDTO getUserDetails(Long userId) {
        log.debug("Fetching user details for userId: {}", userId);
        
        UserDetailsReponseDTO user = userClient.getUser(userId);
        
        if (user == null) {
            log.warn("User service unavailable (circuit breaker open) for userId: {}", userId);
        } else {
            log.debug("Successfully fetched user details for userId: {}", userId);
        }
        
        return user;
    }
}

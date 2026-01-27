package com.devnerd.notification_service.services;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service responsible for sending emails
 * */
@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    
    private final JavaMailSender javaMailSender;
    
    /**
     * Send an email
     * 
     * @param to Recipient email address
     * @param subject Email subject
     * @param body Email body
     * @return true if sent successfully, false otherwise
     */
    public boolean sendEmail(String to, String subject, String body) {
        try {
            log.info("Sending email to: {} with subject: {}", to, subject);
            
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            
            javaMailSender.send(message);
            
            log.info("Email sent successfully to: {}", to);
            return true;
            
        } catch (Exception e) {
            log.error("Failed to send email to: {}. Error: {}", to, e.getMessage(), e);
            return false;
        }
    }
}

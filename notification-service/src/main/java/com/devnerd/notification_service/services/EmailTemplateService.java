package com.devnerd.notification_service.services;

import org.springframework.stereotype.Service;

import com.devnerd.events.models.BidRejectedEvent;
import com.devnerd.events.models.JobAssignedEvent;
import com.devnerd.events.models.JobCompletedEvent;
import com.devnerd.notification_service.dto.UserDetailsReponseDTO;

/**
 * Service responsible for composing email templates
 * Supports Choreography Saga patterns
 */
@Service
public class EmailTemplateService {
    
    /**
     * Build subject for job assignment email
     */
    public String buildJobAssignedSubject(JobAssignedEvent event) {
        return "You have been assigned a job: " + event.getJobTitle();
    }
    
    /**
     * Build body for job assignment email
     */
    public String buildJobAssignedBody(JobAssignedEvent event, UserDetailsReponseDTO user) {
        return "Hi " + user.getFirstName() + " " + user.getLastName() + ",\n\n" +
               "You have been assigned a new job.\n\n" +
               "Job Title: " + event.getJobTitle() + "\n" +
               "Description: " + event.getJobDescription() + "\n" +
               "Initial Budget: $" + event.getJobBudget() + "\n\n" +
               "Agreed Budget: $" + event.getAgreedBidBudget() + "\n\n" +
               "Please check your dashboard for more details.\n\n" +
               "Regards,\nTeam DevNerd";
    }
    
    /**
     * Build subject for job completion email
     */
    public String buildJobCompletedSubject(JobCompletedEvent event) {
        return "Job Completed: " + event.getJobTitle();
    }
    
    /**
     * Build body for job completion email
     */
    public String buildJobCompletedBody(JobCompletedEvent event, 
                                        UserDetailsReponseDTO client, 
                                        UserDetailsReponseDTO freelancer) {
        return "Hi " + client.getFirstName() + " " + client.getLastName() + ",\n\n" +
               "Great news! Your job has been completed.\n\n" +
               "Job Title: " + event.getJobTitle() + "\n" +
               "Description: " + event.getJobDescription() + "\n" +
               "Agreed Budget: $" + event.getAgreedBudget() + "\n" +
               "Completed By: " + freelancer.getFirstName() + " " + freelancer.getLastName() + "\n" +
               "Completed At: " + event.getCompletedAt() + "\n\n" +
               (event.getCompletionNotes() != null ? "Completion Notes: " + event.getCompletionNotes() + "\n\n" : "") +
               "Please review the work and proceed with payment if satisfied.\n\n" +
               "Regards,\nTeam QuickHammer";
    }
    
    /**
     * Build message for pending notification (when email can't be sent)
     */
    public String buildPendingNotificationMessage(String jobTitle, String reason) {
        return "Job: " + jobTitle + " (Email pending - " + reason + ")";
    }
    
    /**
     * Build subject for bid rejection email
     * Part of Choreography Saga: Bid Acceptance Flow
     */
    public String buildBidRejectedSubject(BidRejectedEvent event) {
        return "Your bid was not selected - Job #" + event.getJobId();
    }
    
    /**
     * Build body for bid rejection email
     * Part of Choreography Saga: Bid Acceptance Flow
     */
    public String buildBidRejectedBody(BidRejectedEvent event, UserDetailsReponseDTO user) {
        return "Hi " + user.getFirstName() + " " + user.getLastName() + ",\n\n" +
               "Thank you for submitting your bid on QuickHammer.\n\n" +
               "Unfortunately, your bid was not selected for Job #" + event.getJobId() + ".\n\n" +
               "Your Bid Amount: $" + event.getBidAmount() + "\n" +
               "Reason: " + event.getRejectionReason() + "\n\n" +
               "Don't be discouraged! There are many other opportunities available.\n" +
               "Keep browsing jobs and submitting competitive bids.\n\n" +
               "Tips to improve your chances:\n" +
               "- Provide detailed proposals\n" +
               "- Build your reputation with completed jobs\n" +
               "- Respond quickly to job postings\n\n" +
               "Regards,\nTeam QuickHammer";
    }
}

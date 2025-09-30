package com.devnerd.notification_service.services;

import java.time.LocalDateTime;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.devnerd.events.models.JobAssignedEvent;
import com.devnerd.notification_service.clients.UserClient;
import com.devnerd.notification_service.dto.UserDetailsReponseDTO;
import com.devnerd.notification_service.model.NotificationModel;
import com.devnerd.notification_service.model.NotificationModel.NotificationMedium;
import com.devnerd.notification_service.repository.NotificationRepository;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Service
public class NotificationService{
  private final UserClient userClient;
  private final JavaMailSender javaMailSender;
  private final NotificationRepository notificationRepository;

  public void sendJobAssignedEmail(JobAssignedEvent event){
    //get user email
    UserDetailsReponseDTO user = userClient.getUser(event.getAssignedToId());

    //send email
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(user.getEmail());
    message.setSubject("You have been assigned a job: " + event.getJobTitle());
    message.setText(buildEmailBody(event, user));

    javaMailSender.send(message);

    //save notification to db
    NotificationModel notification = NotificationModel.builder()
      .recipientId(user.getUserId())
      .message(buildEmailBody(event, user))
      .createdAt(LocalDateTime.now())
      .medium(NotificationMedium.EMAIL)
      .build();
      
    notificationRepository.save(notification);
    
  }

  private String buildEmailBody(JobAssignedEvent event, UserDetailsReponseDTO user) {
    return "Hi " + user.getFirstName() + " " + user.getLastName() + ",\n\n" +
           "You have been assigned a new job.\n\n" +
           "Job Title: " + event.getJobTitle() + "\n" +
           "Description: " + event.getJobDescription() + "\n" +
           "Initial Budget: $" + event.getJobBudget() + "\n\n" +
           "Agreed Budget: $" + event.getAgreedBidBudget() + "\n\n" +
           "Please check your dashboard for more details.\n\n" +
           "Regards,\nTeam DevNerd";
  }
}

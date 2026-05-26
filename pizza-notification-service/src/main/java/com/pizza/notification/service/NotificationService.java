package com.pizza.notification.service;

import com.pizza.notification.dto.EmailRequestDto;
import com.pizza.notification.entity.Notification;
import com.pizza.notification.repository.NotificationRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final JavaMailSender javaMailSender;

    public NotificationService(NotificationRepository notificationRepository, JavaMailSender javaMailSender) {
        this.notificationRepository = notificationRepository;
        this.javaMailSender = javaMailSender;
    }

    public Notification sendEmail(EmailRequestDto request) {
        // 1. Send Actual Email
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(request.getRecipientEmail());
            msg.setSubject(request.getSubject());
            msg.setText(request.getMessage());
            msg.setFrom("noreply@pizzastore.com"); // or your email

            javaMailSender.send(msg);
            System.out.println("✅ Email Sent Successfully to " + request.getRecipientEmail());
        } catch (Exception e) {
            System.err.println("❌ Failed to send email: " + e.getMessage());
            // We still save the notification to DB even if email fails, 
            // but in a real app, you might mark status as FAILED.
        }

        // 2. Save Log to Database
        Notification notification = new Notification(
                request.getOrderId(),
                request.getRecipientEmail(),
                request.getSubject(),
                request.getMessage()
        );
        return notificationRepository.save(notification);
    }

    public List<Notification> getNotificationsForOrder(Long orderId) {
        return notificationRepository.findByOrderId(orderId);
    }
}
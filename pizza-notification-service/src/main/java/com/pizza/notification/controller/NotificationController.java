package com.pizza.notification.controller;

import com.pizza.notification.dto.EmailRequestDto;
import com.pizza.notification.entity.Notification;
import com.pizza.notification.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/send")
    public ResponseEntity<Notification> sendNotification(@RequestBody EmailRequestDto request) {
        Notification sent = notificationService.sendEmail(request);
        return ResponseEntity.ok(sent);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<Notification>> getHistoryByOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(notificationService.getNotificationsForOrder(orderId));
    }
}
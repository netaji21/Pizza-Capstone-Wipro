package com.pizza.notification.dto;

public class EmailRequestDto {
    private Long orderId;
    private String recipientEmail;
    private String subject;
    private String message;

    public EmailRequestDto() {
    }

    public EmailRequestDto(Long orderId, String recipientEmail, String subject, String message) {
        this.orderId = orderId;
        this.recipientEmail = recipientEmail;
        this.subject = subject;
        this.message = message;
    }

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public String getRecipientEmail() { return recipientEmail; }
    public void setRecipientEmail(String recipientEmail) { this.recipientEmail = recipientEmail; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
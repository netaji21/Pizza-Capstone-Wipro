package com.pizza.admin.dto;

import java.math.BigDecimal;

public class PaymentIntegrationDto {
    private Long id;
    private Long orderId;
    private BigDecimal amount;
    private String paymentMode;
    private String paymentStatus;
    private String transactionId;

    public PaymentIntegrationDto() {}

    public PaymentIntegrationDto(Long orderId, BigDecimal amount, String paymentMode) {
        this.orderId = orderId;
        this.amount = amount;
        this.paymentMode = paymentMode;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getPaymentMode() { return paymentMode; }
    public void setPaymentMode(String paymentMode) { this.paymentMode = paymentMode; }
    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
}
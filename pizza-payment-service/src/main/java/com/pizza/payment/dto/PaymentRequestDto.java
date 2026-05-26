package com.pizza.payment.dto;

import java.math.BigDecimal;

public class PaymentRequestDto {
    private Long orderId;
    private BigDecimal amount;
    private String paymentMode;

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getPaymentMode() { return paymentMode; }
    public void setPaymentMode(String paymentMode) { this.paymentMode = paymentMode; }
}
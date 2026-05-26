package com.pizza.payment.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId; // Logical link to Admin/User Service Order

    private String paymentMode; // CASH, CARD, UPI

    private String paymentStatus; // PENDING, SUCCESS, FAILED

    private BigDecimal amount;

    private String transactionId;

    public Payment() {
    }

    public Payment(Long orderId, String paymentMode, BigDecimal amount) {
        this.orderId = orderId;
        this.paymentMode = paymentMode;
        this.amount = amount;
        this.paymentStatus = "PENDING";
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public String getPaymentMode() { return paymentMode; }
    public void setPaymentMode(String paymentMode) { this.paymentMode = paymentMode; }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
}
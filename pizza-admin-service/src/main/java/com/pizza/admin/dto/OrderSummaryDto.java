package com.pizza.admin.dto;

import java.math.BigDecimal;

public class OrderSummaryDto {

    private Long id;
    private String orderNumber;
    private String status;
    private BigDecimal totalAmount;

    public OrderSummaryDto() {
    }

    public OrderSummaryDto(Long id, String orderNumber, String status, BigDecimal totalAmount) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.status = status;
        this.totalAmount = totalAmount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
}

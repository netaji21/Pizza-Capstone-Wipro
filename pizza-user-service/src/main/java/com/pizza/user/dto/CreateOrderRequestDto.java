package com.pizza.user.dto;

import java.util.List;

public class CreateOrderRequestDto {

    private Long customerId;
    private String customerEmail; // <--- Added
    private String deliveryMode;
    private String paymentMode;
    private List<OrderItemSelectionDto> items;

    public CreateOrderRequestDto() {
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getDeliveryMode() {
        return deliveryMode;
    }

    public void setDeliveryMode(String deliveryMode) {
        this.deliveryMode = deliveryMode;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public List<OrderItemSelectionDto> getItems() {
        return items;
    }

    public void setItems(List<OrderItemSelectionDto> items) {
        this.items = items;
    }
}
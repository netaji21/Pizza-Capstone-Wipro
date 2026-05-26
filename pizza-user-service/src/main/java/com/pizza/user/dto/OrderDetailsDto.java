package com.pizza.user.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderDetailsDto {

    private Long id;
    private String orderNumber;
    private String status;
    private BigDecimal totalAmount;
    private LocalDateTime orderTime;
    private String deliveryMode;
    private String paymentMode;
    private String paymentStatus;
    private List<Item> items;

    public static class Item {
        private String itemName;
        private BigDecimal price;
        private int quantity;
        private BigDecimal lineTotal;

        public String getItemName() { return itemName; }
        public BigDecimal getPrice() { return price; }
        public int getQuantity() { return quantity; }
        public BigDecimal getLineTotal() { return lineTotal; }

        public void setItemName(String itemName) { this.itemName = itemName; }
        public void setPrice(BigDecimal price) { this.price = price; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
        public void setLineTotal(BigDecimal lineTotal) { this.lineTotal = lineTotal; }
    }

    public Long getId() { return id; }
    public String getOrderNumber() { return orderNumber; }
    public String getStatus() { return status; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public LocalDateTime getOrderTime() { return orderTime; }
    public String getDeliveryMode() { return deliveryMode; }
    public String getPaymentMode() { return paymentMode; }
    public String getPaymentStatus() { return paymentStatus; }
    public List<Item> getItems() { return items; }

    public void setId(Long id) { this.id = id; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }
    public void setStatus(String status) { this.status = status; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public void setOrderTime(LocalDateTime orderTime) { this.orderTime = orderTime; }
    public void setDeliveryMode(String deliveryMode) { this.deliveryMode = deliveryMode; }
    public void setPaymentMode(String paymentMode) { this.paymentMode = paymentMode; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
    public void setItems(List<Item> items) { this.items = items; }
}

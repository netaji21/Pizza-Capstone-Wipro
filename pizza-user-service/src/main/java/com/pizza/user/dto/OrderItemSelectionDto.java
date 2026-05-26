package com.pizza.user.dto;

public class OrderItemSelectionDto {

    private Long menuItemId;
    private int quantity;

    public OrderItemSelectionDto() {
    }

    public Long getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(Long menuItemId) {
        this.menuItemId = menuItemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}

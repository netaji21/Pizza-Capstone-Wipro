package com.pizza.user.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CartDto {

    private List<CartItemDto> items = new ArrayList<>();

    public CartDto() {
    }

    public List<CartItemDto> getItems() {
        return items;
    }

    public void setItems(List<CartItemDto> items) {
        this.items = items;
    }

    public void addItem(CartItemDto newItem) {
        for (CartItemDto it : items) {
            if (it.getMenuItemId().equals(newItem.getMenuItemId())) {
                it.setQuantity(it.getQuantity() + newItem.getQuantity());
                return;
            }
        }
        items.add(newItem);
    }

    // NEW: Increase quantity by 1
    public void increaseItem(Long menuItemId) {
        for (CartItemDto it : items) {
            if (it.getMenuItemId().equals(menuItemId)) {
                it.setQuantity(it.getQuantity() + 1);
                return;
            }
        }
    }

    // Reduce quantity by 1, or remove if it becomes 0
    public void decreaseItem(Long menuItemId) {
        for (int i = 0; i < items.size(); i++) {
            CartItemDto it = items.get(i);
            if (it.getMenuItemId().equals(menuItemId)) {
                if (it.getQuantity() > 1) {
                    it.setQuantity(it.getQuantity() - 1);
                } else {
                    items.remove(i);
                }
                return;
            }
        }
    }

    public void removeItem(Long menuItemId) {
        items.removeIf(i -> i.getMenuItemId().equals(menuItemId));
    }

    public BigDecimal getTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (CartItemDto it : items) {
            if (it.getPrice() != null) {
                total = total.add(it.getPrice().multiply(java.math.BigDecimal.valueOf(it.getQuantity())));
            }
        }
        return total;
    }

    public boolean isEmpty() {
        return items == null || items.isEmpty();
    }

    public void clear() {
        if (items != null) items.clear();
    }
}
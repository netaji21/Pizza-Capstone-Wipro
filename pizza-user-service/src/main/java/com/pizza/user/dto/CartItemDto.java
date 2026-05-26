package com.pizza.user.dto;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * DTO stored in session representing an item in the shopping cart.
 * Includes a helper method getLineTotal() so templates don't perform arithmetic.
 */
public class CartItemDto {

    private Long menuItemId;
    private String name;
    private BigDecimal price;
    private int quantity;

    public CartItemDto() {
    }

    public CartItemDto(Long menuItemId, String name, BigDecimal price, int quantity) {
        this.menuItemId = menuItemId;
        this.name = name;
        this.price = price == null ? BigDecimal.ZERO : price;
        this.quantity = Math.max(1, quantity);
    }

    public Long getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(Long menuItemId) {
        this.menuItemId = menuItemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price == null ? BigDecimal.ZERO : price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getQuantity() {
        return Math.max(1, quantity);
    }

    public void setQuantity(int quantity) {
        this.quantity = Math.max(1, quantity);
    }

    /**
     * Safe helper used by the Thymeleaf template to render the line total.
     * This avoids doing BigDecimal arithmetic in the template.
     */
    public BigDecimal getLineTotal() {
        BigDecimal p = getPrice();
        return p.multiply(BigDecimal.valueOf(getQuantity()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CartItemDto that = (CartItemDto) o;
        return Objects.equals(menuItemId, that.menuItemId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(menuItemId);
    }
}

package com.pizza.admin.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "order_item")
public class OrderItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private CustomerOrderEntity order;

    @ManyToOne
    @JoinColumn(name = "menu_item_id")
    private MenuItemEntity menuItem;

    private int quantity;

    private BigDecimal itemPrice;

    private BigDecimal lineTotal;

    public OrderItemEntity() {
    }

    // getters and setters
    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public CustomerOrderEntity getOrder() { return order; }

    public void setOrder(CustomerOrderEntity order) { this.order = order; }

    public MenuItemEntity getMenuItem() { return menuItem; }

    public void setMenuItem(MenuItemEntity menuItem) { this.menuItem = menuItem; }

    public int getQuantity() { return quantity; }

    public void setQuantity(int quantity) { this.quantity = quantity; }

    public BigDecimal getItemPrice() { return itemPrice; }

    public void setItemPrice(BigDecimal itemPrice) { this.itemPrice = itemPrice; }

    public BigDecimal getLineTotal() { return lineTotal; }

    public void setLineTotal(BigDecimal lineTotal) { this.lineTotal = lineTotal; }
}

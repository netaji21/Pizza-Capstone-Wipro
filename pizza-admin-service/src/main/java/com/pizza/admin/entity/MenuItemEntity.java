package com.pizza.admin.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "menu_item")
public class MenuItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    private boolean veg;

    // e.g. SMALL / MEDIUM / LARGE
    private String size;

    private boolean available = true;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private MenuCategoryEntity category;

    public MenuItemEntity() {
    }

    // getters and setters
    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public BigDecimal getPrice() { return price; }

    public void setPrice(BigDecimal price) { this.price = price; }

    public boolean isVeg() { return veg; }

    public void setVeg(boolean veg) { this.veg = veg; }

    public String getSize() { return size; }

    public void setSize(String size) { this.size = size; }

    public boolean isAvailable() { return available; }

    public void setAvailable(boolean available) { this.available = available; }

    public MenuCategoryEntity getCategory() { return category; }

    public void setCategory(MenuCategoryEntity category) { this.category = category; }
}

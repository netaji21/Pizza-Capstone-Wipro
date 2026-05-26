package com.pizza.user.dto;

import java.math.BigDecimal;

public class MenuItemDto {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private boolean veg;
    private String size;
    private boolean available;
    private MenuCategoryDto category; // <--- Added this

    public MenuItemDto() {
    }

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

    public MenuCategoryDto getCategory() { return category; }
    public void setCategory(MenuCategoryDto category) { this.category = category; }
}
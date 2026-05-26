package com.pizza.admin.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "menu_category")
public class MenuCategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    @JsonIgnore // <--- CRITICAL FIX: Prevents infinite JSON recursion
    private List<MenuItemEntity> items = new ArrayList<>();

    public MenuCategoryEntity() {
    }

    public MenuCategoryEntity(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // getters and setters
    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public List<MenuItemEntity> getItems() { return items; }

    public void setItems(List<MenuItemEntity> items) { this.items = items; }
}
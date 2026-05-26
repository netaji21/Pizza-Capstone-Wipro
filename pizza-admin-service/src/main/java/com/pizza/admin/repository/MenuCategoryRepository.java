package com.pizza.admin.repository;

import com.pizza.admin.entity.MenuCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuCategoryRepository extends JpaRepository<MenuCategoryEntity, Long> {
    // Basic CRUD is enough
}
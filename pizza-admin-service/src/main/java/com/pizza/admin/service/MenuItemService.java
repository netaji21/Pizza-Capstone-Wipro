package com.pizza.admin.service;

import com.pizza.admin.entity.MenuItemEntity;

import java.util.List;
import java.util.Optional;

public interface MenuItemService {

    MenuItemEntity create(MenuItemEntity item);

    MenuItemEntity update(Long id, MenuItemEntity item);

    void delete(Long id);

    List<MenuItemEntity> findAll();

    Optional<MenuItemEntity> findById(Long id);

    // Updated search method
    List<MenuItemEntity> searchMenuItems(String keyword, String category, Boolean veg, String sortDir);
}
package com.pizza.user.service;

import com.pizza.user.dto.MenuItemDto;

import java.util.List;

public interface MenuBrowseService {

    /**
     * Get menu items with filtering and sorting.
     */
    List<MenuItemDto> getMenuItems(String keyword, String category, Boolean veg, String sort);

    MenuItemDto getMenuItemById(Long id);
}
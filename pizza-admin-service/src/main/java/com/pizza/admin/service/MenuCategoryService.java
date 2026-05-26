package com.pizza.admin.service;

import com.pizza.admin.entity.MenuCategoryEntity;
import java.util.List;

public interface MenuCategoryService {
    List<MenuCategoryEntity> findAll();
}
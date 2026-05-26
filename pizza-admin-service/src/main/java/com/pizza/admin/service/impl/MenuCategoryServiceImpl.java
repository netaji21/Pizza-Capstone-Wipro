package com.pizza.admin.service.impl;

import com.pizza.admin.entity.MenuCategoryEntity;
import com.pizza.admin.repository.MenuCategoryRepository;
import com.pizza.admin.service.MenuCategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuCategoryServiceImpl implements MenuCategoryService {

    private final MenuCategoryRepository repository;

    public MenuCategoryServiceImpl(MenuCategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<MenuCategoryEntity> findAll() {
        return repository.findAll();
    }
}
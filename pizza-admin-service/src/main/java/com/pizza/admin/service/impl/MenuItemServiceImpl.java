package com.pizza.admin.service.impl;

import com.pizza.admin.entity.MenuItemEntity;
import com.pizza.admin.repository.MenuItemRepository;
import com.pizza.admin.service.MenuItemService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MenuItemServiceImpl implements MenuItemService {

    private final MenuItemRepository menuItemRepository;

    public MenuItemServiceImpl(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    @Override
    public MenuItemEntity create(MenuItemEntity item) {
        return menuItemRepository.save(item);
    }

    @Override
    public MenuItemEntity update(Long id, MenuItemEntity updatedItem) {
        return menuItemRepository.findById(id)
                .map(existing -> {
                    existing.setName(updatedItem.getName());
                    existing.setDescription(updatedItem.getDescription());
                    existing.setPrice(updatedItem.getPrice());
                    existing.setVeg(updatedItem.isVeg());
                    existing.setSize(updatedItem.getSize());
                    existing.setAvailable(updatedItem.isAvailable());
                    existing.setCategory(updatedItem.getCategory());
                    return menuItemRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Menu item not found with id: " + id));
    }

    @Override
    public void delete(Long id) {
        if (!menuItemRepository.existsById(id)) {
            throw new RuntimeException("Menu item not found with id: " + id);
        }
        menuItemRepository.deleteById(id);
    }

    @Override
    public List<MenuItemEntity> findAll() {
        return menuItemRepository.findAll();
    }

    @Override
    public Optional<MenuItemEntity> findById(Long id) {
        return menuItemRepository.findById(id);
    }

    @Override
    public List<MenuItemEntity> searchMenuItems(String keyword, String category, Boolean veg, String sortDir) {
        // Handle empty strings as null for the query
        if (keyword != null && keyword.trim().isEmpty()) keyword = null;
        if (category != null && category.trim().isEmpty()) category = null;

        // Default sort by name if no sort direction provided
        Sort sort = Sort.by("name");
        if ("asc".equalsIgnoreCase(sortDir)) {
            sort = Sort.by("price").ascending();
        } else if ("desc".equalsIgnoreCase(sortDir)) {
            sort = Sort.by("price").descending();
        }

        return menuItemRepository.searchMenuItems(keyword, category, veg, sort);
    }
}
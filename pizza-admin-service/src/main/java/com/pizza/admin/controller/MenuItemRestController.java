package com.pizza.admin.controller;

import com.pizza.admin.entity.MenuItemEntity;
import com.pizza.admin.service.MenuItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/menu-items")
public class MenuItemRestController {

    private final MenuItemService menuItemService;

    public MenuItemRestController(MenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }

    @PostMapping
    public ResponseEntity<MenuItemEntity> create(@RequestBody MenuItemEntity item) {
        MenuItemEntity created = menuItemService.create(item);
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<MenuItemEntity>> getAll(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Boolean veg,
            @RequestParam(required = false) String sort) {

        List<MenuItemEntity> items = menuItemService.searchMenuItems(keyword, category, veg, sort);
        return ResponseEntity.ok(items);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuItemEntity> getById(@PathVariable Long id) {
        return menuItemService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<MenuItemEntity> update(
            @PathVariable Long id,
            @RequestBody MenuItemEntity item) {

        MenuItemEntity updated = menuItemService.update(id, item);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        menuItemService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
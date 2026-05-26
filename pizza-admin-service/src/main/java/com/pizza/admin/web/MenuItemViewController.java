package com.pizza.admin.web;

import com.pizza.admin.entity.MenuItemEntity;
import com.pizza.admin.service.MenuCategoryService;
import com.pizza.admin.service.MenuItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/menu")
public class MenuItemViewController {

    private final MenuItemService menuItemService;
    private final MenuCategoryService menuCategoryService;

    public MenuItemViewController(MenuItemService menuItemService, MenuCategoryService menuCategoryService) {
        this.menuItemService = menuItemService;
        this.menuCategoryService = menuCategoryService;
    }

    @GetMapping
    public String listMenuItems(@RequestParam(required = false) String keyword,
                                Model model) {

        // FIXED: Calling the new searchMenuItems method.
        // We pass 'null' for category, veg, and sort because the Admin List view 
        // (currently) only has a simple keyword search.
        List<MenuItemEntity> items = menuItemService.searchMenuItems(keyword, null, null, null);
        
        model.addAttribute("items", items);
        model.addAttribute("keyword", keyword == null ? "" : keyword);
        return "admin-menu-list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("menuItem", new MenuItemEntity());
        model.addAttribute("categories", menuCategoryService.findAll()); 
        model.addAttribute("formTitle", "Add New Menu Item");
        return "admin-menu-form";
    }

    @PostMapping
    public String createMenuItem(@ModelAttribute("menuItem") MenuItemEntity menuItem) {
        menuItemService.create(menuItem);
        return "redirect:/admin/menu";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        MenuItemEntity item = menuItemService.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu item not found with id: " + id));
        
        model.addAttribute("menuItem", item);
        model.addAttribute("categories", menuCategoryService.findAll()); 
        model.addAttribute("formTitle", "Edit Menu Item");
        return "admin-menu-form";
    }

    @PostMapping("/{id}")
    public String updateMenuItem(@PathVariable Long id,
                                 @ModelAttribute("menuItem") MenuItemEntity menuItem) {
        menuItemService.update(id, menuItem);
        return "redirect:/admin/menu";
    }

    @GetMapping("/delete/{id}")
    public String deleteMenuItem(@PathVariable Long id) {
        menuItemService.delete(id);
        return "redirect:/admin/menu";
    }
}
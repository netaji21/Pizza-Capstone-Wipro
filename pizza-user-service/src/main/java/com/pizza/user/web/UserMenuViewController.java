package com.pizza.user.web;

import com.pizza.user.dto.MenuItemDto;
import com.pizza.user.service.MenuBrowseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/customers/menu")
public class UserMenuViewController {

    private final MenuBrowseService menuBrowseService;

    public UserMenuViewController(MenuBrowseService menuBrowseService) {
        this.menuBrowseService = menuBrowseService;
    }

    @GetMapping
    public String showMenu(@RequestParam(required = false) String keyword,
                           @RequestParam(required = false) String category,
                           @RequestParam(required = false) Boolean veg,
                           @RequestParam(required = false) String sort,
                           Model model) {

        List<MenuItemDto> items = menuBrowseService.getMenuItems(keyword, category, veg, sort);

        // Group items by Category Name
        Map<String, List<MenuItemDto>> categorizedMenu = items.stream()
                .collect(Collectors.groupingBy(item -> {
                    if (item.getCategory() != null && item.getCategory().getName() != null) {
                        return item.getCategory().getName();
                    } else {
                        return "Others";
                    }
                }, TreeMap::new, Collectors.toList()));

        model.addAttribute("categorizedMenu", categorizedMenu);
        
        // Pass back filters to keep form state
        model.addAttribute("keyword", keyword);
        model.addAttribute("selectedCategory", category);
        model.addAttribute("isVeg", veg);
        model.addAttribute("selectedSort", sort);

        return "user-menu";
    }
}
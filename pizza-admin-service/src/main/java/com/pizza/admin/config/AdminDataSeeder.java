package com.pizza.admin.config;

import com.pizza.admin.entity.AdminEntity;
import com.pizza.admin.entity.MenuCategoryEntity;
import com.pizza.admin.entity.MenuItemEntity;
import com.pizza.admin.repository.AdminRepository;
import com.pizza.admin.repository.MenuCategoryRepository;
import com.pizza.admin.repository.MenuItemRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class AdminDataSeeder implements CommandLineRunner {

    private final AdminRepository adminRepository;
    private final MenuCategoryRepository categoryRepository;
    private final MenuItemRepository menuItemRepository; // Added this
    private final PasswordEncoder passwordEncoder;

    public AdminDataSeeder(AdminRepository adminRepository, 
                           MenuCategoryRepository categoryRepository,
                           MenuItemRepository menuItemRepository,
                           PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.categoryRepository = categoryRepository;
        this.menuItemRepository = menuItemRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        
        // 1. Seed Admin
        if (adminRepository.count() == 0) {
            AdminEntity admin = new AdminEntity();
            admin.setName("Super Admin");
            admin.setEmail("admin@pizza.com");
            admin.setPhone("9999999999");
            admin.setPassword(passwordEncoder.encode("admin")); 
            admin.setRole("ADMIN");

            adminRepository.save(admin);
            System.out.println("------ DATA SEEDER: Created Default Admin ------");
        }

        // 2. Seed Categories & Menu Items
        if (categoryRepository.count() == 0) {
            System.out.println("------ DATA SEEDER: Starting Menu Population ------");

            // Category: Pizza
            MenuCategoryEntity catPizza = new MenuCategoryEntity("Pizza", "Delicious hand-tossed pizzas");
            List<MenuItemEntity> pizzas = new ArrayList<>();
            pizzas.add(createItem("Margherita", "Classic cheese and tomato base", new BigDecimal("250.00"), true, "Regular", catPizza));
            pizzas.add(createItem("Farmhouse", "Onion, capsicum, tomato, and mushroom", new BigDecimal("450.00"), true, "Medium", catPizza));
            pizzas.add(createItem("Pepperoni", "American classic with spicy pepperoni", new BigDecimal("550.00"), false, "Medium", catPizza));
            pizzas.add(createItem("Chicken Supreme", "Loaded with chicken tikka and barbecue chicken", new BigDecimal("650.00"), false, "Large", catPizza));
            catPizza.setItems(pizzas);
            categoryRepository.save(catPizza);

            // Category: Sides
            MenuCategoryEntity catSides = new MenuCategoryEntity("Sides", "Perfect companions for your meal");
            List<MenuItemEntity> sides = new ArrayList<>();
            sides.add(createItem("Garlic Breadsticks", "Baked to perfection with garlic butter", new BigDecimal("120.00"), true, null, catSides));
            sides.add(createItem("Stuffed Garlic Bread", "Filled with jalapenos and cheese", new BigDecimal("160.00"), true, null, catSides));
            sides.add(createItem("Chicken Wings", "Spicy roasted wings (6 pcs)", new BigDecimal("220.00"), false, null, catSides));
            sides.add(createItem("Chicken Meatballs", "Juicy meatballs in peri-peri sauce", new BigDecimal("190.00"), false, null, catSides));
            catSides.setItems(sides);
            categoryRepository.save(catSides);

            // Category: Beverages
            MenuCategoryEntity catBev = new MenuCategoryEntity("Beverages", "Refreshing drinks");
            List<MenuItemEntity> drinks = new ArrayList<>();
            drinks.add(createItem("Coke", "500ml bottle", new BigDecimal("60.00"), true, "500ml", catBev));
            drinks.add(createItem("Pepsi", "500ml bottle", new BigDecimal("60.00"), true, "500ml", catBev));
            drinks.add(createItem("Sprite", "500ml bottle", new BigDecimal("60.00"), true, "500ml", catBev));
            drinks.add(createItem("Iced Tea", "Lemon flavor", new BigDecimal("80.00"), true, "250ml", catBev));
            catBev.setItems(drinks);
            categoryRepository.save(catBev);

            // Category: Combo
            MenuCategoryEntity catCombo = new MenuCategoryEntity("Combo", "Value meals for one or two");
            List<MenuItemEntity> combos = new ArrayList<>();
            combos.add(createItem("Veg Meal for 1", "Small Pizza + Garlic Bread + Coke", new BigDecimal("399.00"), true, "Single", catCombo));
            combos.add(createItem("Non-Veg Meal for 1", "Small Chicken Pizza + Wings + Coke", new BigDecimal("499.00"), false, "Single", catCombo));
            combos.add(createItem("Family Party (Veg)", "2 Medium Pizzas + 2 Sides + 2 Drinks", new BigDecimal("999.00"), true, "Family", catCombo));
            combos.add(createItem("Couple Feast", "Medium Pizza + Stuffed Bread + 2 Drinks", new BigDecimal("799.00"), false, "Double", catCombo));
            catCombo.setItems(combos);
            categoryRepository.save(catCombo);

            System.out.println("------ DATA SEEDER: Menu Population Complete ------");
        }
    }

    private MenuItemEntity createItem(String name, String desc, BigDecimal price, boolean veg, String size, MenuCategoryEntity category) {
        MenuItemEntity item = new MenuItemEntity();
        item.setName(name);
        item.setDescription(desc);
        item.setPrice(price);
        item.setVeg(veg);
        item.setSize(size);
        item.setAvailable(true);
        item.setCategory(category); // Important: Link back to category for JPA
        return item;
    }
}
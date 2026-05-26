package com.pizza.user.web;

import com.pizza.user.dto.MenuItemDto;
import com.pizza.user.dto.OrderResponseDto;
import com.pizza.user.entity.CustomerEntity;
import com.pizza.user.repository.CustomerRepository;
import com.pizza.user.service.MenuBrowseService;
import com.pizza.user.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/customers/order")
public class UserOrderViewController {

    private final MenuBrowseService menuBrowseService;
    private final CustomerRepository customerRepository;
    private final OrderService orderService;

    public UserOrderViewController(MenuBrowseService menuBrowseService,
                                   CustomerRepository customerRepository,
                                   OrderService orderService) {
        this.menuBrowseService = menuBrowseService;
        this.customerRepository = customerRepository;
        this.orderService = orderService;
    }

    @GetMapping("/new")
    public String showOrderForm(@RequestParam("itemId") Long itemId,
                                HttpSession session,
                                Model model) {

        Long customerId = (Long) session.getAttribute("loggedInCustomerId");
        if (customerId == null) {
            return "redirect:/customers/login";
        }

        MenuItemDto menuItem = menuBrowseService.getMenuItemById(itemId);
        if (menuItem == null) {
            return "redirect:/customers/menu";
        }

        Optional<CustomerEntity> customerOpt = customerRepository.findById(customerId);
        customerOpt.ifPresent(c -> model.addAttribute("customerEmail", c.getEmail()));

        model.addAttribute("menuItem", menuItem);
        return "user-order-form";
    }

    @PostMapping
    public String placeOrder(@RequestParam("itemId") Long itemId,
                             @RequestParam("quantity") int quantity,
                             @RequestParam("deliveryMode") String deliveryMode,
                             @RequestParam("paymentMode") String paymentMode,
                             HttpSession session,
                             Model model) {

        Long customerId = (Long) session.getAttribute("loggedInCustomerId");
        if (customerId == null) {
            return "redirect:/customers/login";
        }

        Optional<CustomerEntity> customerOpt = customerRepository.findById(customerId);
        if (customerOpt.isEmpty()) {
            return "redirect:/customers/login";
        }

        CustomerEntity customer = customerOpt.get();

        OrderResponseDto response = orderService.placeOrder(
                customerId,
                deliveryMode,
                paymentMode,
                itemId,
                quantity
        );

        model.addAttribute("order", response);
        model.addAttribute("customerEmail", customer.getEmail());
        return "user-order-confirmation";
    }
}

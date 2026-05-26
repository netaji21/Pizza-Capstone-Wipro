package com.pizza.user.web;

import com.pizza.user.dto.NotificationDto;
import com.pizza.user.dto.OrderDetailsDto;
import com.pizza.user.dto.OrderSummaryDto;
import com.pizza.user.service.OrderDetailsService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/customers/orders/details")
public class UserOrderDetailController {

    private final OrderDetailsService orderDetailsService;

    public UserOrderDetailController(OrderDetailsService orderDetailsService) {
        this.orderDetailsService = orderDetailsService;
    }

    @GetMapping("/{id}")
    public String viewOrderDetails(@PathVariable Long id,
                                   HttpSession session,
                                   Model model) {

        Long customerId = (Long) session.getAttribute("loggedInCustomerId");
        if (customerId == null) return "redirect:/customers/login";

        // 1. Get Order Details (from Admin Service)
        OrderDetailsDto dto = orderDetailsService.getOrderDetails(id);

        if (dto == null) {
            model.addAttribute("error", "Could not load order details");
            return "user-order-history";
        }

        // 2. Get Notifications (from Notification Service)
        List<NotificationDto> notifications = orderDetailsService.getOrderNotifications(id);

        model.addAttribute("order", dto);
        model.addAttribute("notifications", notifications);
        
        return "user-order-details";
    }

    @PostMapping("/cancel/{id}")
    public String cancelOrder(@PathVariable Long id,
                              HttpSession session) {

        Long customerId = (Long) session.getAttribute("loggedInCustomerId");
        if (customerId == null) return "redirect:/customers/login";

        orderDetailsService.cancelOrder(id);

        return "redirect:/customers/orders/details/" + id;
    }
}
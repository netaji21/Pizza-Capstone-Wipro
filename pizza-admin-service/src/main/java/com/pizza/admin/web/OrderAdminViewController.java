package com.pizza.admin.web;

import com.pizza.admin.dto.PaymentIntegrationDto;
import com.pizza.admin.entity.CustomerOrderEntity;
import com.pizza.admin.entity.OrderItemEntity;
import com.pizza.admin.service.OrderManagementService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Controller
@RequestMapping("/admin/orders")
public class OrderAdminViewController {

    private final OrderManagementService orderManagementService;
    private final RestTemplate restTemplate;
    
    // USE SERVICE NAME
    private final String PAYMENT_SERVICE_URL = "http://pizza-payment-service/api/payments";

    public OrderAdminViewController(OrderManagementService orderManagementService, RestTemplate restTemplate) {
        this.orderManagementService = orderManagementService;
        this.restTemplate = restTemplate;
    }

    @GetMapping
    public String listOrders(Model model) {
        List<CustomerOrderEntity> orders = orderManagementService.getAllOrders();
        model.addAttribute("orders", orders);
        return "admin-orders-list";
    }

    @GetMapping("/{id}")
    public String viewOrder(@PathVariable Long id, Model model) {
        CustomerOrderEntity order = orderManagementService.getOrderById(id);
        List<OrderItemEntity> items = order.getItems();
        
        PaymentIntegrationDto payment = null;
        try {
            payment = restTemplate.getForObject(
                PAYMENT_SERVICE_URL + "/by-order/" + id, 
                PaymentIntegrationDto.class
            );
        } catch (Exception e) {
            // Payment might not be found or service down
        }

        model.addAttribute("order", order);
        model.addAttribute("items", items);
        model.addAttribute("payment", payment);

        return "admin-order-detail";
    }

    @PostMapping("/{id}/complete")
    public String markOrderCompleted(@PathVariable Long id) {
        orderManagementService.updateOrderStatus(id, "COMPLETED");
        return "redirect:/admin/orders/" + id;
    }

    @PostMapping("/{id}/cancel")
    public String markOrderCancelled(@PathVariable Long id) {
        orderManagementService.updateOrderStatus(id, "CANCELLED");
        return "redirect:/admin/orders/" + id;
    }
}
package com.pizza.user.web;

import com.pizza.user.dto.OrderSummaryDto;
import com.pizza.user.entity.CustomerEntity;
import com.pizza.user.repository.CustomerRepository;
import com.pizza.user.service.OrderHistoryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/customers/orders")
public class UserOrderHistoryController {

    private final OrderHistoryService orderHistoryService;
    private final CustomerRepository customerRepository;

    public UserOrderHistoryController(OrderHistoryService orderHistoryService,
                                      CustomerRepository customerRepository) {
        this.orderHistoryService = orderHistoryService;
        this.customerRepository = customerRepository;
    }

    @GetMapping
    public String showOrderHistory(HttpSession session, Model model) {
        Long customerId = (Long) session.getAttribute("loggedInCustomerId");
        if (customerId == null) {
            return "redirect:/customers/login";
        }

        Optional<CustomerEntity> customerOpt = customerRepository.findById(customerId);
        customerOpt.ifPresent(c -> model.addAttribute("customerName", c.getName()));

        List<OrderSummaryDto> orders = orderHistoryService.getOrdersForCustomer(customerId);
        model.addAttribute("orders", orders);
        return "user-order-history";
    }
}

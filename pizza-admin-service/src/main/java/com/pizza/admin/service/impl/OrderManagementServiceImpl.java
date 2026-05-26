package com.pizza.admin.service.impl;

import com.pizza.admin.dto.CreateOrderRequestDto;
import com.pizza.admin.dto.EmailIntegrationDto;
import com.pizza.admin.dto.OrderItemRequestDto;
import com.pizza.admin.dto.PaymentIntegrationDto;
import com.pizza.admin.entity.CustomerOrderEntity;
import com.pizza.admin.entity.MenuItemEntity;
import com.pizza.admin.entity.OrderItemEntity;
import com.pizza.admin.repository.CustomerOrderRepository;
import com.pizza.admin.repository.MenuItemRepository;
import com.pizza.admin.service.OrderManagementService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderManagementServiceImpl implements OrderManagementService {

    private final CustomerOrderRepository customerOrderRepository;
    private final MenuItemRepository menuItemRepository;
    private final RestTemplate restTemplate;

    // USE SERVICE NAMES (Eureka IDs)
    private final String PAYMENT_SERVICE_URL = "http://pizza-payment-service/api/payments";
    private final String NOTIFICATION_SERVICE_URL = "http://pizza-notification-service/api/notifications";

    public OrderManagementServiceImpl(CustomerOrderRepository customerOrderRepository,
                                      MenuItemRepository menuItemRepository,
                                      RestTemplate restTemplate) {
        this.customerOrderRepository = customerOrderRepository;
        this.menuItemRepository = menuItemRepository;
        this.restTemplate = restTemplate;
    }

    @Override
    public CustomerOrderEntity createOrder(CreateOrderRequestDto request) {
        if (request == null || request.getItems() == null || request.getItems().isEmpty()) {
            throw new RuntimeException("Order must contain at least one item");
        }

        CustomerOrderEntity order = new CustomerOrderEntity();
        order.setCustomerId(request.getCustomerId());
        order.setCustomerEmail(request.getCustomerEmail()); 
        order.setStatus("NEW");
        order.setDeliveryMode(request.getDeliveryMode());
        order.setOrderTime(LocalDateTime.now());
        order.setOrderNumber("ORD-" + System.currentTimeMillis());

        List<OrderItemEntity> orderItems = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (OrderItemRequestDto itemReq : request.getItems()) {
            MenuItemEntity menuItem = menuItemRepository.findById(itemReq.getMenuItemId())
                    .orElseThrow(() -> new RuntimeException("Menu item not found: " + itemReq.getMenuItemId()));

            OrderItemEntity orderItem = new OrderItemEntity();
            orderItem.setOrder(order);
            orderItem.setMenuItem(menuItem);
            orderItem.setQuantity(itemReq.getQuantity());
            orderItem.setItemPrice(menuItem.getPrice());
            
            BigDecimal lineTotal = menuItem.getPrice().multiply(BigDecimal.valueOf(itemReq.getQuantity()));
            orderItem.setLineTotal(lineTotal);

            total = total.add(lineTotal);
            orderItems.add(orderItem);
        }

        order.setItems(orderItems);
        order.setTotalAmount(total);

        CustomerOrderEntity savedOrder = customerOrderRepository.save(order);

        // Payment Service Call
        try {
            PaymentIntegrationDto paymentReq = new PaymentIntegrationDto(savedOrder.getId(), total, request.getPaymentMode());
            restTemplate.postForObject(PAYMENT_SERVICE_URL, paymentReq, PaymentIntegrationDto.class);
        } catch (Exception e) {
            System.err.println("Failed to contact Payment Service: " + e.getMessage());
        }

        // Notification Service Call
        try {
            String emailToUse = (savedOrder.getCustomerEmail() != null) ? savedOrder.getCustomerEmail() : "customer@example.com";
            
            EmailIntegrationDto emailReq = new EmailIntegrationDto(
                    savedOrder.getId(), 
                    emailToUse, 
                    "Order Placed", 
                    "Your order " + savedOrder.getOrderNumber() + " has been placed successfully."
            );
            restTemplate.postForObject(NOTIFICATION_SERVICE_URL + "/send", emailReq, Object.class);
        } catch (Exception e) {
            System.err.println("Failed to contact Notification Service: " + e.getMessage());
        }

        return savedOrder;
    }

    @Override
    public List<CustomerOrderEntity> getAllOrders() {
        return customerOrderRepository.findAll();
    }

    @Override
    public CustomerOrderEntity getOrderById(Long id) {
        return customerOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found: " + id));
    }

    @Override
    public CustomerOrderEntity updateOrderStatus(Long id, String newStatus) {
        CustomerOrderEntity order = getOrderById(id);
        order.setStatus(newStatus);
        
        try {
            String emailToUse = (order.getCustomerEmail() != null) ? order.getCustomerEmail() : "customer@example.com";

            EmailIntegrationDto emailReq = new EmailIntegrationDto(
                    order.getId(),
                    emailToUse,
                    "Order Update",
                    "Your order " + order.getOrderNumber() + " is now " + newStatus
            );
            restTemplate.postForObject(NOTIFICATION_SERVICE_URL + "/send", emailReq, Object.class);
        } catch (Exception e) {
            System.err.println("Notification failed: " + e.getMessage());
        }
        
        return customerOrderRepository.save(order);
    }

    @Override
    public List<CustomerOrderEntity> getOrdersByCustomerId(Long customerId) {
        return customerOrderRepository.findByCustomerId(customerId);
    }

    @Override
    public BigDecimal calculateTotalRevenue() {
        List<CustomerOrderEntity> allOrders = customerOrderRepository.findAll();
        return allOrders.stream()
                .filter(o -> "COMPLETED".equalsIgnoreCase(o.getStatus()))
                .map(CustomerOrderEntity::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
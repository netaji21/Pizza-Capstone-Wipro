package com.pizza.admin.service;

import com.pizza.admin.dto.CreateOrderRequestDto;
import com.pizza.admin.entity.CustomerOrderEntity;

import java.math.BigDecimal;
import java.util.List;

public interface OrderManagementService {

    CustomerOrderEntity createOrder(CreateOrderRequestDto request);

    List<CustomerOrderEntity> getAllOrders();

    CustomerOrderEntity getOrderById(Long id);

    CustomerOrderEntity updateOrderStatus(Long id, String newStatus);

    List<CustomerOrderEntity> getOrdersByCustomerId(Long customerId);
    
    // NEW: Calculate Revenue
    BigDecimal calculateTotalRevenue();
}
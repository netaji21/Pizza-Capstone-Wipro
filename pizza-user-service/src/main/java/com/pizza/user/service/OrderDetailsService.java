package com.pizza.user.service;

import com.pizza.user.dto.NotificationDto;
import com.pizza.user.dto.OrderDetailsDto;
import com.pizza.user.dto.OrderSummaryDto;

import java.util.List;

public interface OrderDetailsService {
    OrderDetailsDto getOrderDetails(Long orderId);
    OrderSummaryDto cancelOrder(Long orderId);
    
    // NEW: Get notifications for an order
    List<NotificationDto> getOrderNotifications(Long orderId);
}
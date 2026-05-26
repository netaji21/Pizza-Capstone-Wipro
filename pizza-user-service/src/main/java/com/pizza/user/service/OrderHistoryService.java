package com.pizza.user.service;

import com.pizza.user.dto.OrderSummaryDto;

import java.util.List;

public interface OrderHistoryService {

    List<OrderSummaryDto> getOrdersForCustomer(Long customerId);
}

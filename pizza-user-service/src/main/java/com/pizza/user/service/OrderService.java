package com.pizza.user.service;

import com.pizza.user.dto.CartDto;
import com.pizza.user.dto.OrderResponseDto;

public interface OrderService {

    /**
     * Place an order for a single menu item (existing method).
     */
    OrderResponseDto placeOrder(Long customerId,
                                String deliveryMode,
                                String paymentMode,
                                Long menuItemId,
                                int quantity);

    /**
     * Place an order using the contents of the user's cart.
     */
    OrderResponseDto placeOrderFromCart(Long customerId,
                                        CartDto cart,
                                        String deliveryMode,
                                        String paymentMode);
}

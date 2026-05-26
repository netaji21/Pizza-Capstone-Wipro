package com.pizza.user.service.impl;

import com.pizza.user.dto.CartDto;
import com.pizza.user.dto.CreateOrderRequestDto;
import com.pizza.user.dto.OrderItemSelectionDto;
import com.pizza.user.dto.OrderResponseDto;
import com.pizza.user.entity.CustomerEntity;
import com.pizza.user.repository.CustomerRepository;
import com.pizza.user.service.OrderService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private final RestTemplate restTemplate;
    private final String adminBaseUrl;
    private final CustomerRepository customerRepository;

    public OrderServiceImpl(RestTemplate restTemplate,
                            @Value("${pizza.admin.base-url}") String adminBaseUrl,
                            CustomerRepository customerRepository) {
        this.restTemplate = restTemplate;
        this.adminBaseUrl = adminBaseUrl;
        this.customerRepository = customerRepository;
    }

    @Override
    public OrderResponseDto placeOrder(Long customerId, String deliveryMode, String paymentMode, Long menuItemId, int quantity) {
        OrderItemSelectionDto item = new OrderItemSelectionDto();
        item.setMenuItemId(menuItemId);
        item.setQuantity(quantity);

        CreateOrderRequestDto request = new CreateOrderRequestDto();
        request.setCustomerId(customerId);
        request.setDeliveryMode(deliveryMode);
        request.setPaymentMode(paymentMode);
        request.setItems(Collections.singletonList(item));
        
        // Fetch and set email
        Optional<CustomerEntity> customer = customerRepository.findById(customerId);
        customer.ifPresent(c -> request.setCustomerEmail(c.getEmail()));

        return postOrderToAdmin(request);
    }

    @Override
    public OrderResponseDto placeOrderFromCart(Long customerId, CartDto cart, String deliveryMode, String paymentMode) {
        if (cart == null || cart.isEmpty()) {
            OrderResponseDto fail = new OrderResponseDto();
            fail.setStatus("FAILED");
            fail.setOrderNumber("Cart is empty");
            fail.setTotalAmount(BigDecimal.ZERO);
            return fail;
        }

        List<OrderItemSelectionDto> items = new ArrayList<>();
        for (var ci : cart.getItems()) {
            OrderItemSelectionDto sel = new OrderItemSelectionDto();
            sel.setMenuItemId(ci.getMenuItemId());
            sel.setQuantity(ci.getQuantity());
            items.add(sel);
        }

        CreateOrderRequestDto request = new CreateOrderRequestDto();
        request.setCustomerId(customerId);
        request.setDeliveryMode(deliveryMode);
        request.setPaymentMode(paymentMode);
        request.setItems(items);

        // Fetch and set email
        Optional<CustomerEntity> customer = customerRepository.findById(customerId);
        customer.ifPresent(c -> request.setCustomerEmail(c.getEmail()));

        return postOrderToAdmin(request);
    }

    private OrderResponseDto postOrderToAdmin(CreateOrderRequestDto request) {
        String url = adminBaseUrl + "/api/orders";

        try {
            Object raw = restTemplate.postForObject(url, request, Object.class);
            if (!(raw instanceof Map)) {
                return buildFailedResponse("Unexpected response from admin service.");
            }

            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) raw;

            OrderResponseDto dto = new OrderResponseDto();

            Object idObj = map.get("id");
            if (idObj != null) dto.setId(Long.valueOf(idObj.toString()));

            Object orderNumberObj = map.get("orderNumber");
            dto.setOrderNumber(orderNumberObj != null ? orderNumberObj.toString() : "N/A");

            Object statusObj = map.get("status");
            dto.setStatus(statusObj != null ? statusObj.toString() : "NEW");

            Object totalObj = map.get("totalAmount");
            if (totalObj != null) {
                dto.setTotalAmount(new BigDecimal(totalObj.toString()));
            } else {
                dto.setTotalAmount(BigDecimal.ZERO);
            }

            return dto;
        } catch (RestClientException ex) {
            return buildFailedResponse("Could not create order. Please try again later.");
        } catch (Exception ex) {
            return buildFailedResponse("Unexpected error while creating order.");
        }
    }

    private OrderResponseDto buildFailedResponse(String message) {
        OrderResponseDto fallback = new OrderResponseDto();
        fallback.setStatus("FAILED");
        fallback.setOrderNumber(message);
        fallback.setTotalAmount(BigDecimal.ZERO);
        return fallback;
    }
}
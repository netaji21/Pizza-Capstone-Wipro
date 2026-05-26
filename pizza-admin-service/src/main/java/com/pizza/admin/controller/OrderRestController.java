package com.pizza.admin.controller;

import com.pizza.admin.dto.CreateOrderRequestDto;
import com.pizza.admin.dto.OrderDetailsDto;
import com.pizza.admin.dto.OrderSummaryDto;
import com.pizza.admin.dto.PaymentIntegrationDto;
import com.pizza.admin.entity.CustomerOrderEntity;
import com.pizza.admin.entity.OrderItemEntity;
import com.pizza.admin.service.OrderManagementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderRestController {

    private final OrderManagementService orderManagementService;
    private final RestTemplate restTemplate;
    
    // USE SERVICE NAME
    private final String PAYMENT_SERVICE_URL = "http://pizza-payment-service/api/payments";

    public OrderRestController(OrderManagementService orderManagementService, RestTemplate restTemplate) {
        this.orderManagementService = orderManagementService;
        this.restTemplate = restTemplate;
    }

    @PostMapping
    public ResponseEntity<OrderSummaryDto> createOrder(@RequestBody CreateOrderRequestDto request) {
        CustomerOrderEntity created = orderManagementService.createOrder(request);
        OrderSummaryDto dto = new OrderSummaryDto(
                created.getId(),
                created.getOrderNumber(),
                created.getStatus(),
                created.getTotalAmount()
        );
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderSummaryDto> getOrder(@PathVariable Long id) {
        CustomerOrderEntity order = orderManagementService.getOrderById(id);
        OrderSummaryDto dto = new OrderSummaryDto(
                order.getId(),
                order.getOrderNumber(),
                order.getStatus(),
                order.getTotalAmount()
        );
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/by-customer/{customerId}")
    public ResponseEntity<List<OrderSummaryDto>> getOrdersByCustomer(@PathVariable Long customerId) {
        List<CustomerOrderEntity> orders = orderManagementService.getOrdersByCustomerId(customerId);
        List<OrderSummaryDto> dtos = new ArrayList<>();
        for (CustomerOrderEntity order : orders) {
            dtos.add(new OrderSummaryDto(
                    order.getId(),
                    order.getOrderNumber(),
                    order.getStatus(),
                    order.getTotalAmount()
            ));
        }
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/details/{orderId}")
    public ResponseEntity<OrderDetailsDto> getOrderDetails(@PathVariable Long orderId) {
        CustomerOrderEntity order = orderManagementService.getOrderById(orderId);
        
        String paymentMode = "UNKNOWN";
        String paymentStatus = "UNKNOWN";
        try {
            PaymentIntegrationDto payDto = restTemplate.getForObject(
                PAYMENT_SERVICE_URL + "/by-order/" + orderId, 
                PaymentIntegrationDto.class
            );
            if (payDto != null) {
                paymentMode = payDto.getPaymentMode();
                paymentStatus = payDto.getPaymentStatus();
            }
        } catch (Exception e) {
            System.err.println("Could not fetch payment info: " + e.getMessage());
        }

        OrderDetailsDto dto = new OrderDetailsDto();
        dto.setId(order.getId());
        dto.setOrderNumber(order.getOrderNumber());
        dto.setStatus(order.getStatus());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setOrderTime(order.getOrderTime());
        dto.setDeliveryMode(order.getDeliveryMode());
        dto.setPaymentMode(paymentMode);
        dto.setPaymentStatus(paymentStatus);

        List<OrderDetailsDto.OrderItemDetail> items = new ArrayList<>();
        for (OrderItemEntity item : order.getItems()) {
            items.add(new OrderDetailsDto.OrderItemDetail(
                    item.getMenuItem().getName(),
                    item.getItemPrice(),
                    item.getQuantity(),
                    item.getLineTotal()
            ));
        }
        dto.setItems(items);

        return ResponseEntity.ok(dto);
    }

    @PutMapping("/cancel/{orderId}")
    public ResponseEntity<OrderSummaryDto> cancelOrder(@PathVariable Long orderId) {
        CustomerOrderEntity updated = orderManagementService.updateOrderStatus(orderId, "CANCELLED");
        OrderSummaryDto dto = new OrderSummaryDto(
                updated.getId(),
                updated.getOrderNumber(),
                updated.getStatus(),
                updated.getTotalAmount()
        );
        return ResponseEntity.ok(dto);
    }
}
package com.pizza.user.service.impl;

import com.pizza.user.dto.NotificationDto;
import com.pizza.user.dto.OrderDetailsDto;
import com.pizza.user.dto.OrderSummaryDto;
import com.pizza.user.service.OrderDetailsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class OrderDetailsServiceImpl implements OrderDetailsService {

    private final RestTemplate restTemplate;
    private final String adminBaseUrl;
    private final String notificationBaseUrl;

    public OrderDetailsServiceImpl(RestTemplate restTemplate,
                                   @Value("${pizza.admin.base-url}") String adminBaseUrl,
                                   @Value("${pizza.notification.base-url}") String notificationBaseUrl) {
        this.restTemplate = restTemplate;
        this.adminBaseUrl = adminBaseUrl;
        this.notificationBaseUrl = notificationBaseUrl;
    }

    @Override
    public OrderDetailsDto getOrderDetails(Long orderId) {
        try {
            String url = adminBaseUrl + "/api/orders/details/" + orderId;
            return restTemplate.getForObject(url, OrderDetailsDto.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public OrderSummaryDto cancelOrder(Long orderId) {
        try {
            String url = adminBaseUrl + "/api/orders/cancel/" + orderId;
            return restTemplate.exchange(url, org.springframework.http.HttpMethod.PUT, null, OrderSummaryDto.class).getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<NotificationDto> getOrderNotifications(Long orderId) {
        try {
            String url = notificationBaseUrl + "/api/notifications/order/" + orderId;
            NotificationDto[] response = restTemplate.getForObject(url, NotificationDto[].class);
            if (response == null) {
                return Collections.emptyList();
            }
            return Arrays.asList(response);
        } catch (Exception e) {
            System.err.println("Could not fetch notifications: " + e.getMessage());
            return Collections.emptyList();
        }
    }
}
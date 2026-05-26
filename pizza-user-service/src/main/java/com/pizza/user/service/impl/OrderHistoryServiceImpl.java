package com.pizza.user.service.impl;

import com.pizza.user.dto.OrderSummaryDto;
import com.pizza.user.service.OrderHistoryService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class OrderHistoryServiceImpl implements OrderHistoryService {

    private final RestTemplate restTemplate;
    private final String adminBaseUrl;

    public OrderHistoryServiceImpl(RestTemplate restTemplate,
                                   @Value("${pizza.admin.base-url}") String adminBaseUrl) {
        this.restTemplate = restTemplate;
        this.adminBaseUrl = adminBaseUrl;
    }

    @Override
    public List<OrderSummaryDto> getOrdersForCustomer(Long customerId) {
        try {
            String url = adminBaseUrl + "/api/orders/by-customer/" + customerId;
            OrderSummaryDto[] response = restTemplate.getForObject(url, OrderSummaryDto[].class);
            if (response == null) {
                return Collections.emptyList();
            }
            return Arrays.asList(response);
        } catch (RestClientException ex) {
            return Collections.emptyList();
        }
    }
}

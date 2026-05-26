package com.pizza.user.service.impl;

import com.pizza.user.dto.MenuItemDto;
import com.pizza.user.service.MenuBrowseService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class MenuBrowseServiceImpl implements MenuBrowseService {

    private final RestTemplate restTemplate;
    private final String adminBaseUrl;

    public MenuBrowseServiceImpl(RestTemplate restTemplate,
                                 @Value("${pizza.admin.base-url}") String adminBaseUrl) {
        this.restTemplate = restTemplate;
        this.adminBaseUrl = adminBaseUrl;
    }

    @Override
    public List<MenuItemDto> getMenuItems(String keyword, String category, Boolean veg, String sort) {
        // Build the URL with query parameters
        String url = UriComponentsBuilder.fromHttpUrl(adminBaseUrl + "/api/admin/menu-items")
                .queryParam("keyword", keyword)
                .queryParam("category", category)
                .queryParam("veg", veg)
                .queryParam("sort", sort)
                .encode()
                .toUriString();

        try {
            MenuItemDto[] response = restTemplate.getForObject(url, MenuItemDto[].class);
            if (response == null) {
                return Collections.emptyList();
            }
            return Arrays.asList(response);
        } catch (Exception e) {
            System.err.println("ERROR: Could not fetch menu from Admin Service (" + url + ")");
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public MenuItemDto getMenuItemById(Long id) {
        try {
            String url = adminBaseUrl + "/api/admin/menu-items/" + id;
            return restTemplate.getForObject(url, MenuItemDto.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
package com.pizza.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class PizzaAdminServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PizzaAdminServiceApplication.class, args);
    }

    @Bean
    @LoadBalanced // <--- THIS ENABLES SERVICE NAME LOOKUP
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
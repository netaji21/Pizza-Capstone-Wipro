package com.pizza.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class PizzaUserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PizzaUserServiceApplication.class, args);
    }

    @Bean
    @LoadBalanced // <--- THIS ENABLES SERVICE NAME LOOKUP
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
package com.pizza.user.service;

import com.pizza.user.entity.CustomerEntity;
import com.pizza.user.repository.CustomerRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final CustomerRepository customerRepository;

    public CustomUserDetailsService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<CustomerEntity> customer = customerRepository.findByEmail(email);
        if (customer.isEmpty()) {
            throw new UsernameNotFoundException("Customer not found with email: " + email);
        }
        // Return a Spring Security User object
        return User.builder()
                .username(customer.get().getEmail())
                .password(customer.get().getPassword()) // Encrypted password
                .roles(customer.get().getRole())
                .build();
    }
}
package com.pizza.user.config;

import com.pizza.user.entity.CustomerEntity;
import com.pizza.user.repository.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserDataSeeder implements CommandLineRunner {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDataSeeder(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (customerRepository.count() == 0) {
            CustomerEntity customer = new CustomerEntity();
            customer.setName("John Doe");
            customer.setEmail("user@pizza.com");
            customer.setPhone("8888888888");
            customer.setAddress("123 Pizza Street");
            // Important: We encode the password!
            customer.setPassword(passwordEncoder.encode("user"));
            customer.setRole("CUSTOMER");

            customerRepository.save(customer);
            System.out.println("------ DATA SEEDER: Created Default User (user@pizza.com / user) ------");
        }
    }
}
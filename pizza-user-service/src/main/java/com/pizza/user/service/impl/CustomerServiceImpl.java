package com.pizza.user.service.impl;

import com.pizza.user.entity.CustomerEntity;
import com.pizza.user.repository.CustomerRepository;
import com.pizza.user.service.CustomerService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public CustomerEntity register(CustomerEntity customer) {
        return customerRepository.save(customer);
    }

    @Override
    public CustomerEntity login(String email, String password) {
        // Controller verifies password hash
        return customerRepository.findByEmailAndPassword(email, password)
                .orElse(null);
    }

    @Override
    public void logout(String email) {
        // In this stateless architecture (JWT/Cookie), the server doesn't need to 
        // delete a session from the DB. The Controller handles removing the cookie.
        // We log this event here for audit purposes.
        System.out.println("AUDIT: Customer logged out - " + email);
    }

    @Override
    public List<CustomerEntity> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public CustomerEntity getCustomerById(Long id) {
        return customerRepository.findById(id).orElse(null);
    }

    @Override
    public CustomerEntity updateCustomer(Long id, CustomerEntity updated) {
        CustomerEntity existing = customerRepository.findById(id).orElse(null);
        if (existing == null) {
            return null;
        }
        existing.setName(updated.getName());
        existing.setEmail(updated.getEmail());
        existing.setPhone(updated.getPhone());
        existing.setPassword(updated.getPassword());
        existing.setAddress(updated.getAddress());
        return customerRepository.save(existing);
    }

    @Override
    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }
}
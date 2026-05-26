package com.pizza.user.service;

import java.util.List;

import com.pizza.user.entity.CustomerEntity;

public interface CustomerService {

    CustomerEntity register(CustomerEntity customer);

    CustomerEntity login(String email, String password);

    void logout(String email);

    List<CustomerEntity> getAllCustomers();

    CustomerEntity getCustomerById(Long id);

    CustomerEntity updateCustomer(Long id, CustomerEntity updated);

    void deleteCustomer(Long id);
}

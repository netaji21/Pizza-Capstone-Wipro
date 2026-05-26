package com.pizza.admin.repository;

import com.pizza.admin.entity.CustomerOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerOrderRepository extends JpaRepository<CustomerOrderEntity, Long> {

    // To use later for user-specific order history
    List<CustomerOrderEntity> findByCustomerId(Long customerId);
}

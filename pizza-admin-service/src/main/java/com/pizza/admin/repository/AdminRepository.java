package com.pizza.admin.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pizza.admin.entity.AdminEntity;

public interface AdminRepository extends JpaRepository<AdminEntity, Long> {

    Optional<AdminEntity> findByEmail(String email);

    Optional<AdminEntity> findByEmailAndPassword(String email, String password);
}

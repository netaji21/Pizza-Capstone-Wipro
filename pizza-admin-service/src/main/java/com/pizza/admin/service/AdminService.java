package com.pizza.admin.service;

import java.util.List;

import com.pizza.admin.entity.AdminEntity;

public interface AdminService {

    AdminEntity register(AdminEntity admin);

    AdminEntity login(String email, String password);

    void logout(String email);  // for now just a dummy

    List<AdminEntity> getAllAdmins();

    AdminEntity getAdminById(Long id);

    AdminEntity updateAdmin(Long id, AdminEntity updated);

    void deleteAdmin(Long id);
}

package com.pizza.admin.service.impl;

import com.pizza.admin.entity.AdminEntity;
import com.pizza.admin.repository.AdminRepository;
import com.pizza.admin.service.AdminService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;

    public AdminServiceImpl(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    public AdminEntity register(AdminEntity admin) {
        // Save the admin to the database
        return adminRepository.save(admin);
    }

    @Override
    public AdminEntity login(String email, String password) {
        // Verification happens in the Controller using PasswordEncoder
        // This method simply finds the user
        return adminRepository.findByEmailAndPassword(email, password)
                .orElse(null);
    }

    @Override
    public void logout(String email) {
        // In this stateless architecture (JWT/Cookie), the server doesn't need to 
        // delete a session from the DB. The Controller handles removing the cookie.
        // We log this event here for audit purposes.
        System.out.println("AUDIT: Admin logged out - " + email);
    }

    @Override
    public List<AdminEntity> getAllAdmins() {
        return adminRepository.findAll();
    }

    @Override
    public AdminEntity getAdminById(Long id) {
        return adminRepository.findById(id).orElse(null);
    }

    @Override
    public AdminEntity updateAdmin(Long id, AdminEntity updated) {
        AdminEntity existing = adminRepository.findById(id).orElse(null);
        if (existing == null) {
            return null;
        }
        existing.setName(updated.getName());
        existing.setEmail(updated.getEmail());
        existing.setPhone(updated.getPhone());
        existing.setPassword(updated.getPassword());
        return adminRepository.save(existing);
    }

    @Override
    public void deleteAdmin(Long id) {
        adminRepository.deleteById(id);
    }
}
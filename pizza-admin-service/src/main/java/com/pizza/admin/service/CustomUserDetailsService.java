package com.pizza.admin.service;

import com.pizza.admin.entity.AdminEntity;
import com.pizza.admin.repository.AdminRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AdminRepository adminRepository;

    public CustomUserDetailsService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<AdminEntity> admin = adminRepository.findByEmail(email);
        if (admin.isEmpty()) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        // Return a Spring Security User object
        return User.builder()
                .username(admin.get().getEmail())
                .password(admin.get().getPassword()) // This is the encrypted password from DB
                .roles(admin.get().getRole())
                .build();
    }
}
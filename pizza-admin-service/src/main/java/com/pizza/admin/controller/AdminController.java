package com.pizza.admin.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pizza.admin.entity.AdminEntity;
import com.pizza.admin.service.AdminService;

@RestController
@RequestMapping("/api/admins")
@CrossOrigin(origins = "*") // later you can restrict
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // REGISTER
    @PostMapping("/register")
    public ResponseEntity<AdminEntity> register(@RequestBody AdminEntity admin) {
        AdminEntity saved = adminService.register(admin);
        return ResponseEntity.ok(saved);
    }

    // LOGIN
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String email,
                                   @RequestParam String password) {
        AdminEntity admin = adminService.login(email, password);
        if (admin == null) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
        return ResponseEntity.ok(admin);  // later you will return JWT
    }

    // LOGOUT
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestParam String email) {
        adminService.logout(email);
        return ResponseEntity.ok("Logged out (dummy for now)");
    }

    // CRUD

    @GetMapping
    public List<AdminEntity> getAllAdmins() {
        return adminService.getAllAdmins();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminEntity> getById(@PathVariable Long id) {
        AdminEntity admin = adminService.getAdminById(id);
        if (admin == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(admin);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdminEntity> update(@PathVariable Long id,
                                              @RequestBody AdminEntity updated) {
        AdminEntity admin = adminService.updateAdmin(id, updated);
        if (admin == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(admin);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        adminService.deleteAdmin(id);
        return ResponseEntity.noContent().build();
    }
}

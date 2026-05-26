package com.pizza.user.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pizza.user.entity.CustomerEntity;
import com.pizza.user.service.CustomerService;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = "*")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // REGISTER
    @PostMapping("/register")
    public ResponseEntity<CustomerEntity> register(@RequestBody CustomerEntity customer) {
        CustomerEntity saved = customerService.register(customer);
        return ResponseEntity.ok(saved);
    }

    // LOGIN
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String email,
                                   @RequestParam String password) {
        CustomerEntity customer = customerService.login(email, password);
        if (customer == null) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
        return ResponseEntity.ok(customer); // later return token
    }

    // LOGOUT
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestParam String email) {
        customerService.logout(email);
        return ResponseEntity.ok("Logged out (dummy for now)");
    }

    // CRUD

    @GetMapping
    public List<CustomerEntity> getAll() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerEntity> getById(@PathVariable Long id) {
        CustomerEntity customer = customerService.getCustomerById(id);
        if (customer == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(customer);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerEntity> update(@PathVariable Long id,
                                                 @RequestBody CustomerEntity updated) {
        CustomerEntity customer = customerService.updateCustomer(id, updated);
        if (customer == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(customer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
}

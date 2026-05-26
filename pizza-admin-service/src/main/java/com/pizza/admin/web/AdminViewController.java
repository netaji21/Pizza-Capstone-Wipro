package com.pizza.admin.web;

import com.pizza.admin.entity.AdminEntity;
import com.pizza.admin.repository.AdminRepository;
import com.pizza.admin.service.OrderManagementService;
import com.pizza.admin.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminViewController {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final OrderManagementService orderService;

    public AdminViewController(AdminRepository adminRepository, 
                               PasswordEncoder passwordEncoder, 
                               JwtUtil jwtUtil,
                               OrderManagementService orderService) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.orderService = orderService;
    }

    @GetMapping("/login")
    public String showLogin() {
        return "admin-login";
    }

    @PostMapping("/login")
    public String handleLogin(@RequestParam String email,
                              @RequestParam String password,
                              HttpServletResponse response,
                              Model model) {
        
        Optional<AdminEntity> adminOpt = adminRepository.findByEmail(email);

        if (adminOpt.isPresent()) {
            if (passwordEncoder.matches(password, adminOpt.get().getPassword())) {
                String token = jwtUtil.generateToken(email);
                
                // Set "admin_token"
                Cookie cookie = new Cookie("admin_token", token); // <--- CHANGED HERE
                cookie.setHttpOnly(true);
                cookie.setPath("/");
                cookie.setMaxAge(60 * 60 * 10); 
                response.addCookie(cookie);
                return "redirect:/admin/home";
            }
        }
        
        model.addAttribute("error", "Invalid email or password");
        return "admin-login";
    }

    @GetMapping("/home")
    public String showHome(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/admin/login";
        }
        
        String email = principal.getName();
        Optional<AdminEntity> admin = adminRepository.findByEmail(email);
        
        if (admin.isPresent()) {
            model.addAttribute("admin", admin.get());
            
            BigDecimal revenue = orderService.calculateTotalRevenue();
            model.addAttribute("revenue", revenue);
            
            return "admin-home";
        }
        return "redirect:/admin/login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        // Delete "admin_token"
        Cookie cookie = new Cookie("admin_token", null); // <--- CHANGED HERE
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/admin/login";
    }
}
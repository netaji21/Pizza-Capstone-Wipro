package com.pizza.user.web;

import com.pizza.user.entity.CustomerEntity;
import com.pizza.user.repository.CustomerRepository;
import com.pizza.user.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/customers")
public class CustomerViewController {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public CustomerViewController(CustomerRepository customerRepository, 
                                  PasswordEncoder passwordEncoder, 
                                  JwtUtil jwtUtil) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("customer", new CustomerEntity());
        return "user-register";
    }

    @PostMapping("/register")
    public String handleRegister(@ModelAttribute("customer") CustomerEntity customer,
                                 Model model) {

        Optional<CustomerEntity> existing = customerRepository.findByEmail(customer.getEmail());
        if (existing.isPresent()) {
            model.addAttribute("error", "Email is already registered. Please login.");
            return "user-register";
        }

        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        customerRepository.save(customer);
        
        return "redirect:/customers/login?msg=Registration successful. Please login.";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "user-login";
    }

    @PostMapping("/login")
    public String handleLogin(@RequestParam("email") String email,
                              @RequestParam("password") String password,
                              HttpServletResponse response,
                              HttpSession session,
                              Model model) {

        Optional<CustomerEntity> customerOpt = customerRepository.findByEmail(email);
        
        if (customerOpt.isPresent()) {
            if (passwordEncoder.matches(password, customerOpt.get().getPassword())) {
                
                String token = jwtUtil.generateToken(email);

                // Set "user_token"
                Cookie cookie = new Cookie("user_token", token); // <--- CHANGED HERE
                cookie.setHttpOnly(true);
                cookie.setPath("/");
                cookie.setMaxAge(60 * 60 * 10);
                response.addCookie(cookie);

                session.setAttribute("loggedInCustomerId", customerOpt.get().getId());
                session.setAttribute("loggedInCustomerName", customerOpt.get().getName());

                return "redirect:/customers/home";
            }
        }

        model.addAttribute("error", "Invalid email or password");
        return "user-login";
    }

    @GetMapping("/home")
    public String showHome(HttpSession session, Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/customers/login";
        }

        String email = principal.getName();
        Optional<CustomerEntity> customerOpt = customerRepository.findByEmail(email);
        
        if (customerOpt.isPresent()) {
            model.addAttribute("customer", customerOpt.get());
            
            if (session.getAttribute("loggedInCustomerId") == null) {
                session.setAttribute("loggedInCustomerId", customerOpt.get().getId());
                session.setAttribute("loggedInCustomerName", customerOpt.get().getName());
            }
            
            return "user-home";
        }
        return "redirect:/customers/login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response, HttpSession session) {
        // Delete "user_token"
        Cookie cookie = new Cookie("user_token", null); // <--- CHANGED HERE
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        
        session.invalidate();
        
        return "redirect:/customers/login";
    }
}
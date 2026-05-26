package com.pizza.user.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex, Model model) {
        // Log to console so you can debug
        System.err.println("Global Exception Caught: " + ex.getMessage());
        ex.printStackTrace();

        // Pass error info to the view
        model.addAttribute("error", ex.getMessage());
        
        // Return "error.html"
        return "error";
    }
}
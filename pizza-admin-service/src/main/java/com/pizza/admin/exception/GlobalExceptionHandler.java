package com.pizza.admin.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex, Model model) {
        // Log the error (optional but good practice)
        ex.printStackTrace();
        
        // Add error message to the model so the HTML can display it
        model.addAttribute("error", ex.getMessage());
        
        // Return the "error" view name (error.html)
        return "error";
    }
}
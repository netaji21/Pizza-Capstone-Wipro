package com.pizza.payment.service;

import com.pizza.payment.dto.PaymentRequestDto;
import com.pizza.payment.entity.Payment;
import com.pizza.payment.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public Payment processPayment(PaymentRequestDto request) {
        Payment payment = new Payment(
                request.getOrderId(),
                request.getPaymentMode(),
                request.getAmount()
        );
        
        // Simulate payment processing
        payment.setTransactionId(UUID.randomUUID().toString());
        payment.setPaymentStatus("SUCCESS"); // Auto-approve for demo

        return paymentRepository.save(payment);
    }

    public Payment getPaymentByOrderId(Long orderId) {
        return paymentRepository.findByOrderId(orderId).orElse(null);
    }
}
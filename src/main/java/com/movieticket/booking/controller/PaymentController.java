package com.movieticket.booking.controller;

import com.movieticket.booking.dto.PaymentRequest;
import com.movieticket.booking.dto.PaymentResponse;
import com.movieticket.booking.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public PaymentResponse pay(@Valid @RequestBody PaymentRequest req, Authentication authentication) {
        return paymentService.processPayment(req, authentication.getName());
    }
}

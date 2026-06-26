package com.movieticket.booking.controller;

import com.movieticket.booking.dto.AuthResponse;
import com.movieticket.booking.dto.LoginRequest;
import com.movieticket.booking.dto.RegisterRequest;
import com.movieticket.booking.entity.User;
import com.movieticket.booking.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public User register(@Valid @RequestBody RegisterRequest req){
        return authService.register(req);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest req){
        return authService.login(req);
    }

}

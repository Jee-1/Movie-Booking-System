package com.movieticket.booking.service;

import com.movieticket.booking.dto.AuthResponse;
import com.movieticket.booking.dto.LoginRequest;
import com.movieticket.booking.dto.RegisterRequest;
import com.movieticket.booking.entity.User;
import com.movieticket.booking.repository.UserRepository;
import com.movieticket.booking.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public User register(RegisterRequest req){
        if(userRepository.existsByEmail(req.getEmail()))
            throw new RuntimeException("User already exists");
        User user = new User();
        user.setEmail(req.getEmail());
        user.setName(req.getName());
        user.setRole(req.getRole());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        return userRepository.save(user);
    }

    public AuthResponse login(LoginRequest req){
        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid Email or Password"));
        if(!passwordEncoder.matches(req.getPassword(), user.getPassword()))
            throw new RuntimeException("Invalid Email or Password");
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        return new AuthResponse(user.getEmail(), token, user.getRole());
    }

}

package com.movieticket.booking.dto;

import com.movieticket.booking.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class AuthResponse {

    private String email;
    private String token;
    private Role role;

}

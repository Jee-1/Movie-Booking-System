package com.movieticket.booking.dto;

import com.movieticket.booking.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    private String name;
    private String email;
    private String password;
    private Role role;

}

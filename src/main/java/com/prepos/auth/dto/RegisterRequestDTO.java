package com.prepos.auth.dto;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequestDTO {
    private String username;
    @Email(message="Invalid Email format")
    private String email;
    private String password;
}

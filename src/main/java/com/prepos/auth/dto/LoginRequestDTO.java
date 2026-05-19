package com.prepos.auth.dto;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDTO {
    @Email(message="Invalid Email Format")
    private String email;
    private String password;
}

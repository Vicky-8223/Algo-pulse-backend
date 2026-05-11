package com.prepos.auth.service;

import com.prepos.auth.dto.AuthResponseDTO;
import com.prepos.auth.dto.LoginRequestDTO;
import com.prepos.auth.dto.RegisterRequestDTO;

public interface AuthService {
    AuthResponseDTO register(RegisterRequestDTO register);
    AuthResponseDTO login(LoginRequestDTO login);
}

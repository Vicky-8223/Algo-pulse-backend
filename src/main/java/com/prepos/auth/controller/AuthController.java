package com.prepos.auth.controller;

import com.prepos.auth.dto.AuthResponseDTO;
import com.prepos.auth.dto.LoginRequestDTO;
import com.prepos.auth.dto.RegisterRequestDTO;
import com.prepos.auth.service.AuthService;
import com.prepos.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    @PostMapping("/register")
    public ApiResponse<AuthResponseDTO> register(@RequestBody RegisterRequestDTO request){
        AuthResponseDTO  response= authService.register(request);
        return new ApiResponse<>(true,"Registration successful",response);
    }
    @PostMapping("/login")
    public ApiResponse<AuthResponseDTO> login(@RequestBody LoginRequestDTO login){
        AuthResponseDTO response=authService.login(login);
        return new ApiResponse<>(true,"Login successful",response);
    }
}

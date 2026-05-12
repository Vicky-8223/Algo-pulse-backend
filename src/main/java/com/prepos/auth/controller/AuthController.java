package com.prepos.auth.controller;

import com.prepos.auth.dto.AuthResponseDTO;
import com.prepos.auth.dto.LoginRequestDTO;
import com.prepos.auth.dto.RegisterRequestDTO;
import com.prepos.auth.dto.UpdateLeetcodeDTO;
import com.prepos.auth.entity.User;
import com.prepos.auth.repository.UserRepository;
import com.prepos.auth.service.AuthService;
import com.prepos.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserRepository userRepository;

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
    @PutMapping("/leetcode")
    public String updateLeetcodeUsername(@RequestBody UpdateLeetcodeDTO request, Authentication authentication){
        String email=authentication.getName();
        User user=userRepository.findByEmail(email).orElseThrow(()->new RuntimeException("User not found"));
        user.setLeetcodeUserName(request.getLeetCodeUsername());
        userRepository.save(user);
        return "LeetCode username updated";
    }
}

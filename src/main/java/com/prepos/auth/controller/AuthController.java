package com.prepos.auth.controller;

import com.prepos.analytics.AnalyticsService;
import com.prepos.auth.dto.*;
import com.prepos.auth.entity.User;
import com.prepos.auth.repository.UserRepository;
import com.prepos.auth.service.AuthService;
import com.prepos.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthService authService;
    private final UserRepository userRepository;
    private final AnalyticsService analyticsService;

    @PostMapping("/register")
    public ApiResponse<AuthResponseDTO> register(@RequestBody RegisterRequestDTO request){
        AuthResponseDTO  response= authService.register(request);
        return new ApiResponse<>(true,"Registration successful",response);
    }
    @PostMapping("/login")
    public ApiResponse<AuthResponseDTO> login(@RequestBody LoginRequestDTO login)throws Exception{
        AuthResponseDTO response=authService.login(login);
        User user=userRepository.findByEmail(login.getEmail()).orElseThrow();
        String LeetCoderUserName=user.getLeetcodeUserName();
        analyticsService.syncUserAnalytics(user,LeetCoderUserName);
        analyticsService.syncSolvedProblems(user);
        analyticsService.enrichSolvedProblems(user);
        analyticsService.generateFeatureSnapshot(user);
        analyticsService.generateWeaknessScore(user);
        log.info("Synced analytics for {}",user.getEmail());
        return new ApiResponse<>(true,"Login successful",response);
    }
    @PutMapping("/leetcode")
    public ApiResponse<String> updateLeetcodeUsername(@RequestBody UpdateLeetcodeDTO request, Authentication authentication){
        String email=authentication.getName();
        User user=userRepository.findByEmail(email).orElseThrow(()->new RuntimeException("User not found"));
        user.setLeetcodeUserName(request.getLeetCodeUsername());
        userRepository.save(user);
        return new ApiResponse<>(true,"LeetCode username updated",user.getLeetcodeUserName());
    }
    @GetMapping("/me")
    public UserProfileDTO getCurrentUser(Authentication authentication){
        if(authentication==null){
            throw new RuntimeException("Unauthorized");
        }
        String email=authentication.getName();
        User user=userRepository.findByEmail(email).orElseThrow(()->new RuntimeException("User not found"));
        return UserProfileDTO.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .leetcodeUsername(user.getLeetcodeUserName())
                .build();
    }
}

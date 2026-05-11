package com.prepos.auth.service.impl;

import com.prepos.auth.dto.AuthResponseDTO;
import com.prepos.auth.dto.LoginRequestDTO;
import com.prepos.auth.dto.RegisterRequestDTO;
import com.prepos.auth.entity.User;
import com.prepos.auth.repository.UserRepository;
import com.prepos.auth.security.jwt.JwtService;
import com.prepos.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    @Override
    public AuthResponseDTO register(RegisterRequestDTO register){
        if(userRepo.existsByUsername(register.getUsername())){
            throw new RuntimeException("Username already exists");
        }
        if(userRepo.existsByEmail(register.getEmail())){
            throw new RuntimeException("Email already exists");
        }
        User user=User.builder().username(register.getUsername())
                .email(register.getEmail())
                .password(passwordEncoder.encode(register.getPassword())).build();
        userRepo.save(user);
        return new AuthResponseDTO(null,"User registered successfully");
    }

    @Override
    public AuthResponseDTO login(LoginRequestDTO req){
        User user=userRepo.findByEmail(req.getEmail()).orElseThrow(()->new RuntimeException("Invalid email or password"));
        boolean matches=passwordEncoder.matches(req.getPassword(),user.getPassword());
        if(!matches){
            throw new RuntimeException("Invalid email or password");
        }
        String token = jwtService.generateToken(req.getEmail());
        return new AuthResponseDTO(token,"Login successful");
    }
}

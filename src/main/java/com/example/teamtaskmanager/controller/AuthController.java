package com.example.teamtaskmanager.controller;

import com.example.teamtaskmanager.entity.User;
import com.example.teamtaskmanager.repository.UserRepository;
import com.example.teamtaskmanager.security.JwtUtil;
import com.example.teamtaskmanager.service.AuthService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(AuthService authService,
                          UserRepository userRepo,
                          PasswordEncoder passwordEncoder,
                          JwtUtil jwtUtil) {
        this.authService = authService;
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/signup")
    public String signup(@RequestBody User user) {
        return authService.signup(user);
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody User user) {

        User existing = userRepo.findByEmail(user.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(user.getPassword(), existing.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtUtil.generateToken(existing.getEmail());

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("role", existing.getRole());

        return response;
    }
}
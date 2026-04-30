package com.example.teamtaskmanager.service;

import com.example.teamtaskmanager.entity.User;
import com.example.teamtaskmanager.repository.UserRepository;
import com.example.teamtaskmanager.security.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepo;
    private final JwtUtil jwtUtil = new JwtUtil();
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public AuthService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public String signup(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        userRepo.save(user);
        return "User Registered";
    }

    public String login(String email, String password) {
        User user = userRepo.findByEmail(email).orElseThrow();

        if (!encoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return jwtUtil.generateToken(email);
    }
}
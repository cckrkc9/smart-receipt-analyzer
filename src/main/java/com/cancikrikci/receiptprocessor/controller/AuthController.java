package com.cancikrikci.receiptprocessor.controller;

import com.cancikrikci.receiptprocessor.entity.User;
import com.cancikrikci.receiptprocessor.dto.response.AuthResponse;
import com.cancikrikci.receiptprocessor.dto.request.LoginRequest;
import com.cancikrikci.receiptprocessor.dto.request.RegisterRequest;
import com.cancikrikci.receiptprocessor.repository.UserRepository;
import com.cancikrikci.receiptprocessor.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            Optional<User> existingUser = userRepository.findByUsername(request.getUsername());
            if (existingUser.isPresent()) {
                log.warn("Username already exists: {}", request.getUsername());
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(AuthResponse.builder()
                                .message("Username already exists")
                                .build());
            }

            User user = User.builder()
                    .userId(UUID.randomUUID().toString())
                    .username(request.getUsername())
                    .email(request.getEmail())
                    .passwordHash(passwordEncoder.encode(request.getPassword()))
                    .role(request.getRole() != null ? request.getRole() : "USER")
                    .createdAt(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                    .build();

            userRepository.save(user);

            String token = jwtUtil.generateToken(user.getUsername());

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(AuthResponse.builder()
                            .token(token)
                            .username(user.getUsername())
                            .email(user.getEmail())
                            .role(user.getRole())
                            .message("User registered successfully")
                            .build());

        } catch (Exception e) {
            log.error("Registration failed for username: {}", request.getUsername(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(AuthResponse.builder()
                            .message("Registration failed: " + e.getMessage())
                            .build());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            String token = jwtUtil.generateToken(authentication.getName());

            User user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            return ResponseEntity.ok(AuthResponse.builder()
                    .token(token)
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .role(user.getRole())
                    .message("Login successful")
                    .build());

        } catch (AuthenticationException e) {
            log.error("Login failed for username: {}", request.getUsername(), e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(AuthResponse.builder()
                            .message("Invalid username or password")
                            .build());
        } catch (Exception e) {
            log.error("Unexpected error during login for username: {}", request.getUsername(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(AuthResponse.builder()
                            .message("Login failed: " + e.getMessage())
                            .build());
        }
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                
                if (jwtUtil.validateToken(token)) {
                    String username = jwtUtil.extractUsername(token);
                    
                    return ResponseEntity.ok(AuthResponse.builder()
                            .username(username)
                            .message("Token is valid")
                            .build());
                }
            }
            
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(AuthResponse.builder()
                            .message("Invalid token")
                            .build());
                            
        } catch (Exception e) {
            log.error("Token validation error", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(AuthResponse.builder()
                            .message("Token validation failed")
                            .build());
        }
    }
}


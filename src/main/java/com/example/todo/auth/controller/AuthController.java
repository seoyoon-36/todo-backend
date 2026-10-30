package com.example.todo.auth.controller;

import com.example.todo.auth.jwt.JwtTokenProvider;
import com.example.todo.user.entity.User;
import com.example.todo.user.repository.UserRepository;
import com.example.todo.auth.dto.AuthDto.*;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository users;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authManager;
    private final JwtTokenProvider tokenProvider;

    public AuthController(
            UserRepository users,
            PasswordEncoder encoder,
            AuthenticationManager authManager,
            JwtTokenProvider tokenProvider) {
        this.users = users;
        this.encoder = encoder;
        this.authManager = authManager;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(
            @Valid @RequestBody SignupRequest request) {

        String email = request.email().trim().toLowerCase();

        if (users.existsByEmail(email)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("이미 가입된 이메일입니다.");
        }

        User user = users.save(new User(
                email,
                encoder.encode(request.password()),
                request.nickname()
        ));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new UserResponse(
                        user.getId(),
                        user.getEmail(),
                        user.getNickname()
                ));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody LoginRequest request) {

        String email = request.email().trim().toLowerCase();

        try {
            authManager.authenticate(
                    UsernamePasswordAuthenticationToken.unauthenticated(
                            email, request.password()
                    )
            );
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("이메일 또는 비밀번호가 올바르지 않습니다.");
        }

        User user = users.findByEmail(email).orElseThrow();

        return ResponseEntity.ok(new TokenResponse(
                tokenProvider.createAccessToken(user.getEmail()),
                "Bearer",
                tokenProvider.getExpirationSeconds(),
                new UserResponse(
                        user.getId(),
                        user.getEmail(),
                        user.getNickname()
                )
        ));
    }
}

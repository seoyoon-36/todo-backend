package com.example.todo.auth.dto;

import jakarta.validation.constraints.*;

public class AuthDto {

    public record SignupRequest(
            @NotBlank @Email String email,
            @NotBlank @Size(min = 8) String password,
            @NotBlank String nickname
    ) {}

    public record LoginRequest(
            @NotBlank String email,
            @NotBlank String password
    ) {}

    public record UserResponse(
            Long id,
            String email,
            String nickname
    ) {}

    public record TokenResponse(
            String accessToken,
            String tokenType,
            long expiresIn,
            UserResponse user
    ) {}
}

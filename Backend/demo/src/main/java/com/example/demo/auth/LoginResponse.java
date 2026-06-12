package com.example.demo.auth;

public record LoginResponse(
        String token,
        String refreshToken,
        Long userId,
        String username,
        String fullName,
        String email,
        String role,
        String roleCode
) {
}

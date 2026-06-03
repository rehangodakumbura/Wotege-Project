package com.example.demo.auth;

public record LoginResponse(
	String token,
	Long userId,
	String username,
	String fullName,
	String email,
	String role
) {
}
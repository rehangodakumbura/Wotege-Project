package com.example.demo.auth;

public record UpdateProfileRequest(
	String fullName,
	String email
) {
}

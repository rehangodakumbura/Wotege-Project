package com.example.demo.auth;

public record ChangePasswordRequest(
	String currentPassword,
	String newPassword
) {
}

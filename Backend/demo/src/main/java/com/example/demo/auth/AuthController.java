package com.example.demo.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
		return ResponseEntity.ok(authService.login(request));
	}

	@GetMapping("/me")
	public ResponseEntity<UserAccount> me(Authentication authentication) {
		Long userId = (Long) authentication.getPrincipal();
		return ResponseEntity.ok(authService.getCurrentUser(userId));
	}

	@PutMapping("/me")
	public ResponseEntity<UserAccount> updateProfile(
			Authentication authentication,
			@RequestBody UpdateProfileRequest request) {
		Long userId = (Long) authentication.getPrincipal();
		return ResponseEntity.ok(authService.updateProfile(userId, request));
	}

	@PutMapping("/me/password")
	public ResponseEntity<UserAccount> changePassword(
			Authentication authentication,
			@RequestBody ChangePasswordRequest request) {
		Long userId = (Long) authentication.getPrincipal();
		return ResponseEntity.ok(authService.changePassword(userId, request));
	}

	@PutMapping("/me/preferences")
	public ResponseEntity<UserAccount> updatePreferences(
			Authentication authentication,
			@RequestBody UpdatePreferencesRequest request) {
		Long userId = (Long) authentication.getPrincipal();
		return ResponseEntity.ok(authService.updatePreferences(userId, request));
	}
}

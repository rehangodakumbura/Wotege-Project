package com.example.demo.auth;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class AuthService {

	private final UserAccountRepository userAccountRepository;
	private final JwtUtil jwtUtil;

	public AuthService(UserAccountRepository userAccountRepository, JwtUtil jwtUtil) {
		this.userAccountRepository = userAccountRepository;
		this.jwtUtil = jwtUtil;
	}

	@Transactional(readOnly = true)
	public LoginResponse login(LoginRequest request) {
		UserAccount account = userAccountRepository.findByEmail(request.email())
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password"));

		if (!account.isActive() || !account.getPassword().equals(request.password())) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
		}

		String role = account.getRole() != null ? account.getRole().getCode() : null;
		String token = jwtUtil.generate(account.getId(), account.getEmail(), role);

		return new LoginResponse(
			token,
			account.getId(),
			account.getUsername(),
			account.getFullName(),
			account.getEmail(),
			role
		);
	}

	@Transactional(readOnly = true)
	public UserAccount getCurrentUser(Long userId) {
		return userAccountRepository.findById(userId)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
	}

	public UserAccount updateProfile(Long userId, UpdateProfileRequest request) {
		UserAccount account = getCurrentUser(userId);

		if (request.fullName() == null || request.fullName().isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Full name is required");
		}
		if (request.email() == null || request.email().isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is required");
		}

		String newEmail = request.email().trim().toLowerCase();
		if (!newEmail.equals(account.getEmail())) {
			userAccountRepository.findByEmail(newEmail)
				.filter(other -> !other.getId().equals(account.getId()))
				.ifPresent(existing -> {
					throw new ResponseStatusException(HttpStatus.CONFLICT, "Email is already in use");
				});
		}

		account.setFullName(request.fullName().trim());
		account.setEmail(newEmail);
		return userAccountRepository.save(account);
	}

	public UserAccount changePassword(Long userId, ChangePasswordRequest request) {
		UserAccount account = getCurrentUser(userId);

		if (request.currentPassword() == null || request.currentPassword().isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Current password is required");
		}
		if (request.newPassword() == null || request.newPassword().isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "New password is required");
		}
		if (request.newPassword().length() < 6) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "New password must be at least 6 characters");
		}
		if (!account.getPassword().equals(request.currentPassword())) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Current password is incorrect");
		}

		account.setPassword(request.newPassword());
		return userAccountRepository.save(account);
	}

	public UserAccount updatePreferences(Long userId, UpdatePreferencesRequest request) {
		UserAccount account = getCurrentUser(userId);

		if (request.theme() != null) {
			String theme = request.theme().toLowerCase();
			if (!theme.equals("dark") && !theme.equals("light")) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid theme");
			}
			account.setTheme(theme);
		}
		if (request.language() != null) {
			String language = request.language().toLowerCase();
			if (!language.equals("en") && !language.equals("fr") && !language.equals("sw") && !language.equals("ar")) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid language");
			}
			account.setLanguage(language);
		}
		if (request.notificationsEnabled() != null) {
			account.setNotificationsEnabled(request.notificationsEnabled());
		}
		if (request.emailNotifications() != null) {
			account.setEmailNotifications(request.emailNotifications());
		}
		return userAccountRepository.save(account);
	}
}

package com.example.demo.auth;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional(readOnly = true)
public class AuthService {

	private final UserAccountRepository userAccountRepository;
	private final JwtUtil jwtUtil;

	public AuthService(UserAccountRepository userAccountRepository, JwtUtil jwtUtil) {
		this.userAccountRepository = userAccountRepository;
		this.jwtUtil = jwtUtil;
	}

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

	public UserAccount getCurrentUser(Long userId) {
		return userAccountRepository.findById(userId)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
	}
}

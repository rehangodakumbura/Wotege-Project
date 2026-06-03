package com.example.demo.auth;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional(readOnly = true)
public class AuthService {

	private final UserAccountRepository userAccountRepository;

	public AuthService(UserAccountRepository userAccountRepository) {
		this.userAccountRepository = userAccountRepository;
	}

	public LoginResponse login(LoginRequest request) {
		UserAccount account = userAccountRepository.findByUsername(request.username())
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password"));

		if (!account.isActive() || !account.getPassword().equals(request.password())) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
		}

		return new LoginResponse(
			UUID.randomUUID().toString(),
			account.getId(),
			account.getUsername(),
			account.getFullName(),
			account.getEmail(),
			account.getRole() != null ? account.getRole().getName() : null
		);
	}
}
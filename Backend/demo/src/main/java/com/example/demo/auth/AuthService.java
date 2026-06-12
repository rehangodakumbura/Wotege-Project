package com.example.demo.auth;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AuthService {

    private final UserAccountRepository userAccountRepository;

    public AuthService(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    public LoginResponse login(LoginRequest request) {
        throw new UnsupportedOperationException("Use AuthenticationManager-based login in AuthController instead");
    }

    public LoginResponse buildLoginResponse(String username, String token, String refreshToken) {
        UserAccount account = userAccountRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new LoginResponse(
                token,
                refreshToken,
                account.getId(),
                account.getUsername(),
                account.getFullName(),
                account.getEmail(),
                account.getRole() != null ? account.getRole().getName() : null,
                account.getRole() != null ? account.getRole().getCode() : null
        );
    }

    public String getUserRole(String username) {
        UserAccount account = userAccountRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return account.getRole() != null ? account.getRole().getCode() : "USER";
    }

    public Map<String, Object> getUserInfo(String username) {
        UserAccount account = userAccountRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Map<String, Object> info = new LinkedHashMap<>();
        info.put("userId", account.getId());
        info.put("username", account.getUsername());
        info.put("fullName", account.getFullName());
        info.put("email", account.getEmail());
        info.put("role", account.getRole() != null ? account.getRole().getName() : null);
        info.put("roleCode", account.getRole() != null ? account.getRole().getCode() : null);
        return info;
    }
}

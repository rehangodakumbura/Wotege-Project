package com.wotege.hotel.service;

import com.wotege.hotel.dto.auth.LoginRequest;
import com.wotege.hotel.dto.auth.LoginResponse;
import com.wotege.hotel.dto.auth.RegisterRequest;
import com.wotege.hotel.entity.User;
import com.wotege.hotel.exception.BadRequestException;
import com.wotege.hotel.repository.UserRepository;
import com.wotege.hotel.config.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public LoginResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already exists");
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole() != null ? request.getRole() : User.Role.RECEPTIONIST)
                .build();

        user = userRepository.save(user);
        String token = jwtService.generateToken(user);

        return new LoginResponse(token, "Bearer", jwtService.getExpirationTime(),
                user.getUsername(), user.getEmail(), user.getRole());
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsernameOrEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByUsernameOrEmail(
                        request.getUsernameOrEmail(),
                        request.getUsernameOrEmail())
                .orElseThrow(() -> new BadRequestException("Invalid credentials"));

        String token = jwtService.generateToken(user);

        return new LoginResponse(token, "Bearer", jwtService.getExpirationTime(),
                user.getUsername(), user.getEmail(), user.getRole());
    }
}

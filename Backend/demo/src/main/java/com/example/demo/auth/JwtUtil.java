package com.example.demo.auth;

import java.time.Instant;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

	private final SecretKey key;
	private final long expirationMs;

	public JwtUtil(
		@Value("${jwt.secret:WotegeHotelRestaurantSecretKey2024VeryLongAndSecure256Bits!!}") String secret,
		@Value("${jwt.expiration-ms:86400000}") long expirationMs
	) {
		this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(
			java.util.Base64.getEncoder().encodeToString(secret.getBytes())
		));
		this.expirationMs = expirationMs;
	}

	public String generate(Long userId, String email, String role) {
		Date now = new Date();
		return Jwts.builder()
			.subject(userId.toString())
			.claim("email", email)
			.claim("role", role)
			.issuedAt(now)
			.expiration(new Date(now.getTime() + expirationMs))
			.signWith(key)
			.compact();
	}

	public Long getUserId(String token) {
		return Long.parseLong(parse(token).getPayload().getSubject());
	}

	public String getEmail(String token) {
		return parse(token).getPayload().get("email", String.class);
	}

	public boolean isValid(String token) {
		try {
			parse(token);
			return true;
		} catch (JwtException e) {
			return false;
		}
	}

	private Jws<Claims> parse(String token) {
		return Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
	}
}

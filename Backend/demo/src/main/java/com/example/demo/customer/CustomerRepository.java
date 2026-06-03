package com.example.demo.customer;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
	long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}

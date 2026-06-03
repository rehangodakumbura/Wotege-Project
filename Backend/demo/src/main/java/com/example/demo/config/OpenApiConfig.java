package com.example.demo.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
	info = @Info(
		title = "WOTEGE Hotel & Restaurant API",
		version = "1.0.0",
		description = "Backend API for hotel, restaurant, inventory, and administration workflows"
	)
)
public class OpenApiConfig {
}
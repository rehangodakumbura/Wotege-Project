package com.example.demo.auth;

public record UpdatePreferencesRequest(
	String theme,
	String language,
	Boolean notificationsEnabled,
	Boolean emailNotifications
) {
}

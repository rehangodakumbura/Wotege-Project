package com.wotege.controller;

import com.wotege.dto.ApiResponse;
import com.wotege.dto.SettingsRequestDTO;
import com.wotege.dto.SettingsResponseDTO;
import com.wotege.enums.ThemeType;
import com.wotege.service.SettingsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/settings")
@RequiredArgsConstructor
public class SettingsController {

    private final SettingsService settingsService;

    @GetMapping
    public ResponseEntity<ApiResponse<SettingsResponseDTO>> getSettings() {
        SettingsResponseDTO settings = settingsService.getSettings();
        return ResponseEntity.ok(ApiResponse.success(settings));
    }

    @PutMapping("/general")
    public ResponseEntity<ApiResponse<SettingsResponseDTO>> updateGeneral(
            @Valid @RequestBody SettingsRequestDTO request) {
        SettingsResponseDTO settings = settingsService.updateGeneralSettings(request);
        return ResponseEntity.ok(ApiResponse.success("General settings updated", settings));
    }

    @PutMapping("/appearance")
    public ResponseEntity<ApiResponse<SettingsResponseDTO>> updateAppearance(
            @RequestBody Map<String, String> body) {
        String themeStr = body.get("theme");
        if (themeStr == null || themeStr.isBlank()) {
            throw new IllegalArgumentException("Theme is required");
        }
        ThemeType theme = ThemeType.valueOf(themeStr.toUpperCase());
        SettingsResponseDTO settings = settingsService.updateAppearance(theme);
        return ResponseEntity.ok(ApiResponse.success("Appearance settings updated", settings));
    }

    @PutMapping("/notifications")
    public ResponseEntity<ApiResponse<SettingsResponseDTO>> updateNotifications(
            @RequestBody SettingsRequestDTO request) {
        SettingsResponseDTO settings = settingsService.updateNotifications(request);
        return ResponseEntity.ok(ApiResponse.success("Notification settings updated", settings));
    }

    @PutMapping("/security")
    public ResponseEntity<ApiResponse<SettingsResponseDTO>> updateSecurity(
            @RequestBody SettingsRequestDTO request) {
        SettingsResponseDTO settings = settingsService.updateSecurity(request);
        return ResponseEntity.ok(ApiResponse.success("Security settings updated", settings));
    }

    @PutMapping("/localization")
    public ResponseEntity<ApiResponse<SettingsResponseDTO>> updateLocalization(
            @Valid @RequestBody SettingsRequestDTO request) {
        SettingsResponseDTO settings = settingsService.updateLocalization(request);
        return ResponseEntity.ok(ApiResponse.success("Localization settings updated", settings));
    }
}

package com.wotege.service;

import com.wotege.dto.SettingsRequestDTO;
import com.wotege.dto.SettingsResponseDTO;
import com.wotege.enums.ThemeType;

public interface SettingsService {

    SettingsResponseDTO getSettings();

    SettingsResponseDTO updateGeneralSettings(SettingsRequestDTO request);

    SettingsResponseDTO updateAppearance(ThemeType theme);

    SettingsResponseDTO updateNotifications(SettingsRequestDTO request);

    SettingsResponseDTO updateSecurity(SettingsRequestDTO request);

    SettingsResponseDTO updateLocalization(SettingsRequestDTO request);
}

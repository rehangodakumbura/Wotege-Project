package com.wotege.service.impl;

import com.wotege.dto.SettingsRequestDTO;
import com.wotege.dto.SettingsResponseDTO;
import com.wotege.entity.SystemSettingsEntity;
import com.wotege.enums.ThemeType;
import com.wotege.exception.ResourceNotFoundException;
import com.wotege.repository.SettingsRepository;
import com.wotege.service.SettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SettingsServiceImpl implements SettingsService {

    private final SettingsRepository settingsRepository;

    private SystemSettingsEntity getOrThrow() {
        return settingsRepository.findTopByOrderByIdAsc()
                .orElseThrow(() -> new ResourceNotFoundException("Settings not found"));
    }

    @Override
    public SettingsResponseDTO getSettings() {
        SystemSettingsEntity entity = getOrThrow();
        return toResponseDTO(entity);
    }

    @Override
    @Transactional
    public SettingsResponseDTO updateGeneralSettings(SettingsRequestDTO request) {
        SystemSettingsEntity entity = getOrThrow();
        entity.setHotelName(request.getHotelName());
        entity.setBranchName(request.getBranchName());
        entity.setContactEmail(request.getContactEmail());
        return toResponseDTO(settingsRepository.save(entity));
    }

    @Override
    @Transactional
    public SettingsResponseDTO updateAppearance(ThemeType theme) {
        SystemSettingsEntity entity = getOrThrow();
        entity.setTheme(theme);
        return toResponseDTO(settingsRepository.save(entity));
    }

    @Override
    @Transactional
    public SettingsResponseDTO updateNotifications(SettingsRequestDTO request) {
        SystemSettingsEntity entity = getOrThrow();
        if (request.getEmailNotifications() != null) {
            entity.setEmailNotifications(request.getEmailNotifications());
        }
        if (request.getPushNotifications() != null) {
            entity.setPushNotifications(request.getPushNotifications());
        }
        if (request.getBookingAlerts() != null) {
            entity.setBookingAlerts(request.getBookingAlerts());
        }
        if (request.getPromotionalMessages() != null) {
            entity.setPromotionalMessages(request.getPromotionalMessages());
        }
        return toResponseDTO(settingsRepository.save(entity));
    }

    @Override
    @Transactional
    public SettingsResponseDTO updateSecurity(SettingsRequestDTO request) {
        SystemSettingsEntity entity = getOrThrow();
        if (request.getTwoFactorEnabled() != null) {
            entity.setTwoFactorEnabled(request.getTwoFactorEnabled());
        }
        if (request.getSessionTimeoutMinutes() != null) {
            entity.setSessionTimeoutMinutes(request.getSessionTimeoutMinutes());
        }
        if (request.getPasswordExpiryDays() != null) {
            entity.setPasswordExpiryDays(request.getPasswordExpiryDays());
        }
        return toResponseDTO(settingsRepository.save(entity));
    }

    @Override
    @Transactional
    public SettingsResponseDTO updateLocalization(SettingsRequestDTO request) {
        SystemSettingsEntity entity = getOrThrow();
        entity.setTimezone(request.getTimezone());
        entity.setRegion(request.getRegion());
        entity.setLanguage(request.getLanguage());
        return toResponseDTO(settingsRepository.save(entity));
    }

    private SettingsResponseDTO toResponseDTO(SystemSettingsEntity entity) {
        return SettingsResponseDTO.builder()
                .id(entity.getId())
                .hotelName(entity.getHotelName())
                .branchName(entity.getBranchName())
                .contactEmail(entity.getContactEmail())
                .theme(entity.getTheme())
                .emailNotifications(entity.getEmailNotifications())
                .pushNotifications(entity.getPushNotifications())
                .bookingAlerts(entity.getBookingAlerts())
                .promotionalMessages(entity.getPromotionalMessages())
                .twoFactorEnabled(entity.getTwoFactorEnabled())
                .sessionTimeoutMinutes(entity.getSessionTimeoutMinutes())
                .passwordExpiryDays(entity.getPasswordExpiryDays())
                .timezone(entity.getTimezone())
                .region(entity.getRegion())
                .language(entity.getLanguage())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}

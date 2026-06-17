package com.wotege.config;

import com.wotege.entity.SystemSettingsEntity;
import com.wotege.enums.ThemeType;
import com.wotege.repository.SettingsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DefaultSettingsInitializer implements CommandLineRunner {

    private final SettingsRepository settingsRepository;

    @Override
    public void run(String... args) {
        if (settingsRepository.count() > 0) {
            log.info("Settings already exist, skipping initialization.");
            return;
        }

        log.info("Initializing default system settings...");

        SystemSettingsEntity defaultSettings = SystemSettingsEntity.builder()
                .hotelName("WOTEGE Hotel & Restaurant")
                .branchName("Dubai Marina")
                .contactEmail("admin@wotege.com")
                .theme(ThemeType.DARK_LUXURY)
                .emailNotifications(true)
                .pushNotifications(true)
                .bookingAlerts(true)
                .promotionalMessages(true)
                .twoFactorEnabled(false)
                .sessionTimeoutMinutes(30)
                .passwordExpiryDays(90)
                .timezone("GMT+4")
                .region("Dubai Marina")
                .language("English")
                .build();

        settingsRepository.save(defaultSettings);
        log.info("Default settings initialized successfully.");
    }
}

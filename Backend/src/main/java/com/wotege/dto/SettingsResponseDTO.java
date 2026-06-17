package com.wotege.dto;

import com.wotege.enums.ThemeType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SettingsResponseDTO {

    private Long id;
    private String hotelName;
    private String branchName;
    private String contactEmail;
    private ThemeType theme;
    private Boolean emailNotifications;
    private Boolean pushNotifications;
    private Boolean bookingAlerts;
    private Boolean promotionalMessages;
    private Boolean twoFactorEnabled;
    private Integer sessionTimeoutMinutes;
    private Integer passwordExpiryDays;
    private String timezone;
    private String region;
    private String language;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

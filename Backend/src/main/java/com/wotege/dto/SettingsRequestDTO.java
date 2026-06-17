package com.wotege.dto;

import com.wotege.enums.ThemeType;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SettingsRequestDTO {

    @NotBlank(message = "Hotel name cannot be empty")
    private String hotelName;

    @NotBlank(message = "Branch name cannot be empty")
    private String branchName;

    @Email(message = "Contact email must be a valid email")
    private String contactEmail;

    private ThemeType theme;

    private Boolean emailNotifications;

    private Boolean pushNotifications;

    private Boolean bookingAlerts;

    private Boolean promotionalMessages;

    private Boolean twoFactorEnabled;

    @Min(value = 1, message = "Session timeout must be greater than 0")
    private Integer sessionTimeoutMinutes;

    @Min(value = 1, message = "Password expiry days must be greater than 0")
    private Integer passwordExpiryDays;

    @NotBlank(message = "Timezone cannot be empty")
    private String timezone;

    private String region;

    @NotBlank(message = "Language cannot be empty")
    private String language;
}

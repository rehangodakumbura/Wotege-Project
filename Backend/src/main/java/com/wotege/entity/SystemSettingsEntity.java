package com.wotege.entity;

import com.wotege.enums.ThemeType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "system_settings")
public class SystemSettingsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "hotel_name")
    private String hotelName;

    @Column(name = "branch_name")
    private String branchName;

    @Column(name = "contact_email")
    private String contactEmail;

    @Enumerated(EnumType.STRING)
    @Column(name = "theme")
    private ThemeType theme;

    @Column(name = "email_notifications")
    private Boolean emailNotifications;

    @Column(name = "push_notifications")
    private Boolean pushNotifications;

    @Column(name = "booking_alerts")
    private Boolean bookingAlerts;

    @Column(name = "promotional_messages")
    private Boolean promotionalMessages;

    @Column(name = "two_factor_enabled")
    private Boolean twoFactorEnabled;

    @Column(name = "session_timeout_minutes")
    private Integer sessionTimeoutMinutes;

    @Column(name = "password_expiry_days")
    private Integer passwordExpiryDays;

    @Column(name = "timezone")
    private String timezone;

    @Column(name = "region")
    private String region;

    @Column(name = "language")
    private String language;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

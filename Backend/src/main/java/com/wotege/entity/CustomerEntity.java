package com.wotege.entity;

import com.wotege.enums.CustomerStatus;
import com.wotege.enums.LoyaltyTier;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "loyalty_customers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_code", unique = true, nullable = false, length = 50)
    private String customerCode;

    @Column(name = "full_name", nullable = false, length = 255)
    private String fullName;

    @Column(unique = true, length = 255)
    private String email;

    @Column(name = "phone_number", length = 50)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "loyalty_tier", length = 50)
    private LoyaltyTier loyaltyTier;

    @Column(name = "total_spent", columnDefinition = "DECIMAL(12,2)")
    private Double totalSpent;

    @Column(name = "visit_count")
    private Integer visitCount;

    @Column(name = "is_vip")
    private Boolean isVip;

    @Column(name = "last_visit_date")
    private LocalDate lastVisitDate;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private CustomerStatus status;

    @Column(updatable = false, name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (isVip == null) isVip = false;
        if (visitCount == null) visitCount = 0;
        if (totalSpent == null) totalSpent = 0.0;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

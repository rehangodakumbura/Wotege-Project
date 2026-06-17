package com.wotege.dto;

import com.wotege.enums.CustomerStatus;
import com.wotege.enums.LoyaltyTier;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerResponseDTO {

    private Long id;
    private String customerCode;
    private String fullName;
    private String email;
    private String phoneNumber;
    private LoyaltyTier loyaltyTier;
    private Double totalSpent;
    private Integer visitCount;
    private Boolean isVip;
    private LocalDate lastVisitDate;
    private CustomerStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

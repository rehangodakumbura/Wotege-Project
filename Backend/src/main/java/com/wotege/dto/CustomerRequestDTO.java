package com.wotege.dto;

import com.wotege.enums.CustomerStatus;
import com.wotege.enums.LoyaltyTier;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRequestDTO {

    @NotBlank(message = "Customer code cannot be empty")
    private String customerCode;

    @NotBlank(message = "Full name cannot be empty")
    private String fullName;

    @Email(message = "Email must be valid")
    private String email;

    @NotBlank(message = "Phone number cannot be empty")
    private String phoneNumber;

    @NotNull(message = "Loyalty tier cannot be null")
    private LoyaltyTier loyaltyTier;

    @Min(value = 0, message = "Total spent cannot be negative")
    private Double totalSpent;

    @Min(value = 0, message = "Visit count cannot be negative")
    private Integer visitCount;

    private Boolean isVip;

    private LocalDate lastVisitDate;

    @NotNull(message = "Status cannot be null")
    private CustomerStatus status;
}

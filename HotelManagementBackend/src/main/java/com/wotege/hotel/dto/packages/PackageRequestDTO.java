package com.wotege.hotel.dto.packages;

import com.wotege.hotel.enums.PackageCategory;
import com.wotege.hotel.enums.PackageStatus;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PackageRequestDTO {

    @NotEmpty(message = "Package name cannot be empty")
    private String packageName;

    @NotNull(message = "Category is required")
    private PackageCategory category;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than 0")
    private Double price;

    private String imageUrl;

    @NotEmpty(message = "Included items cannot be empty")
    private List<String> includedItems;

    @NotNull(message = "Valid from date is required")
    private LocalDate validFrom;

    @NotNull(message = "Valid to date is required")
    private LocalDate validTo;

    @NotNull(message = "Status is required")
    private PackageStatus status;

    public boolean isValidDateRange() {
        return validTo == null || validFrom == null || !validTo.isBefore(validFrom);
    }
}

package com.wotege.dto;

import com.wotege.enums.InventoryCategory;
import com.wotege.enums.UnitOfMeasurement;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryRequestDTO {

    @NotBlank(message = "Item name is required")
    private String itemName;

    @NotNull(message = "Category is required")
    private InventoryCategory category;

    @NotNull(message = "Unit of measurement is required")
    private UnitOfMeasurement unitOfMeasurement;

    @Min(value = 0, message = "Current stock must be >= 0")
    private Double currentStock;

    @NotNull(message = "Minimum threshold is required")
    @Min(value = 1, message = "Minimum threshold must be > 0")
    private Double minimumThreshold;

    private String supplier;

    @Min(value = 0, message = "Unit cost must be >= 0")
    private Double unitCost;
}

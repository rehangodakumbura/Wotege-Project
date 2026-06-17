package com.wotege.dto;

import com.wotege.enums.InventoryCategory;
import com.wotege.enums.InventoryStatus;
import com.wotege.enums.UnitOfMeasurement;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryResponseDTO {

    private Long id;
    private String itemCode;
    private String itemName;
    private InventoryCategory category;
    private UnitOfMeasurement unitOfMeasurement;
    private Double currentStock;
    private Double minimumThreshold;
    private String supplier;
    private Double unitCost;
    private Double inventoryValue;
    private InventoryStatus status;
    private LocalDateTime lastRestockDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

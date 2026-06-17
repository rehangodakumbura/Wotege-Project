package com.wotege.entity;

import com.wotege.enums.InventoryCategory;
import com.wotege.enums.InventoryStatus;
import com.wotege.enums.UnitOfMeasurement;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "inventory_items")
public class InventoryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "item_code", unique = true, length = 50)
    private String itemCode;

    @Column(name = "item_name", nullable = false)
    private String itemName;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private InventoryCategory category;

    @Enumerated(EnumType.STRING)
    @Column(name = "unit_of_measurement")
    private UnitOfMeasurement unitOfMeasurement;

    @Column(name = "current_stock")
    private Double currentStock;

    @Column(name = "minimum_threshold")
    private Double minimumThreshold;

    @Column(name = "supplier")
    private String supplier;

    @Column(name = "unit_cost")
    private Double unitCost;

    @Column(name = "inventory_value")
    private Double inventoryValue;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private InventoryStatus status;

    @Column(name = "last_restock_date")
    private LocalDateTime lastRestockDate;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        calculateStatus();
        calculateInventoryValue();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        calculateStatus();
        calculateInventoryValue();
    }

    public void calculateStatus() {
        if (currentStock == null || minimumThreshold == null || minimumThreshold <= 0) {
            this.status = InventoryStatus.IN_STOCK;
            return;
        }
        if (currentStock <= minimumThreshold / 2) {
            this.status = InventoryStatus.CRITICAL;
        } else if (currentStock <= minimumThreshold) {
            this.status = InventoryStatus.LOW_STOCK;
        } else {
            this.status = InventoryStatus.IN_STOCK;
        }
    }

    public void calculateInventoryValue() {
        if (currentStock != null && unitCost != null) {
            this.inventoryValue = currentStock * unitCost;
        } else {
            this.inventoryValue = 0.0;
        }
    }
}

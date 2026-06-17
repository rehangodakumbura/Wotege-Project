package com.wotege.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardResponseDTO {

    private long totalItems;
    private long lowStockAlerts;
    private double inventoryValue;
    private long pendingOrders;
}

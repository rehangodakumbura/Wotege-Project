package com.wotege.hotel.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {

    private long totalRooms;
    private long availableRooms;
    private long occupiedRooms;
    private long reservedRooms;
    private long cleaningRooms;
    private long maintenanceRooms;
    private long todayCheckIns;
    private long todayCheckOuts;
}

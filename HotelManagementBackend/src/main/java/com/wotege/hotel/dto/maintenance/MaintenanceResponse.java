package com.wotege.hotel.dto.maintenance;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceResponse {

    private Long id;
    private Long roomId;
    private String roomNumber;
    private String issueDescription;
    private LocalDateTime reportedDate;
    private LocalDateTime completedDate;
    private String status;
    private LocalDateTime createdAt;
}

package com.wotege.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardStatisticsDTO {

    private long totalCustomers;
    private long activeVipCustomers;
    private double averageLtv;
}

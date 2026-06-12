package com.wotege.hotel.service;

import com.wotege.hotel.dto.maintenance.MaintenanceRequest;
import com.wotege.hotel.dto.maintenance.MaintenanceResponse;

import java.util.List;

public interface MaintenanceService {

    List<MaintenanceResponse> getAllMaintenances();

    MaintenanceResponse createMaintenance(MaintenanceRequest request);

    MaintenanceResponse updateMaintenance(Long id, String status);
}

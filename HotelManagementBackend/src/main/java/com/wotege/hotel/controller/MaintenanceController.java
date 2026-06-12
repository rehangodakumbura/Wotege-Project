package com.wotege.hotel.controller;

import com.wotege.hotel.dto.ApiResponse;
import com.wotege.hotel.dto.maintenance.MaintenanceRequest;
import com.wotege.hotel.dto.maintenance.MaintenanceResponse;
import com.wotege.hotel.service.MaintenanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/maintenance")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MaintenanceController {

    private final MaintenanceService maintenanceService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<MaintenanceResponse>>> getAllMaintenances() {
        return ResponseEntity.ok(ApiResponse.success(maintenanceService.getAllMaintenances()));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<MaintenanceResponse>> createMaintenance(@Valid @RequestBody MaintenanceRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Maintenance task created", maintenanceService.createMaintenance(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MaintenanceResponse>> updateMaintenance(
            @PathVariable Long id,
            @RequestParam String status) {
        return ResponseEntity.ok(ApiResponse.success("Maintenance updated", maintenanceService.updateMaintenance(id, status)));
    }
}

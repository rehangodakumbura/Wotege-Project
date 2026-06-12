package com.wotege.hotel.service;

import com.wotege.hotel.dto.maintenance.MaintenanceRequest;
import com.wotege.hotel.dto.maintenance.MaintenanceResponse;
import com.wotege.hotel.entity.Maintenance;
import com.wotege.hotel.entity.Room;
import com.wotege.hotel.exception.BadRequestException;
import com.wotege.hotel.exception.ResourceNotFoundException;
import com.wotege.hotel.repository.MaintenanceRepository;
import com.wotege.hotel.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MaintenanceServiceImpl implements MaintenanceService {

    private final MaintenanceRepository maintenanceRepository;
    private final RoomRepository roomRepository;

    @Override
    @Transactional(readOnly = true)
    public List<MaintenanceResponse> getAllMaintenances() {
        return maintenanceRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public MaintenanceResponse createMaintenance(MaintenanceRequest request) {
        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + request.getRoomId()));

        Maintenance maintenance = Maintenance.builder()
                .room(room)
                .issueDescription(request.getIssueDescription())
                .reportedDate(LocalDateTime.now())
                .status(Maintenance.MaintenanceStatus.OPEN)
                .build();

        maintenance = maintenanceRepository.save(maintenance);

        room.setStatus(Room.RoomStatus.MAINTENANCE);
        roomRepository.save(room);

        return toResponse(maintenance);
    }

    @Override
    @Transactional
    public MaintenanceResponse updateMaintenance(Long id, String status) {
        Maintenance maintenance = maintenanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Maintenance not found with id: " + id));

        Maintenance.MaintenanceStatus newStatus;
        try {
            newStatus = Maintenance.MaintenanceStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid maintenance status: " + status);
        }

        maintenance.setStatus(newStatus);

        if (newStatus == Maintenance.MaintenanceStatus.COMPLETED) {
            maintenance.setCompletedDate(LocalDateTime.now());
            Room room = maintenance.getRoom();
            room.setStatus(Room.RoomStatus.AVAILABLE);
            roomRepository.save(room);
        }

        maintenance = maintenanceRepository.save(maintenance);
        return toResponse(maintenance);
    }

    private MaintenanceResponse toResponse(Maintenance maintenance) {
        MaintenanceResponse response = new MaintenanceResponse();
        response.setId(maintenance.getId());
        response.setRoomId(maintenance.getRoom().getId());
        response.setRoomNumber(maintenance.getRoom().getRoomNumber());
        response.setIssueDescription(maintenance.getIssueDescription());
        response.setReportedDate(maintenance.getReportedDate());
        response.setCompletedDate(maintenance.getCompletedDate());
        response.setStatus(maintenance.getStatus().name());
        response.setCreatedAt(maintenance.getCreatedAt());
        return response;
    }
}

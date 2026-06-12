package com.wotege.hotel.service;

import com.wotege.hotel.dto.dashboard.DashboardResponse;
import com.wotege.hotel.entity.Room;
import com.wotege.hotel.repository.BookingRepository;
import com.wotege.hotel.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;

    @Override
    @Transactional(readOnly = true)
    public DashboardResponse getSummary() {
        long totalRooms = roomRepository.count();
        long availableRooms = roomRepository.countByStatus(Room.RoomStatus.AVAILABLE);
        long occupiedRooms = roomRepository.countByStatus(Room.RoomStatus.OCCUPIED);
        long reservedRooms = roomRepository.countByStatus(Room.RoomStatus.RESERVED);
        long cleaningRooms = roomRepository.countByStatus(Room.RoomStatus.CLEANING);
        long maintenanceRooms = roomRepository.countByStatus(Room.RoomStatus.MAINTENANCE);
        long todayCheckIns = bookingRepository.countByCheckInDate(LocalDate.now());
        long todayCheckOuts = bookingRepository.countByCheckOutDate(LocalDate.now());

        return DashboardResponse.builder()
                .totalRooms(totalRooms)
                .availableRooms(availableRooms)
                .occupiedRooms(occupiedRooms)
                .reservedRooms(reservedRooms)
                .cleaningRooms(cleaningRooms)
                .maintenanceRooms(maintenanceRooms)
                .todayCheckIns(todayCheckIns)
                .todayCheckOuts(todayCheckOuts)
                .build();
    }
}

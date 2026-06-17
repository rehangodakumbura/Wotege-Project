package com.example.demo.room;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.example.demo.config.NotificationService;
import com.example.demo.reservation.Reservation;
import com.example.demo.reservation.ReservationRepository;
import com.example.demo.reservation.ReservationStatus;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class RoomService {

    private final RoomRepository roomRepository;
    private final RoomTypeRepository roomTypeRepository;
    private final RoomStatusHistoryRepository statusHistoryRepository;
    private final NotificationService notificationService;
    private final ReservationRepository reservationRepository;

    public RoomService(
            RoomRepository roomRepository,
            RoomTypeRepository roomTypeRepository,
            RoomStatusHistoryRepository statusHistoryRepository,
            NotificationService notificationService,
            ReservationRepository reservationRepository) {
        this.roomRepository = roomRepository;
        this.roomTypeRepository = roomTypeRepository;
        this.statusHistoryRepository = statusHistoryRepository;
        this.notificationService = notificationService;
        this.reservationRepository = reservationRepository;
    }

    @Transactional(readOnly = true)
    public List<Room> findAll() {
        return roomRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Room findById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Room not found"));
    }

    public Room create(Room payload, Long roomTypeId) {
        Room room = new Room();
        apply(payload, room);
        if (roomTypeId != null) {
            room.setRoomType(roomTypeRepository.findById(roomTypeId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Room type not found")));
        }
        return roomRepository.save(room);
    }

    public Room update(Long id, Room payload, Long roomTypeId) {
        Room existing = roomRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Room not found"));
        apply(payload, existing);
        if (roomTypeId != null) {
            existing.setRoomType(roomTypeRepository.findById(roomTypeId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Room type not found")));
        }
        return roomRepository.save(existing);
    }

    public Room updateStatus(Long id, RoomStatus status, String reason, String changedBy) {
        Room existing = roomRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Room not found"));

        RoomStatus fromStatus = existing.getStatus();
        existing.setStatus(status);

        if (status == RoomStatus.AVAILABLE) {
            existing.setGuestName(null);
        }

        recordStatusChange(existing, fromStatus, status, reason, changedBy);

        return roomRepository.save(existing);
    }

    public Room checkIn(Long id, String guestName, Long reservationId, String changedBy) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Room not found"));

        if (room.getStatus() != RoomStatus.RESERVED && room.getStatus() != RoomStatus.AVAILABLE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Room must be RESERVED or AVAILABLE for check-in. Current status: " + room.getStatus());
        }

        RoomStatus fromStatus = room.getStatus();
        room.setStatus(RoomStatus.OCCUPIED);
        room.setGuestName(guestName);

        recordStatusChange(room, fromStatus, RoomStatus.OCCUPIED, "Guest check-in", changedBy);

        return roomRepository.save(room);
    }

    public Room checkOut(Long id, String changedBy) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Room not found"));

        if (room.getStatus() != RoomStatus.OCCUPIED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Room must be OCCUPIED for check-out. Current status: " + room.getStatus());
        }

        RoomStatus fromStatus = room.getStatus();
        room.setStatus(RoomStatus.CLEANING);
        room.setGuestName(null);

        recordStatusChange(room, fromStatus, RoomStatus.CLEANING, "Guest check-out", changedBy);

        return roomRepository.save(room);
    }

    public Room markCleaned(Long id, String changedBy) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Room not found"));

        if (room.getStatus() != RoomStatus.CLEANING) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Room must be CLEANING to mark as cleaned. Current status: " + room.getStatus());
        }

        RoomStatus fromStatus = room.getStatus();
        room.setStatus(RoomStatus.AVAILABLE);

        recordStatusChange(room, fromStatus, RoomStatus.AVAILABLE, "Cleaning completed", changedBy);

        return roomRepository.save(room);
    }

    public void delete(Long id) {
        Room existing = roomRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Room not found"));
        roomRepository.delete(existing);
    }

    @Transactional(readOnly = true)
    public List<Room> findAvailable(LocalDate checkIn, LocalDate checkOut, String type) {
        List<Room> availableRooms = roomRepository.findByStatus(RoomStatus.AVAILABLE);

        if (type != null && !type.isBlank()) {
            availableRooms = availableRooms.stream()
                    .filter(r -> r.getRoomType() != null &&
                            (r.getRoomType().getName().toLowerCase().contains(type.toLowerCase()) ||
                             r.getRoomType().getCode().toLowerCase().contains(type.toLowerCase())))
                    .collect(Collectors.toList());
        }

        if (checkIn != null && checkOut != null) {
            availableRooms = availableRooms.stream()
                    .filter(room -> {
                        List<Reservation> conflicts = reservationRepository
                                .findConflicting(room.getId(), checkIn, checkOut);
                        return conflicts.isEmpty();
                    })
                    .collect(Collectors.toList());
        }

        return availableRooms;
    }

    @Transactional(readOnly = true)
    public List<RoomStatusHistory> getStatusHistory(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Room not found"));
        return statusHistoryRepository.findByRoomIdOrderByChangedAtDesc(roomId);
    }

    private void recordStatusChange(Room room, RoomStatus fromStatus, RoomStatus toStatus, String reason, String changedBy) {
        RoomStatusHistory history = new RoomStatusHistory();
        history.setRoom(room);
        history.setFromStatus(fromStatus);
        history.setToStatus(toStatus);
        history.setReason(reason);
        history.setChangedBy(changedBy);
        statusHistoryRepository.save(history);

        Map<String, Object> notification = new LinkedHashMap<>();
        notification.put("type", "ROOM_STATUS_CHANGE");
        notification.put("roomId", room.getId());
        notification.put("roomNumber", room.getRoomNumber());
        notification.put("fromStatus", fromStatus);
        notification.put("toStatus", toStatus);
        notification.put("guestName", room.getGuestName());
        notification.put("reason", reason);
        notification.put("changedBy", changedBy);
        notificationService.broadcastRoomStatusChange(notification);
    }

    private void apply(Room payload, Room target) {
        target.setRoomNumber(payload.getRoomNumber());
        target.setFloor(payload.getFloor());
        target.setPrice(payload.getPrice());
        target.setBeds(payload.getBeds());
        target.setStatus(payload.getStatus() == null ? RoomStatus.AVAILABLE : payload.getStatus());
        target.setGuestName(payload.getGuestName());
        target.setAmenities(payload.getAmenities());
    }
}

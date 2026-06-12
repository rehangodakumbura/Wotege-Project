package com.wotege.hotel.service;

import com.wotege.hotel.dto.cleaning.CleaningRequest;
import com.wotege.hotel.dto.cleaning.CleaningResponse;
import com.wotege.hotel.entity.Cleaning;
import com.wotege.hotel.entity.Room;
import com.wotege.hotel.exception.BadRequestException;
import com.wotege.hotel.exception.ResourceNotFoundException;
import com.wotege.hotel.repository.CleaningRepository;
import com.wotege.hotel.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CleaningServiceImpl implements CleaningService {

    private final CleaningRepository cleaningRepository;
    private final RoomRepository roomRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CleaningResponse> getAllCleanings() {
        return cleaningRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CleaningResponse createCleaning(CleaningRequest request) {
        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + request.getRoomId()));

        Cleaning cleaning = Cleaning.builder()
                .room(room)
                .assignedTo(request.getAssignedTo())
                .status(Cleaning.CleaningStatus.PENDING)
                .build();

        cleaning = cleaningRepository.save(cleaning);
        return toResponse(cleaning);
    }

    @Override
    @Transactional
    public CleaningResponse updateCleaning(Long id, String status) {
        Cleaning cleaning = cleaningRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cleaning not found with id: " + id));

        Cleaning.CleaningStatus newStatus;
        try {
            newStatus = Cleaning.CleaningStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid cleaning status: " + status);
        }

        cleaning.setStatus(newStatus);

        if (newStatus == Cleaning.CleaningStatus.IN_PROGRESS) {
            cleaning.setStartTime(LocalDateTime.now());
        } else if (newStatus == Cleaning.CleaningStatus.COMPLETED) {
            cleaning.setEndTime(LocalDateTime.now());
            Room room = cleaning.getRoom();
            room.setStatus(Room.RoomStatus.AVAILABLE);
            roomRepository.save(room);
        }

        cleaning = cleaningRepository.save(cleaning);
        return toResponse(cleaning);
    }

    private CleaningResponse toResponse(Cleaning cleaning) {
        CleaningResponse response = new CleaningResponse();
        response.setId(cleaning.getId());
        response.setRoomId(cleaning.getRoom().getId());
        response.setRoomNumber(cleaning.getRoom().getRoomNumber());
        response.setAssignedTo(cleaning.getAssignedTo());
        response.setStartTime(cleaning.getStartTime());
        response.setEndTime(cleaning.getEndTime());
        response.setStatus(cleaning.getStatus().name());
        response.setCreatedAt(cleaning.getCreatedAt());
        return response;
    }
}

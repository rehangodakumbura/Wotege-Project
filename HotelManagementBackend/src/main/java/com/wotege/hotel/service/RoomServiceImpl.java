package com.wotege.hotel.service;

import com.wotege.hotel.dto.room.RoomRequest;
import com.wotege.hotel.dto.room.RoomResponse;
import com.wotege.hotel.entity.Room;
import com.wotege.hotel.exception.BadRequestException;
import com.wotege.hotel.exception.ResourceNotFoundException;
import com.wotege.hotel.mapper.RoomMapper;
import com.wotege.hotel.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;

    @Override
    @Transactional(readOnly = true)
    public List<RoomResponse> getAllRooms() {
        return roomRepository.findAll().stream()
                .map(roomMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public RoomResponse getRoomById(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + id));
        return roomMapper.toResponse(room);
    }

    @Override
    @Transactional
    public RoomResponse createRoom(RoomRequest request) {
        if (roomRepository.existsByRoomNumber(request.getRoomNumber())) {
            throw new BadRequestException("Room number already exists: " + request.getRoomNumber());
        }
        Room room = roomMapper.toEntity(request);
        room.setStatus(Room.RoomStatus.AVAILABLE);
        room = roomRepository.save(room);
        return roomMapper.toResponse(room);
    }

    @Override
    @Transactional
    public RoomResponse updateRoom(Long id, RoomRequest request) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + id));

        Room existing = roomRepository.findByRoomNumber(request.getRoomNumber());
        if (existing != null && !existing.getId().equals(id)) {
            throw new BadRequestException("Room number already exists: " + request.getRoomNumber());
        }

        room.setRoomNumber(request.getRoomNumber());
        room.setRoomType(request.getRoomType());
        room.setDescription(request.getDescription());
        room.setPricePerNight(request.getPricePerNight());
        room.setBedCount(request.getBedCount());
        room.setHasSeaView(request.getHasSeaView());
        room.setHasBalcony(request.getHasBalcony());

        room = roomRepository.save(room);
        return roomMapper.toResponse(room);
    }

    @Override
    @Transactional
    public void deleteRoom(Long id) {
        if (!roomRepository.existsById(id)) {
            throw new ResourceNotFoundException("Room not found with id: " + id);
        }
        roomRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomResponse> getRoomsByStatus(String status) {
        Room.RoomStatus roomStatus;
        try {
            roomStatus = Room.RoomStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid room status: " + status);
        }
        return roomRepository.findByStatus(roomStatus).stream()
                .map(roomMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomResponse> searchRooms(String keyword) {
        return roomRepository.search(keyword).stream()
                .map(roomMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Room updateRoomStatus(Long id, Room.RoomStatus roomStatus) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + id));
        room.setStatus(roomStatus);
        return roomRepository.save(room);
    }
}

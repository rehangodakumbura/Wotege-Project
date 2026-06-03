package com.example.demo.room;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class RoomService {

	private final RoomRepository roomRepository;
	private final RoomTypeRepository roomTypeRepository;

	public RoomService(RoomRepository roomRepository, RoomTypeRepository roomTypeRepository) {
		this.roomRepository = roomRepository;
		this.roomTypeRepository = roomTypeRepository;
	}

	@Transactional(readOnly = true)
	public List<Room> findAll() {
		return roomRepository.findAll();
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

	public Room updateStatus(Long id, RoomStatus status) {
		Room existing = roomRepository.findById(id)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Room not found"));
		existing.setStatus(status);
		if (status == RoomStatus.AVAILABLE) {
			existing.setGuestName(null);
		}
		return roomRepository.save(existing);
	}

	public void delete(Long id) {
		Room existing = roomRepository.findById(id)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Room not found"));
		roomRepository.delete(existing);
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
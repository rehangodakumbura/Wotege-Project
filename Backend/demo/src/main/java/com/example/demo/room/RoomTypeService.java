package com.example.demo.room;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class RoomTypeService {

	private final RoomTypeRepository roomTypeRepository;

	public RoomTypeService(RoomTypeRepository roomTypeRepository) {
		this.roomTypeRepository = roomTypeRepository;
	}

	@Transactional(readOnly = true)
	public List<RoomType> findAll() {
		return roomTypeRepository.findAll();
	}

	public RoomType create(RoomType roomType) {
		roomType.setId(null);
		return roomTypeRepository.save(roomType);
	}

	public RoomType update(Long id, RoomType payload) {
		RoomType existing = roomTypeRepository.findById(id)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Room type not found"));
		existing.setCode(payload.getCode());
		existing.setName(payload.getName());
		existing.setBaseRate(payload.getBaseRate());
		existing.setBeds(payload.getBeds());
		existing.setCapacity(payload.getCapacity());
		existing.setAmenities(payload.getAmenities());
		return roomTypeRepository.save(existing);
	}

	public void delete(Long id) {
		RoomType existing = roomTypeRepository.findById(id)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Room type not found"));
		roomTypeRepository.delete(existing);
	}
}
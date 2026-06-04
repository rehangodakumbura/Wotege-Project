package com.example.demo.room;

import java.util.List;
import java.util.Optional;

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
	private final ReservationRepository reservationRepository;

	public RoomService(
		RoomRepository roomRepository,
		RoomTypeRepository roomTypeRepository,
		ReservationRepository reservationRepository
	) {
		this.roomRepository = roomRepository;
		this.roomTypeRepository = roomTypeRepository;
		this.reservationRepository = reservationRepository;
	}

	@Transactional(readOnly = true)
	public List<RoomView> findAll() {
		List<Room> rooms = roomRepository.findAll();
		return rooms.stream().map(this::toView).toList();
	}

	@Transactional(readOnly = true)
	public RoomView findViewById(Long id) {
		Room room = roomRepository.findById(id)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Room not found"));
		return toView(room);
	}

	private RoomView toView(Room room) {
		List<ReservationStatus> activeStatuses = switch (room.getStatus()) {
			case RESERVED -> List.of(ReservationStatus.PENDING, ReservationStatus.CONFIRMED);
			case OCCUPIED -> List.of(ReservationStatus.IN_HOUSE, ReservationStatus.CONFIRMED);
			case CLEANING -> List.of(ReservationStatus.COMPLETED);
			default -> List.of();
		};
		Reservation current = null;
		if (!activeStatuses.isEmpty()) {
			List<Reservation> matches = reservationRepository
				.findByRoomIdAndStatusInOrderByCheckInDateDesc(room.getId(), activeStatuses);
			Optional<Reservation> first = matches.stream().findFirst();
			if (first.isPresent()) {
				current = first.get();
			}
		}
		return new RoomView(room, current);
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

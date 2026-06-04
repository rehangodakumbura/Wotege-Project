package com.example.demo.reservation;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import com.example.demo.room.Room;
import com.example.demo.room.RoomRepository;
import com.example.demo.room.RoomStatus;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class ReservationService {

	private final ReservationRepository reservationRepository;
	private final RoomRepository roomRepository;

	public ReservationService(ReservationRepository reservationRepository, RoomRepository roomRepository) {
		this.reservationRepository = reservationRepository;
		this.roomRepository = roomRepository;
	}

	@Transactional(readOnly = true)
	public List<Reservation> findAll() {
		return reservationRepository.findAll();
	}

	public Reservation create(Reservation payload, Long roomId) {
		Reservation reservation = new Reservation();
		apply(payload, reservation);
		reservation.setBookingCode(payload.getBookingCode() == null || payload.getBookingCode().isBlank()
			? generateBookingCode() : payload.getBookingCode());

		if (roomId != null) {
			Room room = roomRepository.findById(roomId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Room not found"));
			reservation.setRoom(room);
			room.setStatus(RoomStatus.RESERVED);
			room.setGuestName(payload.getGuestName());
		}

		return reservationRepository.save(reservation);
	}

	public Reservation update(Long id, Reservation payload, Long roomId) {
		Reservation existing = reservationRepository.findById(id)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reservation not found"));
		apply(payload, existing);
		if (roomId != null) {
			Room room = roomRepository.findById(roomId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Room not found"));
			existing.setRoom(room);
		}
		return reservationRepository.save(existing);
	}

	public Reservation updateStatus(Long id, ReservationStatus status) {
		Reservation existing = reservationRepository.findById(id)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reservation not found"));
		existing.setStatus(status);
		if (existing.getRoom() != null) {
			switch (status) {
				case CONFIRMED, IN_HOUSE -> existing.getRoom().setStatus(RoomStatus.OCCUPIED);
				case COMPLETED -> existing.getRoom().setStatus(RoomStatus.CLEANING);
				case CANCELLED -> existing.getRoom().setStatus(RoomStatus.AVAILABLE);
				default -> {
				}
			}
		}
		return reservationRepository.save(existing);
	}

	public void delete(Long id) {
		Reservation existing = reservationRepository.findById(id)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reservation not found"));
		reservationRepository.delete(existing);
	}

	private void apply(Reservation payload, Reservation target) {
		target.setGuestName(payload.getGuestName());
		target.setGuestEmail(payload.getGuestEmail());
		target.setGuestPhone(payload.getGuestPhone());
		target.setCheckInDate(payload.getCheckInDate());
		target.setCheckOutDate(payload.getCheckOutDate());
		target.setCheckInTime(payload.getCheckInTime());
		target.setCheckOutTime(payload.getCheckOutTime());
		target.setStatus(payload.getStatus() == null ? ReservationStatus.PENDING : payload.getStatus());
		target.setBookingType(payload.getBookingType() == null ? BookingType.STANDARD : payload.getBookingType());
		target.setAmount(payload.getAmount());
		target.setNotes(payload.getNotes());
	}

	private String generateBookingCode() {
		String suffix = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
		String datePart = java.time.LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
		return "RES-" + datePart + "-" + suffix;
	}
}
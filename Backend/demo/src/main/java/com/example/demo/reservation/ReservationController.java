package com.example.demo.reservation;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

	private final ReservationService reservationService;

	public ReservationController(ReservationService reservationService) {
		this.reservationService = reservationService;
	}

	@GetMapping
	public List<Reservation> list() {
		return reservationService.findAll();
	}

	@PostMapping
	public ResponseEntity<Reservation> create(@RequestParam(required = false) Long roomId, @RequestBody Reservation reservation) {
		return ResponseEntity.ok(reservationService.create(reservation, roomId));
	}

	@PutMapping("/{id}")
	public ResponseEntity<Reservation> update(@PathVariable Long id, @RequestParam(required = false) Long roomId, @RequestBody Reservation reservation) {
		return ResponseEntity.ok(reservationService.update(id, reservation, roomId));
	}

	@PatchMapping("/{id}/status")
	public ResponseEntity<Reservation> updateStatus(@PathVariable Long id, @RequestParam ReservationStatus status) {
		return ResponseEntity.ok(reservationService.updateStatus(id, status));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		reservationService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
package com.example.demo.room;

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
@RequestMapping("/api/rooms")
public class RoomController {

	private final RoomService roomService;

	public RoomController(RoomService roomService) {
		this.roomService = roomService;
	}

	@GetMapping
	public List<Room> list() {
		return roomService.findAll();
	}

	@PostMapping
	public ResponseEntity<Room> create(@RequestParam(required = false) Long roomTypeId, @RequestBody Room room) {
		return ResponseEntity.ok(roomService.create(room, roomTypeId));
	}

	@PutMapping("/{id}")
	public ResponseEntity<Room> update(@PathVariable Long id, @RequestParam(required = false) Long roomTypeId, @RequestBody Room room) {
		return ResponseEntity.ok(roomService.update(id, room, roomTypeId));
	}

	@PatchMapping("/{id}/status")
	public ResponseEntity<Room> updateStatus(@PathVariable Long id, @RequestParam RoomStatus status) {
		return ResponseEntity.ok(roomService.updateStatus(id, status));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		roomService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
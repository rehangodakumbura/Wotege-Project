package com.example.demo.room;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/room-types")
public class RoomTypeController {

	private final RoomTypeService roomTypeService;

	public RoomTypeController(RoomTypeService roomTypeService) {
		this.roomTypeService = roomTypeService;
	}

	@GetMapping
	public List<RoomType> list() {
		return roomTypeService.findAll();
	}

	@PostMapping
	public ResponseEntity<RoomType> create(@RequestBody RoomType roomType) {
		return ResponseEntity.ok(roomTypeService.create(roomType));
	}

	@PutMapping("/{id}")
	public ResponseEntity<RoomType> update(@PathVariable Long id, @RequestBody RoomType roomType) {
		return ResponseEntity.ok(roomTypeService.update(id, roomType));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		roomTypeService.delete(id);
		return ResponseEntity.noContent().build();
	}
}

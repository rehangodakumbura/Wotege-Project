package com.example.demo.room;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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

    @GetMapping("/{id}")
    public ResponseEntity<Room> get(@PathVariable Long id) {
        return ResponseEntity.ok(roomService.findById(id));
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
    public ResponseEntity<Room> updateStatus(
            @PathVariable Long id,
            @RequestParam RoomStatus status,
            @RequestParam(required = false) String reason,
            Authentication authentication) {
        String changedBy = authentication != null ? authentication.getName() : "system";
        return ResponseEntity.ok(roomService.updateStatus(id, status, reason, changedBy));
    }

    @PostMapping("/{id}/check-in")
    public ResponseEntity<Room> checkIn(
            @PathVariable Long id,
            @RequestBody Map<String, Object> body,
            Authentication authentication) {
        String guestName = (String) body.get("guestName");
        Long reservationId = body.get("reservationId") != null
                ? Long.valueOf(body.get("reservationId").toString()) : null;
        String changedBy = authentication != null ? authentication.getName() : "system";
        return ResponseEntity.ok(roomService.checkIn(id, guestName, reservationId, changedBy));
    }

    @PostMapping("/{id}/check-out")
    public ResponseEntity<Room> checkOut(@PathVariable Long id, Authentication authentication) {
        String changedBy = authentication != null ? authentication.getName() : "system";
        return ResponseEntity.ok(roomService.checkOut(id, changedBy));
    }

    @PostMapping("/{id}/mark-cleaned")
    public ResponseEntity<Room> markCleaned(@PathVariable Long id, Authentication authentication) {
        String changedBy = authentication != null ? authentication.getName() : "system";
        return ResponseEntity.ok(roomService.markCleaned(id, changedBy));
    }

    @GetMapping("/available")
    public ResponseEntity<?> getAvailable(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut,
            @RequestParam(required = false) String type) {
        return ResponseEntity.ok(Map.of("success", true, "data", roomService.findAvailable(checkIn, checkOut, type)));
    }

    @GetMapping("/{id}/status-history")
    public ResponseEntity<List<RoomStatusHistory>> getStatusHistory(@PathVariable Long id) {
        return ResponseEntity.ok(roomService.getStatusHistory(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        roomService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

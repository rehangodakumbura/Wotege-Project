package com.example.demo.reservation;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;
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
    public ResponseEntity<?> list(
            @RequestParam(required = false) ReservationStatus status,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String filter) {
        if (filter != null) {
            LocalDate today = LocalDate.now();
            if ("upcoming".equalsIgnoreCase(filter)) {
                List<Reservation> all = reservationService.findAll();
                List<Reservation> upcoming = all.stream()
                        .filter(r -> r.getCheckInDate().isAfter(today.minusDays(1))
                                && r.getStatus() != ReservationStatus.CANCELLED
                                && r.getStatus() != ReservationStatus.COMPLETED)
                        .toList();
                return ResponseEntity.ok(Map.of("success", true, "data", upcoming));
            }
        }
        if (status != null || (search != null && !search.isBlank())) {
            return ResponseEntity.ok(Map.of("success", true, "data", reservationService.findByFilters(status, search)));
        }
        return ResponseEntity.ok(Map.of("success", true, "data", reservationService.findAll()));
    }

    @GetMapping("/stats")
    public ResponseEntity<?> stats() {
        return ResponseEntity.ok(Map.of("success", true, "data", reservationService.getStats()));
    }

    @GetMapping("/calendar")
    public ResponseEntity<?> calendar(
            @RequestParam(defaultValue = "weekly") String view,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(Map.of("success", true, "data", reservationService.getCalendar(view, date)));
    }

    @PostMapping("/estimate")
    public ResponseEntity<?> estimate(@RequestBody Map<String, Object> body) {
        Long roomId = Long.valueOf(body.get("roomId").toString());
        LocalDate checkIn = LocalDate.parse(body.get("checkIn").toString());
        LocalDate checkOut = LocalDate.parse(body.get("checkOut").toString());
        DurationType durationType = DurationType.valueOf(body.getOrDefault("durationType", "PER_NIGHT").toString().toUpperCase());
        return ResponseEntity.ok(Map.of("success", true, "data", reservationService.getEstimate(roomId, checkIn, checkOut, durationType)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        return ResponseEntity.ok(Map.of("success", true, "data", reservationService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<?> create(
            @RequestParam(required = false) Long roomId,
            @RequestParam(required = false) Long guestId,
            @RequestParam(required = false) Long customerId,
            @RequestBody Reservation reservation) {
        return ResponseEntity.ok(Map.of("success", true, "data",
                reservationService.create(reservation, roomId, guestId, customerId)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @RequestParam(required = false) Long roomId,
            @RequestParam(required = false) Long guestId,
            @RequestParam(required = false) Long customerId,
            @RequestBody Reservation reservation) {
        return ResponseEntity.ok(Map.of("success", true, "data",
                reservationService.update(id, reservation, roomId, guestId, customerId)));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestParam ReservationStatus status) {
        return ResponseEntity.ok(Map.of("success", true, "data", reservationService.updateStatus(id, status)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        reservationService.delete(id);
        return ResponseEntity.ok(Map.of("success", true));
    }
}

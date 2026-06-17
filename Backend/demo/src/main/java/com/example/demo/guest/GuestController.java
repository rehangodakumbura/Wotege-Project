package com.example.demo.guest;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/guests")
public class GuestController {

    private final GuestService guestService;

    public GuestController(GuestService guestService) {
        this.guestService = guestService;
    }

    @GetMapping
    public ResponseEntity<?> list(@RequestParam(required = false) String search) {
        if (search != null && !search.isBlank()) {
            return ResponseEntity.ok(Map.of("success", true, "data", guestService.search(search)));
        }
        return ResponseEntity.ok(Map.of("success", true, "data", guestService.findAll()));
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam String q) {
        List<Guest> results = guestService.search(q);
        return ResponseEntity.ok(Map.of("success", true, "data", results));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        return ResponseEntity.ok(Map.of("success", true, "data", guestService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Guest guest) {
        return ResponseEntity.ok(Map.of("success", true, "data", guestService.create(guest)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Guest guest) {
        return ResponseEntity.ok(Map.of("success", true, "data", guestService.update(id, guest)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        guestService.delete(id);
        return ResponseEntity.ok(Map.of("success", true));
    }
}

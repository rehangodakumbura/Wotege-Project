package com.example.demo.staff;

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
@RequestMapping("/api/staff")
public class StaffController {

    private final StaffService staffService;

    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    @GetMapping
    public List<Staff> list(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long departmentId) {
        if (search != null && !search.isBlank()) {
            return staffService.search(search);
        }
        if (departmentId != null) {
            return staffService.findByDepartment(departmentId);
        }
        return staffService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Staff> get(@PathVariable Long id) {
        return ResponseEntity.ok(staffService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Staff> create(@RequestBody Staff staff) {
        return ResponseEntity.ok(staffService.create(staff));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Staff> update(@PathVariable Long id, @RequestBody Staff staff) {
        return ResponseEntity.ok(staffService.update(id, staff));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Staff> updateStatus(@PathVariable Long id, @RequestParam StaffStatus status) {
        return ResponseEntity.ok(staffService.updateStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        staffService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

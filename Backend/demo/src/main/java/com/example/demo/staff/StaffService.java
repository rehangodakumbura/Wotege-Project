package com.example.demo.staff;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class StaffService {

    private final StaffRepository staffRepository;
    private final DepartmentRepository departmentRepository;

    public StaffService(StaffRepository staffRepository, DepartmentRepository departmentRepository) {
        this.staffRepository = staffRepository;
        this.departmentRepository = departmentRepository;
    }

    @Transactional(readOnly = true)
    public List<Staff> findAll() {
        return staffRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Staff findById(Long id) {
        return staffRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Staff not found"));
    }

    @Transactional(readOnly = true)
    public List<Staff> search(String query) {
        return staffRepository.search(query);
    }

    @Transactional(readOnly = true)
    public List<Staff> findByDepartment(Long departmentId) {
        return staffRepository.findByDepartmentId(departmentId);
    }

    public Staff create(Staff payload) {
        Staff staff = new Staff();
        apply(payload, staff);
        return staffRepository.save(staff);
    }

    public Staff update(Long id, Staff payload) {
        Staff existing = staffRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Staff not found"));
        apply(payload, existing);
        existing.setUpdatedAt(LocalDateTime.now());
        return staffRepository.save(existing);
    }

    public Staff updateStatus(Long id, StaffStatus status) {
        Staff existing = staffRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Staff not found"));
        existing.setStatus(status);
        existing.setUpdatedAt(LocalDateTime.now());
        return staffRepository.save(existing);
    }

    public void delete(Long id) {
        Staff existing = staffRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Staff not found"));
        staffRepository.delete(existing);
    }

    private void apply(Staff payload, Staff target) {
        target.setFirstName(payload.getFirstName());
        target.setLastName(payload.getLastName());
        target.setEmail(payload.getEmail());
        target.setPhone(payload.getPhone());
        target.setPosition(payload.getPosition());
        target.setShift(payload.getShift());
        target.setHireDate(payload.getHireDate());
        if (payload.getStatus() != null) {
            target.setStatus(payload.getStatus());
        }
        if (payload.getDepartment() != null && payload.getDepartment().getId() != null) {
            Department dept = departmentRepository.findById(payload.getDepartment().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found"));
            target.setDepartment(dept);
        }
    }
}

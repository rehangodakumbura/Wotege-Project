package com.example.demo.staff;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Transactional(readOnly = true)
    public List<Department> findAll() {
        return departmentRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Department findById(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found"));
    }

    public Department create(Department payload) {
        if (departmentRepository.existsByCode(payload.getCode())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Department code already exists");
        }
        Department department = new Department();
        apply(payload, department);
        return departmentRepository.save(department);
    }

    public Department update(Long id, Department payload) {
        Department existing = departmentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found"));
        apply(payload, existing);
        return departmentRepository.save(existing);
    }

    public void delete(Long id) {
        Department existing = departmentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found"));
        departmentRepository.delete(existing);
    }

    private void apply(Department payload, Department target) {
        target.setCode(payload.getCode());
        target.setName(payload.getName());
        target.setDescription(payload.getDescription());
    }
}

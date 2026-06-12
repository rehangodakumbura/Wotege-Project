package com.example.demo.role;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class RoleService {

	private final RoleRepository roleRepository;

	public RoleService(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	@Transactional(readOnly = true)
	public List<Role> findAll() {
		return roleRepository.findAll();
	}

	public Role create(Role role) {
		role.setId(null);
		return roleRepository.save(role);
	}

	public Role update(Long id, Role payload) {
		Role existing = roleRepository.findById(id)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found"));
		existing.setCode(payload.getCode());
		existing.setName(payload.getName());
		existing.setDescription(payload.getDescription());
		existing.setSystemRole(payload.isSystemRole());
		return roleRepository.save(existing);
	}

	public void delete(Long id) {
		Role existing = roleRepository.findById(id)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found"));
		if (existing.isSystemRole()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "System roles cannot be deleted");
		}
		roleRepository.delete(existing);
	}
}
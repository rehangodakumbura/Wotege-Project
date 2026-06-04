package com.example.demo.branch;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BranchService {

	private final BranchRepository branchRepository;

	public BranchService(BranchRepository branchRepository) {
		this.branchRepository = branchRepository;
	}

	@Transactional(readOnly = true)
	public List<Branch> findAll() {
		return branchRepository.findAll();
	}

	@Transactional(readOnly = true)
	public List<Branch> findActive() {
		return branchRepository.findByActiveTrue();
	}

	@Transactional(readOnly = true)
	public Branch findById(Long id) {
		return branchRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Branch not found: " + id));
	}
}

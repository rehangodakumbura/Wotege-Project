package com.example.demo.branch;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/branches")
public class BranchController {

	private final BranchService branchService;

	public BranchController(BranchService branchService) {
		this.branchService = branchService;
	}

	@GetMapping
	public ResponseEntity<List<Branch>> list() {
		return ResponseEntity.ok(branchService.findActive());
	}

	@GetMapping("/all")
	public ResponseEntity<List<Branch>> listAll() {
		return ResponseEntity.ok(branchService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Branch> getById(@PathVariable Long id) {
		return ResponseEntity.ok(branchService.findById(id));
	}
}

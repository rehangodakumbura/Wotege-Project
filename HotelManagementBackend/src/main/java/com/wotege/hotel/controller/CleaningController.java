package com.wotege.hotel.controller;

import com.wotege.hotel.dto.ApiResponse;
import com.wotege.hotel.dto.cleaning.CleaningRequest;
import com.wotege.hotel.dto.cleaning.CleaningResponse;
import com.wotege.hotel.service.CleaningService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cleaning")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CleaningController {

    private final CleaningService cleaningService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CleaningResponse>>> getAllCleanings() {
        return ResponseEntity.ok(ApiResponse.success(cleaningService.getAllCleanings()));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CleaningResponse>> createCleaning(@Valid @RequestBody CleaningRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Cleaning task created", cleaningService.createCleaning(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CleaningResponse>> updateCleaning(
            @PathVariable Long id,
            @RequestParam String status) {
        return ResponseEntity.ok(ApiResponse.success("Cleaning updated", cleaningService.updateCleaning(id, status)));
    }
}

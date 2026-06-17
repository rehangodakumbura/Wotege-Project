package com.wotege.hotel.controller;

import com.wotege.hotel.dto.ApiResponse;
import com.wotege.hotel.dto.packages.PackageRequestDTO;
import com.wotege.hotel.dto.packages.PackageResponseDTO;
import com.wotege.hotel.enums.PackageCategory;
import com.wotege.hotel.enums.PackageStatus;
import com.wotege.hotel.service.PackageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/packages")
@RequiredArgsConstructor
public class PackageController {

    private final PackageService packageService;

    private static final String UPLOAD_DIR = "uploads/packages/";

    @GetMapping
    public ResponseEntity<ApiResponse<List<PackageResponseDTO>>> getAllPackages() {
        return ResponseEntity.ok(ApiResponse.success(packageService.getAllPackages()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PackageResponseDTO>> getPackageById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(packageService.getPackageById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PackageResponseDTO>> createPackage(
            @Valid @RequestBody PackageRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Package created successfully",
                        packageService.createPackage(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PackageResponseDTO>> updatePackage(
            @PathVariable Long id,
            @Valid @RequestBody PackageRequestDTO request) {
        return ResponseEntity.ok(ApiResponse.success("Package updated successfully",
                packageService.updatePackage(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePackage(@PathVariable Long id) {
        packageService.deletePackage(id);
        return ResponseEntity.ok(ApiResponse.success("Package deleted successfully", null));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<PackageResponseDTO>> changeStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        PackageStatus status = PackageStatus.valueOf(body.get("status").toUpperCase());
        return ResponseEntity.ok(ApiResponse.success("Status changed successfully",
                packageService.changeStatus(id, status)));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResponse<List<PackageResponseDTO>>> getPackagesByCategory(
            @PathVariable String category) {
        PackageCategory cat = PackageCategory.valueOf(category.toUpperCase());
        return ResponseEntity.ok(ApiResponse.success(packageService.getPackagesByCategory(cat)));
    }

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<Map<String, String>>> uploadImage(
            @RequestParam("image") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Image file is required"));
        }

        try {
            String fileName = UUID.randomUUID().toString() + "_"
                    + file.getOriginalFilename().replaceAll("\\s+", "_");
            Path uploadPath = Paths.get(UPLOAD_DIR);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            String imageUrl = UPLOAD_DIR + fileName;
            return ResponseEntity.ok(ApiResponse.success("Image uploaded successfully",
                    Map.of("imageUrl", imageUrl)));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to upload image: " + e.getMessage()));
        }
    }
}

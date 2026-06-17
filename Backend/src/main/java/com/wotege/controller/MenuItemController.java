package com.wotege.controller;

import com.wotege.dto.ApiResponse;
import com.wotege.dto.MenuItemRequestDTO;
import com.wotege.dto.MenuItemResponseDTO;
import com.wotege.dto.MenuItemStatusDTO;
import com.wotege.service.MenuItemService;
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
@RequestMapping("/api/menu-items")
@RequiredArgsConstructor
public class MenuItemController {

    private final MenuItemService menuItemService;
    private final Path uploadDir = Paths.get("uploads/menu");

    @GetMapping
    public ResponseEntity<ApiResponse<List<MenuItemResponseDTO>>> getAllMenuItems() {
        return ResponseEntity.ok(ApiResponse.success(menuItemService.getAllMenuItems()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MenuItemResponseDTO>> getMenuItemById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(menuItemService.getMenuItemById(id)));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<List<MenuItemResponseDTO>>> getMenuItemsByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(ApiResponse.success(menuItemService.getMenuItemsByCategory(categoryId)));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<MenuItemResponseDTO>>> searchMenuItems(@RequestParam String keyword) {
        return ResponseEntity.ok(ApiResponse.success(menuItemService.searchMenuItems(keyword)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<MenuItemResponseDTO>> createMenuItem(@Valid @RequestBody MenuItemRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Menu item created successfully", menuItemService.createMenuItem(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MenuItemResponseDTO>> updateMenuItem(@PathVariable Long id,
                                                                            @Valid @RequestBody MenuItemRequestDTO request) {
        return ResponseEntity.ok(ApiResponse.success("Menu item updated successfully", menuItemService.updateMenuItem(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteMenuItem(@PathVariable Long id) {
        menuItemService.deleteMenuItem(id);
        return ResponseEntity.ok(ApiResponse.success("Menu item deleted successfully", null));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<MenuItemResponseDTO>> toggleAvailability(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Availability toggled successfully", menuItemService.toggleAvailability(id)));
    }

    @PatchMapping("/{id}/order-count")
    public ResponseEntity<ApiResponse<MenuItemResponseDTO>> increaseOrderCount(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Order count increased", menuItemService.increaseOrderCount(id)));
    }

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<Map<String, String>>> uploadImage(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponse.error("File is empty"));
        }

        String contentType = file.getContentType();
        if (contentType == null || !(contentType.equals("image/jpeg") || contentType.equals("image/png") || contentType.equals("image/webp"))) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Only JPG, PNG, and WEBP files are allowed"));
        }

        if (file.getSize() > 5 * 1024 * 1024) {
            return ResponseEntity.badRequest().body(ApiResponse.error("File size exceeds 5MB limit"));
        }

        try {
            Files.createDirectories(uploadDir);
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String filename = UUID.randomUUID().toString() + extension;
            Path targetPath = uploadDir.resolve(filename);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            String imageUrl = "/uploads/menu/" + filename;
            return ResponseEntity.ok(ApiResponse.success("Image uploaded successfully", Map.of("imageUrl", imageUrl)));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to upload file: " + e.getMessage()));
        }
    }
}

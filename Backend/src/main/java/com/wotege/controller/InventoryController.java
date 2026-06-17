package com.wotege.controller;

import com.wotege.dto.*;
import com.wotege.enums.InventoryCategory;
import com.wotege.service.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<InventoryResponseDTO>>> getAllItems() {
        List<InventoryResponseDTO> items = inventoryService.getAllItems();
        return ResponseEntity.ok(ApiResponse.success(items));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<InventoryResponseDTO>> getItemById(@PathVariable Long id) {
        InventoryResponseDTO item = inventoryService.getItemById(id);
        return ResponseEntity.ok(ApiResponse.success(item));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<InventoryResponseDTO>> createItem(
            @Valid @RequestBody InventoryRequestDTO requestDTO) {
        InventoryResponseDTO item = inventoryService.createItem(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Inventory item created successfully", item));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<InventoryResponseDTO>> updateItem(
            @PathVariable Long id,
            @Valid @RequestBody InventoryRequestDTO requestDTO) {
        InventoryResponseDTO item = inventoryService.updateItem(id, requestDTO);
        return ResponseEntity.ok(ApiResponse.success("Inventory item updated successfully", item));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteItem(@PathVariable Long id) {
        inventoryService.deleteItem(id);
        return ResponseEntity.ok(ApiResponse.success("Inventory item deleted successfully", null));
    }

    @PatchMapping("/{id}/stock")
    public ResponseEntity<ApiResponse<InventoryResponseDTO>> updateStock(
            @PathVariable Long id,
            @Valid @RequestBody StockUpdateDTO stockUpdateDTO) {
        InventoryResponseDTO item = inventoryService.updateStock(id, stockUpdateDTO);
        return ResponseEntity.ok(ApiResponse.success("Stock updated successfully", item));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResponse<List<InventoryResponseDTO>>> getItemsByCategory(
            @PathVariable InventoryCategory category) {
        List<InventoryResponseDTO> items = inventoryService.getItemsByCategory(category);
        return ResponseEntity.ok(ApiResponse.success(items));
    }

    @GetMapping("/low-stock")
    public ResponseEntity<ApiResponse<List<InventoryResponseDTO>>> getLowStockItems() {
        List<InventoryResponseDTO> items = inventoryService.getLowStockItems();
        return ResponseEntity.ok(ApiResponse.success(items));
    }

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<DashboardResponseDTO>> getDashboardSummary() {
        DashboardResponseDTO dashboard = inventoryService.getDashboardSummary();
        return ResponseEntity.ok(ApiResponse.success(dashboard));
    }
}

package com.wotege.service.impl;

import com.wotege.dto.*;
import com.wotege.entity.InventoryItem;
import com.wotege.enums.InventoryCategory;
import com.wotege.enums.InventoryStatus;
import com.wotege.exception.ResourceNotFoundException;
import com.wotege.repository.InventoryRepository;
import com.wotege.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;

    @Override
    @Transactional
    public InventoryResponseDTO createItem(InventoryRequestDTO requestDTO) {
        InventoryItem item = InventoryItem.builder()
                .itemCode(generateItemCode())
                .itemName(requestDTO.getItemName())
                .category(requestDTO.getCategory())
                .unitOfMeasurement(requestDTO.getUnitOfMeasurement())
                .currentStock(requestDTO.getCurrentStock())
                .minimumThreshold(requestDTO.getMinimumThreshold())
                .supplier(requestDTO.getSupplier())
                .unitCost(requestDTO.getUnitCost())
                .build();

        item = inventoryRepository.save(item);
        return mapToResponseDTO(item);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryResponseDTO> getAllItems() {
        return inventoryRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public InventoryResponseDTO getItemById(Long id) {
        InventoryItem item = inventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory item not found with id: " + id));
        return mapToResponseDTO(item);
    }

    @Override
    @Transactional
    public InventoryResponseDTO updateItem(Long id, InventoryRequestDTO requestDTO) {
        InventoryItem item = inventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory item not found with id: " + id));

        item.setItemName(requestDTO.getItemName());
        item.setCategory(requestDTO.getCategory());
        item.setUnitOfMeasurement(requestDTO.getUnitOfMeasurement());
        item.setCurrentStock(requestDTO.getCurrentStock());
        item.setMinimumThreshold(requestDTO.getMinimumThreshold());
        item.setSupplier(requestDTO.getSupplier());
        item.setUnitCost(requestDTO.getUnitCost());

        item = inventoryRepository.save(item);
        return mapToResponseDTO(item);
    }

    @Override
    @Transactional
    public void deleteItem(Long id) {
        InventoryItem item = inventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory item not found with id: " + id));
        inventoryRepository.delete(item);
    }

    @Override
    @Transactional
    public InventoryResponseDTO updateStock(Long id, StockUpdateDTO stockUpdateDTO) {
        InventoryItem item = inventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory item not found with id: " + id));

        item.setCurrentStock(stockUpdateDTO.getQuantity());
        item.setLastRestockDate(LocalDateTime.now());
        item.calculateInventoryValue();
        item.calculateStatus();
        item.setUpdatedAt(LocalDateTime.now());

        item = inventoryRepository.save(item);
        return mapToResponseDTO(item);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryResponseDTO> getItemsByCategory(InventoryCategory category) {
        return inventoryRepository.findByCategory(category)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryResponseDTO> getLowStockItems() {
        List<InventoryStatus> lowStockStatuses = List.of(InventoryStatus.LOW_STOCK, InventoryStatus.CRITICAL);
        return lowStockStatuses.stream()
                .flatMap(status -> inventoryRepository.findByStatus(status).stream())
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public DashboardResponseDTO getDashboardSummary() {
        List<InventoryItem> allItems = inventoryRepository.findAll();

        long totalItems = allItems.size();

        long lowStockAlerts = allItems.stream()
                .filter(item -> item.getStatus() == InventoryStatus.LOW_STOCK
                        || item.getStatus() == InventoryStatus.CRITICAL)
                .count();

        double inventoryValue = allItems.stream()
                .mapToDouble(item -> item.getInventoryValue() != null ? item.getInventoryValue() : 0.0)
                .sum();

        return DashboardResponseDTO.builder()
                .totalItems(totalItems)
                .lowStockAlerts(lowStockAlerts)
                .inventoryValue(inventoryValue)
                .pendingOrders(0)
                .build();
    }

    private InventoryResponseDTO mapToResponseDTO(InventoryItem item) {
        return InventoryResponseDTO.builder()
                .id(item.getId())
                .itemCode(item.getItemCode())
                .itemName(item.getItemName())
                .category(item.getCategory())
                .unitOfMeasurement(item.getUnitOfMeasurement())
                .currentStock(item.getCurrentStock())
                .minimumThreshold(item.getMinimumThreshold())
                .supplier(item.getSupplier())
                .unitCost(item.getUnitCost())
                .inventoryValue(item.getInventoryValue())
                .status(item.getStatus())
                .lastRestockDate(item.getLastRestockDate())
                .createdAt(item.getCreatedAt())
                .updatedAt(item.getUpdatedAt())
                .build();
    }

    private String generateItemCode() {
        InventoryItem lastItem = inventoryRepository.findTopByOrderByIdDesc();
        if (lastItem == null) {
            return "INV-001";
        }
        String lastCode = lastItem.getItemCode();
        int lastNumber = Integer.parseInt(lastCode.substring(4));
        return String.format("INV-%03d", lastNumber + 1);
    }
}

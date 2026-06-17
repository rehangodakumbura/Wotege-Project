package com.wotege.service;

import com.wotege.dto.*;
import com.wotege.enums.InventoryCategory;

import java.util.List;

public interface InventoryService {

    InventoryResponseDTO createItem(InventoryRequestDTO requestDTO);

    List<InventoryResponseDTO> getAllItems();

    InventoryResponseDTO getItemById(Long id);

    InventoryResponseDTO updateItem(Long id, InventoryRequestDTO requestDTO);

    void deleteItem(Long id);

    InventoryResponseDTO updateStock(Long id, StockUpdateDTO stockUpdateDTO);

    List<InventoryResponseDTO> getItemsByCategory(InventoryCategory category);

    List<InventoryResponseDTO> getLowStockItems();

    DashboardResponseDTO getDashboardSummary();
}

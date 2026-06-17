package com.wotege.repository;

import com.wotege.entity.InventoryItem;
import com.wotege.enums.InventoryCategory;
import com.wotege.enums.InventoryStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<InventoryItem, Long> {

    List<InventoryItem> findByCategory(InventoryCategory category);

    List<InventoryItem> findByStatus(InventoryStatus status);

    List<InventoryItem> findByItemNameContainingIgnoreCase(String itemName);

    InventoryItem findTopByOrderByIdDesc();
}

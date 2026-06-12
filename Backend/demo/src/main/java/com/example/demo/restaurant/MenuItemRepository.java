package com.example.demo.restaurant;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {

    List<MenuItem> findByCategoryId(Long categoryId);

    List<MenuItem> findByStatus(ItemStatus status);

    List<MenuItem> findByCategory_IdAndStatus(Long categoryId, ItemStatus status);
}

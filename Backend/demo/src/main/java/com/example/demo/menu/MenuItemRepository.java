package com.example.demo.menu;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
	List<MenuItem> findByAvailableTrue();
	List<MenuItem> findByCategoryIdAndAvailableTrue(Long categoryId);

	@Query("SELECT m FROM MenuItem m WHERE m.available = true AND " +
			"(LOWER(m.name) LIKE LOWER(CONCAT('%', :q, '%')) " +
			"OR LOWER(m.code) LIKE LOWER(CONCAT('%', :q, '%')) " +
			"OR LOWER(m.description) LIKE LOWER(CONCAT('%', :q, '%')))")
	List<MenuItem> searchByName(@Param("q") String query);
}

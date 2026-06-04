package com.example.demo.menu;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuCategoryRepository extends JpaRepository<MenuCategory, Long> {
	List<MenuCategory> findByActiveTrueOrderByDisplayOrderAsc();
}

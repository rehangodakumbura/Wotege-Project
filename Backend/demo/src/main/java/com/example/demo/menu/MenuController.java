package com.example.demo.menu;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/menu")
public class MenuController {

	private final MenuService menuService;

	public MenuController(MenuService menuService) {
		this.menuService = menuService;
	}

	@GetMapping("/categories")
	public ResponseEntity<List<MenuCategory>> listCategories() {
		return ResponseEntity.ok(menuService.findAllCategories());
	}

	@GetMapping("/items")
	public ResponseEntity<List<MenuItem>> listItems(
			@RequestParam(required = false) Long categoryId,
			@RequestParam(required = false) String search) {
		List<MenuItem> items;
		if (search != null && !search.isBlank()) {
			items = menuService.searchItems(search);
		} else if (categoryId != null) {
			items = menuService.findItemsByCategory(categoryId);
		} else {
			items = menuService.findAvailableItems();
		}
		return ResponseEntity.ok(items);
	}
}

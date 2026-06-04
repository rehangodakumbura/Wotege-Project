package com.example.demo.menu;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MenuService {

	private final MenuItemRepository menuItemRepository;
	private final MenuCategoryRepository menuCategoryRepository;

	public MenuService(MenuItemRepository menuItemRepository, MenuCategoryRepository menuCategoryRepository) {
		this.menuItemRepository = menuItemRepository;
		this.menuCategoryRepository = menuCategoryRepository;
	}

	@Transactional(readOnly = true)
	public List<MenuCategory> findAllCategories() {
		return menuCategoryRepository.findByActiveTrueOrderByDisplayOrderAsc();
	}

	@Transactional(readOnly = true)
	public List<MenuItem> findAllItems() {
		return menuItemRepository.findAll();
	}

	@Transactional(readOnly = true)
	public List<MenuItem> findAvailableItems() {
		return menuItemRepository.findByAvailableTrue();
	}

	@Transactional(readOnly = true)
	public List<MenuItem> findItemsByCategory(Long categoryId) {
		return menuItemRepository.findByCategoryIdAndAvailableTrue(categoryId);
	}

	@Transactional(readOnly = true)
	public List<MenuItem> searchItems(String query) {
		if (query == null || query.isBlank()) {
			return findAvailableItems();
		}
		return menuItemRepository.searchByName(query.trim());
	}
}

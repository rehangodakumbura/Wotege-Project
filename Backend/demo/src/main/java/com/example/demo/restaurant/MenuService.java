package com.example.demo.restaurant;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class MenuService {

    private final MenuCategoryRepository categoryRepository;
    private final MenuItemRepository itemRepository;

    public MenuService(MenuCategoryRepository categoryRepository, MenuItemRepository itemRepository) {
        this.categoryRepository = categoryRepository;
        this.itemRepository = itemRepository;
    }

    @Transactional(readOnly = true)
    public List<MenuCategory> findAllCategories() {
        return categoryRepository.findAll();
    }

    @Transactional(readOnly = true)
    public MenuCategory findCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
    }

    public MenuCategory createCategory(MenuCategory payload) {
        MenuCategory category = new MenuCategory();
        category.setName(payload.getName());
        category.setDescription(payload.getDescription());
        category.setSortOrder(payload.getSortOrder());
        category.setActive(payload.isActive());
        return categoryRepository.save(category);
    }

    public MenuCategory updateCategory(Long id, MenuCategory payload) {
        MenuCategory existing = categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
        existing.setName(payload.getName());
        existing.setDescription(payload.getDescription());
        existing.setSortOrder(payload.getSortOrder());
        existing.setActive(payload.isActive());
        return categoryRepository.save(existing);
    }

    public void deleteCategory(Long id) {
        MenuCategory existing = categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
        categoryRepository.delete(existing);
    }

    @Transactional(readOnly = true)
    public List<MenuItem> findAllItems() {
        return itemRepository.findAll();
    }

    @Transactional(readOnly = true)
    public MenuItem findItemById(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Menu item not found"));
    }

    @Transactional(readOnly = true)
    public List<MenuItem> findItemsByCategory(Long categoryId) {
        return itemRepository.findByCategoryId(categoryId);
    }

    public MenuItem createItem(MenuItem payload, Long categoryId) {
        MenuItem item = new MenuItem();
        item.setName(payload.getName());
        item.setDescription(payload.getDescription());
        item.setPrice(payload.getPrice());
        item.setImageUrl(payload.getImageUrl());
        item.setStatus(payload.getStatus() != null ? payload.getStatus() : ItemStatus.AVAILABLE);

        if (categoryId != null) {
            MenuCategory category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
            item.setCategory(category);
        }

        return itemRepository.save(item);
    }

    public MenuItem updateItem(Long id, MenuItem payload, Long categoryId) {
        MenuItem existing = itemRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Menu item not found"));
        existing.setName(payload.getName());
        existing.setDescription(payload.getDescription());
        existing.setPrice(payload.getPrice());
        existing.setImageUrl(payload.getImageUrl());
        existing.setStatus(payload.getStatus());
        existing.setUpdatedAt(LocalDateTime.now());

        if (categoryId != null) {
            MenuCategory category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
            existing.setCategory(category);
        }

        return itemRepository.save(existing);
    }

    public void deleteItem(Long id) {
        MenuItem existing = itemRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Menu item not found"));
        itemRepository.delete(existing);
    }
}

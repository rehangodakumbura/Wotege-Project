package com.wotege.service;

import com.wotege.dto.MenuItemRequestDTO;
import com.wotege.dto.MenuItemResponseDTO;
import com.wotege.entity.Category;
import com.wotege.entity.MenuItem;
import com.wotege.exception.ResourceNotFoundException;
import com.wotege.repository.MenuItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final CategoryService categoryService;

    public List<MenuItemResponseDTO> getAllMenuItems() {
        return menuItemRepository.findAll().stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public MenuItemResponseDTO getMenuItemById(Long id) {
        return toResponseDTO(findById(id));
    }

    public List<MenuItemResponseDTO> getMenuItemsByCategory(Long categoryId) {
        return menuItemRepository.findByCategoryId(categoryId).stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public List<MenuItemResponseDTO> searchMenuItems(String keyword) {
        return menuItemRepository.findByNameContainingIgnoreCase(keyword).stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Transactional
    public MenuItemResponseDTO createMenuItem(MenuItemRequestDTO request) {
        Category category = categoryService.findById(request.getCategoryId());
        MenuItem item = new MenuItem();
        item.setName(request.getName());
        item.setDescription(request.getDescription());
        item.setPrice(request.getPrice());
        item.setImageUrl(request.getImageUrl());
        item.setAvailable(request.getAvailable() != null ? request.getAvailable() : true);
        item.setOrderCount(0);
        item.setCategory(category);
        return toResponseDTO(menuItemRepository.save(item));
    }

    @Transactional
    public MenuItemResponseDTO updateMenuItem(Long id, MenuItemRequestDTO request) {
        MenuItem item = findById(id);
        Category category = categoryService.findById(request.getCategoryId());
        item.setName(request.getName());
        item.setDescription(request.getDescription());
        item.setPrice(request.getPrice());
        item.setImageUrl(request.getImageUrl());
        item.setAvailable(request.getAvailable() != null ? request.getAvailable() : item.getAvailable());
        item.setCategory(category);
        return toResponseDTO(menuItemRepository.save(item));
    }

    @Transactional
    public void deleteMenuItem(Long id) {
        if (!menuItemRepository.existsById(id)) {
            throw new ResourceNotFoundException("Menu item not found with id: " + id);
        }
        menuItemRepository.deleteById(id);
    }

    @Transactional
    public MenuItemResponseDTO toggleAvailability(Long id) {
        MenuItem item = findById(id);
        item.setAvailable(!item.getAvailable());
        return toResponseDTO(menuItemRepository.save(item));
    }

    @Transactional
    public MenuItemResponseDTO updateAvailability(Long id, boolean available) {
        MenuItem item = findById(id);
        item.setAvailable(available);
        return toResponseDTO(menuItemRepository.save(item));
    }

    @Transactional
    public MenuItemResponseDTO increaseOrderCount(Long id) {
        MenuItem item = findById(id);
        item.setOrderCount(item.getOrderCount() + 1);
        return toResponseDTO(menuItemRepository.save(item));
    }

    public MenuItem findById(Long id) {
        return menuItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found with id: " + id));
    }

    private MenuItemResponseDTO toResponseDTO(MenuItem item) {
        return MenuItemResponseDTO.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .price(item.getPrice())
                .imageUrl(item.getImageUrl())
                .available(item.getAvailable())
                .status(item.getAvailable() ? "Available" : "Out of Stock")
                .orderCount(item.getOrderCount())
                .categoryId(item.getCategory().getId())
                .categoryName(item.getCategory().getName())
                .createdAt(item.getCreatedAt())
                .updatedAt(item.getUpdatedAt())
                .build();
    }
}

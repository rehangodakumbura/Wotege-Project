package com.example.demo.restaurant;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    public List<MenuCategory> listCategories() {
        return menuService.findAllCategories();
    }

    @GetMapping("/categories/{id}")
    public ResponseEntity<MenuCategory> getCategory(@PathVariable Long id) {
        return ResponseEntity.ok(menuService.findCategoryById(id));
    }

    @PostMapping("/categories")
    public ResponseEntity<MenuCategory> createCategory(@RequestBody MenuCategory category) {
        return ResponseEntity.ok(menuService.createCategory(category));
    }

    @PutMapping("/categories/{id}")
    public ResponseEntity<MenuCategory> updateCategory(@PathVariable Long id, @RequestBody MenuCategory category) {
        return ResponseEntity.ok(menuService.updateCategory(id, category));
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        menuService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/items")
    public List<MenuItem> listItems(@RequestParam(required = false) Long categoryId) {
        if (categoryId != null) {
            return menuService.findItemsByCategory(categoryId);
        }
        return menuService.findAllItems();
    }

    @GetMapping("/items/{id}")
    public ResponseEntity<MenuItem> getItem(@PathVariable Long id) {
        return ResponseEntity.ok(menuService.findItemById(id));
    }

    @PostMapping("/items")
    public ResponseEntity<MenuItem> createItem(
            @RequestBody MenuItem item,
            @RequestParam(required = false) Long categoryId) {
        return ResponseEntity.ok(menuService.createItem(item, categoryId));
    }

    @PutMapping("/items/{id}")
    public ResponseEntity<MenuItem> updateItem(
            @PathVariable Long id,
            @RequestBody MenuItem item,
            @RequestParam(required = false) Long categoryId) {
        return ResponseEntity.ok(menuService.updateItem(id, item, categoryId));
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        menuService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }
}

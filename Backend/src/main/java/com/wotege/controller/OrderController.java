package com.wotege.controller;

import com.wotege.dto.OrderItemRequest;
import com.wotege.dto.OrderRequest;
import com.wotege.dto.OrderResponse;
import com.wotege.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrder(id));
    }

    @PostMapping("/{id}/items")
    public ResponseEntity<OrderResponse> addItem(@PathVariable Long id,
                                                  @Valid @RequestBody OrderItemRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.addItem(id, request));
    }

    @PutMapping("/{id}/items/{itemId}/increase")
    public ResponseEntity<OrderResponse> increaseQuantity(@PathVariable Long id,
                                                           @PathVariable Long itemId) {
        return ResponseEntity.ok(orderService.increaseQuantity(id, itemId));
    }

    @PutMapping("/{id}/items/{itemId}/decrease")
    public ResponseEntity<OrderResponse> decreaseQuantity(@PathVariable Long id,
                                                           @PathVariable Long itemId) {
        return ResponseEntity.ok(orderService.decreaseQuantity(id, itemId));
    }

    @DeleteMapping("/{id}/items/{itemId}")
    public ResponseEntity<OrderResponse> removeItem(@PathVariable Long id,
                                                     @PathVariable Long itemId) {
        return ResponseEntity.ok(orderService.removeItem(id, itemId));
    }

    @PutMapping("/{id}/kitchen")
    public ResponseEntity<OrderResponse> sendToKitchen(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.sendToKitchen(id));
    }

    @PutMapping("/{id}/pay")
    public ResponseEntity<OrderResponse> payOrder(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.payOrder(id));
    }
}

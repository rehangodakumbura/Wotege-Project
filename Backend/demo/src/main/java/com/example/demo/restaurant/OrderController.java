package com.example.demo.restaurant;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public List<Order> list(@RequestParam(required = false) OrderStatus status) {
        if (status != null) {
            return orderService.findByStatus(status);
        }
        return orderService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> get(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Order> create(@RequestBody OrderCreateRequest request) {
        return ResponseEntity.ok(orderService.createOrder(request.getOrder(), request.getItems()));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Order> updateStatus(@PathVariable Long id, @RequestParam OrderStatus status) {
        return ResponseEntity.ok(orderService.updateStatus(id, status));
    }

    @PostMapping("/{id}/pay")
    public ResponseEntity<Order> processPayment(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        String paymentMethod = body.get("paymentMethod");
        return ResponseEntity.ok(orderService.processPayment(id, paymentMethod));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<Void> cancel(@PathVariable Long id) {
        orderService.cancelOrder(id);
        return ResponseEntity.noContent().build();
    }

    public static class OrderCreateRequest {
        private Order order;
        private List<OrderService.OrderItemData> items;

        public Order getOrder() {
            return order;
        }

        public void setOrder(Order order) {
            this.order = order;
        }

        public List<OrderService.OrderItemData> getItems() {
            return items;
        }

        public void setItems(List<OrderService.OrderItemData> items) {
            this.items = items;
        }
    }
}

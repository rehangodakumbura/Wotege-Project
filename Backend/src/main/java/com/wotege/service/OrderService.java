package com.wotege.service;

import com.wotege.dto.*;
import com.wotege.entity.*;
import com.wotege.exception.BadRequestException;
import com.wotege.exception.ResourceNotFoundException;
import com.wotege.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final RestaurantTableService tableService;
    private final MenuItemService menuItemService;

    private static final double TAX_RATE = 0.08;

    @Transactional
    public OrderResponse createOrder(OrderRequest request) {
        RestaurantTable table = tableService.findById(request.getTableId());
        if (table.getStatus() == RestaurantTable.TableStatus.OCCUPIED) {
            throw new BadRequestException("Table is already occupied");
        }

        Order order = new Order();
        order.setTable(table);
        order.setStatus(Order.OrderStatus.PENDING);
        order.setSubtotal(0.0);
        order.setTax(0.0);
        order.setTotal(0.0);
        order.setCreatedAt(LocalDateTime.now());
        order.setItems(new java.util.ArrayList<>());
        order = orderRepository.save(order);

        tableService.updateTableStatus(table.getId(), RestaurantTable.TableStatus.OCCUPIED);

        return toResponse(order);
    }

    public OrderResponse getOrder(Long id) {
        Order order = findById(id);
        return toResponse(order);
    }

    @Transactional
    public OrderResponse addItem(Long orderId, OrderItemRequest request) {
        Order order = findById(orderId);
        validateOrderModifiable(order);

        MenuItem menuItem = menuItemService.findById(request.getMenuItemId());

        Optional<OrderItem> existingItem = order.getItems().stream()
                .filter(oi -> oi.getMenuItem().getId().equals(request.getMenuItemId()))
                .findFirst();

        if (existingItem.isPresent()) {
            OrderItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + request.getQuantity());
        } else {
            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setMenuItem(menuItem);
            item.setQuantity(request.getQuantity());
            item.setPrice(menuItem.getPrice());
            order.getItems().add(item);
        }

        recalculateOrder(order);
        order = orderRepository.save(order);
        return toResponse(order);
    }

    @Transactional
    public OrderResponse increaseQuantity(Long orderId, Long itemId) {
        Order order = findById(orderId);
        validateOrderModifiable(order);

        OrderItem item = findOrderItem(order, itemId);
        item.setQuantity(item.getQuantity() + 1);

        recalculateOrder(order);
        order = orderRepository.save(order);
        return toResponse(order);
    }

    @Transactional
    public OrderResponse decreaseQuantity(Long orderId, Long itemId) {
        Order order = findById(orderId);
        validateOrderModifiable(order);

        OrderItem item = findOrderItem(order, itemId);
        if (item.getQuantity() <= 1) {
            order.getItems().remove(item);
        } else {
            item.setQuantity(item.getQuantity() - 1);
        }

        recalculateOrder(order);
        order = orderRepository.save(order);
        return toResponse(order);
    }

    @Transactional
    public OrderResponse removeItem(Long orderId, Long itemId) {
        Order order = findById(orderId);
        validateOrderModifiable(order);

        OrderItem item = findOrderItem(order, itemId);
        order.getItems().remove(item);

        recalculateOrder(order);
        order = orderRepository.save(order);
        return toResponse(order);
    }

    @Transactional
    public OrderResponse sendToKitchen(Long orderId) {
        Order order = findById(orderId);
        if (order.getStatus() != Order.OrderStatus.PENDING) {
            throw new BadRequestException("Order cannot be sent to kitchen from current status");
        }
        order.setStatus(Order.OrderStatus.KITCHEN);
        order = orderRepository.save(order);
        return toResponse(order);
    }

    @Transactional
    public OrderResponse payOrder(Long orderId) {
        Order order = findById(orderId);
        if (order.getStatus() == Order.OrderStatus.PAID) {
            throw new BadRequestException("Order is already paid");
        }

        order.setStatus(Order.OrderStatus.PAID);
        order = orderRepository.save(order);

        tableService.updateTableStatus(order.getTable().getId(), RestaurantTable.TableStatus.AVAILABLE);

        return toResponse(order);
    }

    public Order findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
    }

    private void validateOrderModifiable(Order order) {
        if (order.getStatus() == Order.OrderStatus.PAID) {
            throw new BadRequestException("Cannot modify a paid order");
        }
    }

    private OrderItem findOrderItem(Order order, Long itemId) {
        return order.getItems().stream()
                .filter(oi -> oi.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Order item not found with id: " + itemId));
    }

    private void recalculateOrder(Order order) {
        double subtotal = order.getItems().stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
        double tax = subtotal * TAX_RATE;
        double total = subtotal + tax;

        order.setSubtotal(Math.round(subtotal * 100.0) / 100.0);
        order.setTax(Math.round(tax * 100.0) / 100.0);
        order.setTotal(Math.round(total * 100.0) / 100.0);
    }

    private OrderResponse toResponse(Order order) {
        List<OrderItemResponse> itemResponses = order.getItems().stream()
                .map(item -> OrderItemResponse.builder()
                        .id(item.getId())
                        .menuItemId(item.getMenuItem().getId())
                        .menuItemName(item.getMenuItem().getName())
                        .imageUrl(item.getMenuItem().getImageUrl())
                        .quantity(item.getQuantity())
                        .price(item.getPrice())
                        .totalPrice(Math.round(item.getPrice() * item.getQuantity() * 100.0) / 100.0)
                        .build())
                .toList();

        return OrderResponse.builder()
                .id(order.getId())
                .tableId(order.getTable().getId())
                .tableNumber(order.getTable().getTableNumber())
                .status(order.getStatus().name())
                .subtotal(order.getSubtotal())
                .tax(order.getTax())
                .total(order.getTotal())
                .createdAt(order.getCreatedAt())
                .items(itemResponses)
                .build();
    }
}

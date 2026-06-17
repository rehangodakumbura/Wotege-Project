package com.wotege.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {

    private Long id;
    private Long tableId;
    private String tableNumber;
    private String status;
    private Double subtotal;
    private Double tax;
    private Double total;
    private LocalDateTime createdAt;
    private List<OrderItemResponse> items;
}

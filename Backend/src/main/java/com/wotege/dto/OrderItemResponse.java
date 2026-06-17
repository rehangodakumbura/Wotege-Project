package com.wotege.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemResponse {

    private Long id;
    private Long menuItemId;
    private String menuItemName;
    private String imageUrl;
    private Integer quantity;
    private Double price;
    private Double totalPrice;
}

package com.wotege.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockUpdateDTO {

    @NotNull(message = "Quantity is required")
    private Double quantity;
}

package com.wotege.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TableRequest {

    @NotBlank(message = "Table number is required")
    private String tableNumber;
}

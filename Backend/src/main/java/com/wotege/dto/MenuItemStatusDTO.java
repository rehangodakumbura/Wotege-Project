package com.wotege.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemStatusDTO {

    @NotNull(message = "Availability status is required")
    private Boolean available;
}

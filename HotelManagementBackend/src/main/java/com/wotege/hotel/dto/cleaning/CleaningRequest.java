package com.wotege.hotel.dto.cleaning;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CleaningRequest {

    @NotNull(message = "Room ID is required")
    private Long roomId;

    private String assignedTo;
}

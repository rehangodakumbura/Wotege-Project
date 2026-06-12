package com.wotege.hotel.dto.room;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomResponse {

    private Long id;
    private String roomNumber;
    private String roomType;
    private String description;
    private Double pricePerNight;
    private Integer bedCount;
    private Boolean hasSeaView;
    private Boolean hasBalcony;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

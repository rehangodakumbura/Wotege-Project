package com.wotege.hotel.mapper;

import com.wotege.hotel.dto.room.RoomRequest;
import com.wotege.hotel.dto.room.RoomResponse;
import com.wotege.hotel.entity.Room;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoomMapper {

    Room toEntity(RoomRequest request);

    RoomResponse toResponse(Room room);
}

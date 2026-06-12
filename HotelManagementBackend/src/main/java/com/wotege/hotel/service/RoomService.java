package com.wotege.hotel.service;

import com.wotege.hotel.dto.room.RoomRequest;
import com.wotege.hotel.dto.room.RoomResponse;
import com.wotege.hotel.entity.Room;

import java.util.List;

public interface RoomService {

    List<RoomResponse> getAllRooms();

    RoomResponse getRoomById(Long id);

    RoomResponse createRoom(RoomRequest request);

    RoomResponse updateRoom(Long id, RoomRequest request);

    void deleteRoom(Long id);

    List<RoomResponse> getRoomsByStatus(String status);

    List<RoomResponse> searchRooms(String keyword);

    Room updateRoomStatus(Long id, Room.RoomStatus roomStatus);
}

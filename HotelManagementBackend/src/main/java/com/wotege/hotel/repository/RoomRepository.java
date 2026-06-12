package com.wotege.hotel.repository;

import com.wotege.hotel.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    List<Room> findByStatus(Room.RoomStatus status);

    Room findByRoomNumber(String roomNumber);

    boolean existsByRoomNumber(String roomNumber);

    @Query("SELECT r FROM Room r WHERE LOWER(r.roomNumber) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(r.roomType) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(r.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Room> search(@Param("keyword") String keyword);

    long countByStatus(Room.RoomStatus status);
}

package com.example.demo.room;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoomRepository extends JpaRepository<Room, Long> {

    List<Room> findByStatus(RoomStatus status);

    @Query("SELECT r FROM Room r WHERE r.status = 'AVAILABLE' AND (:type IS NULL OR " +
           "LOWER(r.roomType.name) LIKE LOWER(CONCAT('%', :type, '%')) " +
           "OR LOWER(r.roomType.code) LIKE LOWER(CONCAT('%', :type, '%')))")
    List<Room> findAvailableByType(@Param("type") String type);
}

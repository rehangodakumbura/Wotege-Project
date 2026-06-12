package com.example.demo.room;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomStatusHistoryRepository extends JpaRepository<RoomStatusHistory, Long> {

    List<RoomStatusHistory> findByRoomIdOrderByChangedAtDesc(Long roomId);

    List<RoomStatusHistory> findTop50ByOrderByChangedAtDesc();
}

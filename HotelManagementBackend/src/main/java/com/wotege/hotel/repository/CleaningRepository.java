package com.wotege.hotel.repository;

import com.wotege.hotel.entity.Cleaning;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CleaningRepository extends JpaRepository<Cleaning, Long> {

    List<Cleaning> findByRoomId(Long roomId);

    List<Cleaning> findByStatus(Cleaning.CleaningStatus status);

    List<Cleaning> findByAssignedToIgnoreCase(String assignedTo);
}

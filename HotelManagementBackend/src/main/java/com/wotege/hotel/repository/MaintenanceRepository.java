package com.wotege.hotel.repository;

import com.wotege.hotel.entity.Maintenance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaintenanceRepository extends JpaRepository<Maintenance, Long> {

    List<Maintenance> findByRoomId(Long roomId);

    List<Maintenance> findByStatus(Maintenance.MaintenanceStatus status);
}

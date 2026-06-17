package com.wotege.repository;

import com.wotege.entity.SystemSettingsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SettingsRepository extends JpaRepository<SystemSettingsEntity, Long> {

    Optional<SystemSettingsEntity> findTopByOrderByIdAsc();
}

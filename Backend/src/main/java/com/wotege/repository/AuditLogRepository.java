package com.wotege.repository;

import com.wotege.entity.AuditLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLogEntity, Long> {

    List<AuditLogEntity> findByRoleIdOrderByCreatedAtDesc(Long roleId);

    List<AuditLogEntity> findAllByOrderByCreatedAtDesc();
}

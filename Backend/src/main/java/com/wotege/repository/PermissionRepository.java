package com.wotege.repository;

import com.wotege.entity.PermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionRepository extends JpaRepository<PermissionEntity, Long> {

    List<PermissionEntity> findByRoleId(Long roleId);

    void deleteByRoleId(Long roleId);
}

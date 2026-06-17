package com.wotege.repository;

import com.wotege.entity.RoleEntity;
import com.wotege.enums.RoleStatus;
import com.wotege.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    Optional<RoleEntity> findByRoleName(String roleName);

    List<RoleEntity> findByStatus(RoleStatus status);

    List<RoleEntity> findByRoleType(RoleType roleType);

    Optional<RoleEntity> findByRoleCode(String roleCode);

    boolean existsByRoleName(String roleName);
}

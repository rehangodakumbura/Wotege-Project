package com.wotege.service;

import com.wotege.dto.AuditLogDTO;
import com.wotege.dto.PermissionDTO;
import com.wotege.dto.RoleRequestDTO;
import com.wotege.dto.RoleResponseDTO;
import com.wotege.enums.RoleStatus;

import java.util.List;

public interface RoleService {

    RoleResponseDTO createRole(RoleRequestDTO request);

    List<RoleResponseDTO> getAllRoles();

    RoleResponseDTO getRoleById(Long id);

    RoleResponseDTO updateRole(Long id, RoleRequestDTO request);

    void deleteRole(Long id);

    RoleResponseDTO changeRoleStatus(Long id, RoleStatus status);

    List<PermissionDTO> getPermissionsByRole(Long roleId);

    List<PermissionDTO> updatePermissions(Long roleId, List<PermissionDTO> permissions);

    List<AuditLogDTO> getAuditLogs(Long roleId);
}

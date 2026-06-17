package com.wotege.service.impl;

import com.wotege.dto.PermissionDTO;
import com.wotege.entity.AuditLogEntity;
import com.wotege.entity.PermissionEntity;
import com.wotege.entity.RoleEntity;
import com.wotege.exception.ResourceNotFoundException;
import com.wotege.repository.AuditLogRepository;
import com.wotege.repository.PermissionRepository;
import com.wotege.repository.RoleRepository;
import com.wotege.service.PermissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;
    private final AuditLogRepository auditLogRepository;

    @Override
    public List<PermissionDTO> getPermissionsByRole(Long roleId) {
        return permissionRepository.findByRoleId(roleId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<PermissionDTO> updatePermissions(Long roleId, List<PermissionDTO> permissions) {
        RoleEntity role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + roleId));

        List<PermissionEntity> existing = permissionRepository.findByRoleId(roleId);
        permissionRepository.deleteByRoleId(roleId);

        List<PermissionEntity> updated = permissions.stream()
                .map(dto -> PermissionEntity.builder()
                        .role(role)
                        .moduleName(dto.getModuleName())
                        .canView(dto.isCanView())
                        .canCreate(dto.isCanCreate())
                        .canEdit(dto.isCanEdit())
                        .canDelete(dto.isCanDelete())
                        .build())
                .collect(Collectors.toList());

        permissionRepository.saveAll(updated);

        StringBuilder desc = new StringBuilder();
        for (PermissionDTO dto : permissions) {
            PermissionEntity prev = existing.stream()
                    .filter(p -> p.getModuleName().equals(dto.getModuleName()))
                    .findFirst().orElse(null);
            if (prev != null && (!prev.getCanView().equals(dto.isCanView())
                    || !prev.getCanCreate().equals(dto.isCanCreate())
                    || !prev.getCanEdit().equals(dto.isCanEdit())
                    || !prev.getCanDelete().equals(dto.isCanDelete()))) {
                if (desc.length() > 0) desc.append("; ");
                desc.append("Changed ").append(dto.getModuleName()).append(" permissions");
            }
        }

        if (desc.length() > 0) {
            auditLogRepository.save(AuditLogEntity.builder()
                    .roleId(roleId)
                    .action("UPDATED_PERMISSION")
                    .performedBy("System")
                    .description(desc.toString())
                    .build());
        }

        log.info("Permissions updated for role: {}", role.getRoleName());
        return permissions;
    }

    private PermissionDTO mapToDTO(PermissionEntity entity) {
        return PermissionDTO.builder()
                .moduleName(entity.getModuleName())
                .canView(entity.getCanView() != null && entity.getCanView())
                .canCreate(entity.getCanCreate() != null && entity.getCanCreate())
                .canEdit(entity.getCanEdit() != null && entity.getCanEdit())
                .canDelete(entity.getCanDelete() != null && entity.getCanDelete())
                .build();
    }
}

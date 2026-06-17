package com.wotege.service.impl;

import com.wotege.dto.AuditLogDTO;
import com.wotege.dto.PermissionDTO;
import com.wotege.dto.RoleRequestDTO;
import com.wotege.dto.RoleResponseDTO;
import com.wotege.entity.AuditLogEntity;
import com.wotege.entity.PermissionEntity;
import com.wotege.entity.RoleEntity;
import com.wotege.enums.RoleStatus;
import com.wotege.enums.RoleType;
import com.wotege.exception.DuplicateResourceException;
import com.wotege.exception.ResourceNotFoundException;
import com.wotege.repository.AuditLogRepository;
import com.wotege.repository.PermissionRepository;
import com.wotege.repository.RoleRepository;
import com.wotege.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleServiceImpl implements RoleService {

    private static final List<String> DEFAULT_MODULES = List.of(
            "Dashboard Analytics",
            "Reservation Module",
            "Room Management",
            "Restaurant POS",
            "Menu Management",
            "Customer Database",
            "Staff Management",
            "Inventory System",
            "Reports & Insights",
            "System Settings"
    );

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final AuditLogRepository auditLogRepository;

    @Override
    @Transactional
    public RoleResponseDTO createRole(RoleRequestDTO request) {
        if (roleRepository.existsByRoleName(request.getRoleName())) {
            throw new DuplicateResourceException("Role with name '" + request.getRoleName() + "' already exists");
        }

        String roleCode = generateRoleCode();

        RoleEntity role = RoleEntity.builder()
                .roleCode(roleCode)
                .roleName(request.getRoleName())
                .description(request.getDescription())
                .department(request.getDepartment())
                .roleType(request.getRoleType() != null ? request.getRoleType() : RoleType.CUSTOM_ROLE)
                .status(request.getStatus() != null ? request.getStatus() : RoleStatus.ACTIVE)
                .userCount(0)
                .build();

        RoleEntity savedRole = roleRepository.save(role);
        createDefaultPermissions(savedRole);

        log.info("Created role: {} ({})", savedRole.getRoleName(), savedRole.getRoleCode());
        return mapToResponseDTO(savedRole);
    }

    @Override
    public List<RoleResponseDTO> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .sorted(Comparator.comparing(RoleResponseDTO::getId))
                .collect(Collectors.toList());
    }

    @Override
    public RoleResponseDTO getRoleById(Long id) {
        RoleEntity role = findRoleById(id);
        return mapToResponseDTO(role);
    }

    @Override
    @Transactional
    public RoleResponseDTO updateRole(Long id, RoleRequestDTO request) {
        RoleEntity role = findRoleById(id);

        if (!role.getRoleName().equals(request.getRoleName())
                && roleRepository.existsByRoleName(request.getRoleName())) {
            throw new DuplicateResourceException("Role with name '" + request.getRoleName() + "' already exists");
        }

        role.setRoleName(request.getRoleName());
        role.setDescription(request.getDescription());
        role.setDepartment(request.getDepartment());

        if (request.getStatus() != null) {
            role.setStatus(request.getStatus());
        }

        RoleEntity savedRole = roleRepository.save(role);
        log.info("Updated role: {} ({})", savedRole.getRoleName(), savedRole.getRoleCode());
        return mapToResponseDTO(savedRole);
    }

    @Override
    @Transactional
    public void deleteRole(Long id) {
        RoleEntity role = findRoleById(id);

        if (role.getRoleType() == RoleType.SYSTEM_ROLE) {
            throw new IllegalStateException("Cannot delete system role: " + role.getRoleName());
        }

        permissionRepository.deleteByRoleId(id);
        roleRepository.delete(role);
        log.info("Deleted role: {} ({})", role.getRoleName(), role.getRoleCode());
    }

    @Override
    @Transactional
    public RoleResponseDTO changeRoleStatus(Long id, RoleStatus status) {
        RoleEntity role = findRoleById(id);
        role.setStatus(status);
        RoleEntity savedRole = roleRepository.save(role);

        auditLogRepository.save(AuditLogEntity.builder()
                .roleId(id)
                .action("STATUS_CHANGED")
                .performedBy("System")
                .description("Role status changed to " + status)
                .build());

        log.info("Changed role {} status to {}", role.getRoleName(), status);
        return mapToResponseDTO(savedRole);
    }

    @Override
    public List<PermissionDTO> getPermissionsByRole(Long roleId) {
        return permissionRepository.findByRoleId(roleId).stream()
                .map(this::mapToPermissionDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<PermissionDTO> updatePermissions(Long roleId, List<PermissionDTO> permissions) {
        RoleEntity role = findRoleById(roleId);

        List<PermissionEntity> existingPermissions = permissionRepository.findByRoleId(roleId);
        permissionRepository.deleteByRoleId(roleId);

        List<PermissionEntity> updatedPermissions = permissions.stream()
                .map(dto -> PermissionEntity.builder()
                        .role(role)
                        .moduleName(dto.getModuleName())
                        .canView(dto.isCanView())
                        .canCreate(dto.isCanCreate())
                        .canEdit(dto.isCanEdit())
                        .canDelete(dto.isCanDelete())
                        .build())
                .collect(Collectors.toList());

        permissionRepository.saveAll(updatedPermissions);

        StringBuilder auditDescription = new StringBuilder();
        for (PermissionDTO dto : permissions) {
            PermissionEntity existing = existingPermissions.stream()
                    .filter(p -> p.getModuleName().equals(dto.getModuleName()))
                    .findFirst().orElse(null);

            if (existing != null && (!existing.getCanView().equals(dto.isCanView())
                    || !existing.getCanCreate().equals(dto.isCanCreate())
                    || !existing.getCanEdit().equals(dto.isCanEdit())
                    || !existing.getCanDelete().equals(dto.isCanDelete()))) {
                if (auditDescription.length() > 0) auditDescription.append("; ");
                auditDescription.append("Changed ").append(dto.getModuleName()).append(" permissions");
            }
        }

        if (auditDescription.length() > 0) {
            auditLogRepository.save(AuditLogEntity.builder()
                    .roleId(roleId)
                    .action("UPDATED_PERMISSION")
                    .performedBy("System")
                    .description(auditDescription.toString())
                    .build());
        }

        log.info("Updated permissions for role: {}", role.getRoleName());
        return permissions;
    }

    @Override
    public List<AuditLogDTO> getAuditLogs(Long roleId) {
        return auditLogRepository.findByRoleIdOrderByCreatedAtDesc(roleId).stream()
                .map(this::mapToAuditLogDTO)
                .collect(Collectors.toList());
    }

    private RoleEntity findRoleById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + id));
    }

    private String generateRoleCode() {
        long count = roleRepository.count() + 1;
        return String.format("R-%03d", count);
    }

    private void createDefaultPermissions(RoleEntity role) {
        List<PermissionEntity> permissions = DEFAULT_MODULES.stream()
                .map(module -> PermissionEntity.builder()
                        .role(role)
                        .moduleName(module)
                        .canView(false)
                        .canCreate(false)
                        .canEdit(false)
                        .canDelete(false)
                        .build())
                .collect(Collectors.toList());
        permissionRepository.saveAll(permissions);
    }

    private RoleResponseDTO mapToResponseDTO(RoleEntity role) {
        List<PermissionDTO> permissionDTOs = role.getPermissions() != null
                ? role.getPermissions().stream().map(this::mapToPermissionDTO).collect(Collectors.toList())
                : new ArrayList<>();

        return RoleResponseDTO.builder()
                .id(role.getId())
                .roleCode(role.getRoleCode())
                .roleName(role.getRoleName())
                .description(role.getDescription())
                .department(role.getDepartment())
                .roleType(role.getRoleType())
                .status(role.getStatus())
                .userCount(role.getUserCount())
                .permissions(permissionDTOs)
                .createdAt(role.getCreatedAt())
                .updatedAt(role.getUpdatedAt())
                .build();
    }

    private PermissionDTO mapToPermissionDTO(PermissionEntity entity) {
        return PermissionDTO.builder()
                .moduleName(entity.getModuleName())
                .canView(entity.getCanView() != null && entity.getCanView())
                .canCreate(entity.getCanCreate() != null && entity.getCanCreate())
                .canEdit(entity.getCanEdit() != null && entity.getCanEdit())
                .canDelete(entity.getCanDelete() != null && entity.getCanDelete())
                .build();
    }

    private AuditLogDTO mapToAuditLogDTO(AuditLogEntity entity) {
        return AuditLogDTO.builder()
                .id(entity.getId())
                .roleId(entity.getRoleId())
                .action(entity.getAction())
                .performedBy(entity.getPerformedBy())
                .description(entity.getDescription())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}

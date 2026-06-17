package com.wotege.dto;

import com.wotege.enums.Department;
import com.wotege.enums.RoleStatus;
import com.wotege.enums.RoleType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleResponseDTO {

    private Long id;
    private String roleCode;
    private String roleName;
    private String description;
    private Department department;
    private RoleType roleType;
    private RoleStatus status;
    private Integer userCount;
    private List<PermissionDTO> permissions;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

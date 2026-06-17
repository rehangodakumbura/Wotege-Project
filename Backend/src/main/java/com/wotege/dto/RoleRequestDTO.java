package com.wotege.dto;

import com.wotege.enums.Department;
import com.wotege.enums.RoleStatus;
import com.wotege.enums.RoleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleRequestDTO {

    @NotBlank(message = "Role name is required")
    private String roleName;

    private String description;

    @NotNull(message = "Department is required")
    private Department department;

    private RoleType roleType;

    private RoleStatus status;
}

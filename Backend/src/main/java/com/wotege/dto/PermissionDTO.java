package com.wotege.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PermissionDTO {

    @NotBlank(message = "Module name is required")
    private String moduleName;

    private boolean canView;
    private boolean canCreate;
    private boolean canEdit;
    private boolean canDelete;
}

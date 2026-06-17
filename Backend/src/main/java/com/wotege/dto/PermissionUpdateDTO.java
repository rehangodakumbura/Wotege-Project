package com.wotege.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PermissionUpdateDTO {

    @NotEmpty(message = "Permissions list cannot be empty")
    @Valid
    private List<PermissionDTO> permissions;
}

package com.wotege.controller;

import com.wotege.dto.PermissionDTO;
import com.wotege.service.PermissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/security/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    @GetMapping("/role/{roleId}")
    public ResponseEntity<List<PermissionDTO>> getPermissionsByRole(@PathVariable Long roleId) {
        return ResponseEntity.ok(permissionService.getPermissionsByRole(roleId));
    }

    @PutMapping("/role/{roleId}")
    public ResponseEntity<List<PermissionDTO>> updatePermissions(@PathVariable Long roleId,
                                                                  @Valid @RequestBody List<PermissionDTO> permissions) {
        return ResponseEntity.ok(permissionService.updatePermissions(roleId, permissions));
    }
}

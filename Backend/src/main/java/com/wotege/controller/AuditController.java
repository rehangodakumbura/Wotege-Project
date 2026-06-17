package com.wotege.controller;

import com.wotege.dto.AuditLogDTO;
import com.wotege.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/security/audit")
@RequiredArgsConstructor
public class AuditController {

    private final RoleService roleService;

    @GetMapping("/role/{roleId}")
    public ResponseEntity<List<AuditLogDTO>> getAuditLogs(@PathVariable Long roleId) {
        return ResponseEntity.ok(roleService.getAuditLogs(roleId));
    }
}

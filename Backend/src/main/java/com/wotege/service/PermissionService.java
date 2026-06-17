package com.wotege.service;

import com.wotege.dto.PermissionDTO;

import java.util.List;

public interface PermissionService {

    List<PermissionDTO> getPermissionsByRole(Long roleId);

    List<PermissionDTO> updatePermissions(Long roleId, List<PermissionDTO> permissions);
}

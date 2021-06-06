package com.vts.clientcenter.service;

import com.vts.clientcenter.service.dto.*;

import java.util.List;

public interface RolePermissionExtensionService {
    RolePermissionDTO save(RolePermissionDTO rolePermissionDTO);

    EditPermissionResponseDto saveDetail(EditPermissionRequestDto dto);

    EditPermissionResponseDto getDetailRole(String roleName);

    EditPermissionResponseDto createRole(EditPermissionRequestDto dto);

    void removeDetailRole(String roleName);

    UserDTO assignRoleForUser(String roleName, String userId);

    List<UserDTO> assignRoleForUsers(String roleName, List<String> userIds);

    UserRolePermissionResponse getRoleWithPermissions(String roleName);
}

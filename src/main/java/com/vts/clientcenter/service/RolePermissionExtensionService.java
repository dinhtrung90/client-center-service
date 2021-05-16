package com.vts.clientcenter.service;

import com.vts.clientcenter.service.dto.EditPermissionRequestDto;
import com.vts.clientcenter.service.dto.EditPermissionResponseDto;
import com.vts.clientcenter.service.dto.RolePermissionDTO;

public interface RolePermissionExtensionService {
    RolePermissionDTO save(RolePermissionDTO rolePermissionDTO);

    EditPermissionResponseDto saveDetail(EditPermissionRequestDto dto);

    EditPermissionResponseDto getDetailRole(String roleName);
}

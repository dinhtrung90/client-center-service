package com.vts.clientcenter.service.dto;

import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleDetailResponse {

    private String roleName;

    private String description;

    private boolean isCompositeRole;

    private List<String> effectiveRoles;

    private List<PermissionDTO> availablePrivileges;

    private boolean success;
}

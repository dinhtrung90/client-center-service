package com.vts.clientcenter.service.dto;

import com.vts.clientcenter.service.PermissionDetailDto;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRolePermissionResponse {
    private String roleName;
    private List<PermissionDetailDto> permissionDetails;

}

package com.vts.clientcenter.service.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAuthorizedResponseDto {

    private UserDTO userDto;

    private List<UserRolePermissionResponse> rolePermissions;
}

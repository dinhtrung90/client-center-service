package com.vts.clientcenter.service.dto;

import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleMappingRequest {

    private List<String> assignRoles;

    private List<ClientAppDto> accessibleApps;
}

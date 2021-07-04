package com.vts.clientcenter.service.dto;

import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleMappingResponse {

    private String userId;

    private List<String> effectiveRoles;

}

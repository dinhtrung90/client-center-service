package com.vts.clientcenter.service.dto;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssignUserRequest {

    private String userId;

    private String organizationId;
}

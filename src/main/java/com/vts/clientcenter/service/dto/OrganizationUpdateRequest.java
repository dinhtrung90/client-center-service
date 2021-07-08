package com.vts.clientcenter.service.dto;

import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrganizationUpdateRequest {

    private String uuid;

    private String displayName;

    private String description;

    private List<OrganizationBrandDTO> brands;

    private List<OrganizationGroupDTO> groups;

}

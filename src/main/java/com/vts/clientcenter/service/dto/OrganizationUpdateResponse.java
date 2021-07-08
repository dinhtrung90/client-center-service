package com.vts.clientcenter.service.dto;

import com.vts.clientcenter.domain.OrganizationGroup;
import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrganizationUpdateResponse {

    private String organizationId;

    private String displayName;

    private String description;

    private List<OrganizationBrandDTO> brands;

    private List<OrganizationGroupDTO> groups;
}

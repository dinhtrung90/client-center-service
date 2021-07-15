package com.vts.clientcenter.service.dto;

import com.vts.clientcenter.domain.OrganizationGroup;
import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrganizationFullResponse {

    private String id;

    private String displayName;

    private String name;

    private String description;

    private String email;

    private String phone;

    private boolean isEnabled;

    private List<OrganizationBrandDTO> brands;

    private List<OrganizationGroupDTO> groups;

    private List<AuthorityDto> roles;

}

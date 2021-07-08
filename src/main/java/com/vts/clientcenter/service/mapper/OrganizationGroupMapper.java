package com.vts.clientcenter.service.mapper;


import com.vts.clientcenter.domain.*;
import com.vts.clientcenter.service.dto.OrganizationGroupDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link OrganizationGroup} and its DTO {@link OrganizationGroupDTO}.
 */
@Mapper(componentModel = "spring", uses = {OrganizationMapper.class})
public interface OrganizationGroupMapper extends EntityMapper<OrganizationGroupDTO, OrganizationGroup> {

    @Mapping(source = "organization.id", target = "organizationId")
    OrganizationGroupDTO toDto(OrganizationGroup organizationGroup);

    @Mapping(source = "organizationId", target = "organization")
    OrganizationGroup toEntity(OrganizationGroupDTO organizationGroupDTO);

    default OrganizationGroup fromId(Long id) {
        if (id == null) {
            return null;
        }
        OrganizationGroup organizationGroup = new OrganizationGroup();
        organizationGroup.setId(id);
        return organizationGroup;
    }
}

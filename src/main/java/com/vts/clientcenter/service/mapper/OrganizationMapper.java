package com.vts.clientcenter.service.mapper;


import com.vts.clientcenter.domain.*;
import com.vts.clientcenter.service.dto.OrganizationDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Organization} and its DTO {@link OrganizationDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface OrganizationMapper extends EntityMapper<OrganizationDTO, Organization> {


    @Mapping(target = "userAddresses", ignore = true)
    @Mapping(target = "removeUserAddress", ignore = true)
    @Mapping(target = "organizationBrands", ignore = true)
    @Mapping(target = "removeOrganizationBrand", ignore = true)
    @Mapping(target = "organizationGroups", ignore = true)
    @Mapping(target = "removeOrganizationGroup", ignore = true)
    Organization toEntity(OrganizationDTO organizationDTO);

    default Organization fromId(Long id) {
        if (id == null) {
            return null;
        }
        Organization organization = new Organization();
        organization.setId(id);
        return organization;
    }
}

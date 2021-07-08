package com.vts.clientcenter.service.mapper;


import com.vts.clientcenter.domain.*;
import com.vts.clientcenter.service.dto.OrganizationBrandDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link OrganizationBrand} and its DTO {@link OrganizationBrandDTO}.
 */
@Mapper(componentModel = "spring", uses = {OrganizationMapper.class})
public interface OrganizationBrandMapper extends EntityMapper<OrganizationBrandDTO, OrganizationBrand> {

    @Mapping(source = "organization.id", target = "organizationId")
    OrganizationBrandDTO toDto(OrganizationBrand organizationBrand);

    @Mapping(source = "organizationId", target = "organization")
    OrganizationBrand toEntity(OrganizationBrandDTO organizationBrandDTO);

    default OrganizationBrand fromId(Long id) {
        if (id == null) {
            return null;
        }
        OrganizationBrand organizationBrand = new OrganizationBrand();
        organizationBrand.setId(id);
        return organizationBrand;
    }
}

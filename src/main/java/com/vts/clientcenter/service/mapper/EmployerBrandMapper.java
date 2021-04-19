package com.vts.clientcenter.service.mapper;


import com.vts.clientcenter.domain.*;
import com.vts.clientcenter.service.dto.EmployerBrandDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link EmployerBrand} and its DTO {@link EmployerBrandDTO}.
 */
@Mapper(componentModel = "spring", uses = {EmployerMapper.class})
public interface EmployerBrandMapper extends EntityMapper<EmployerBrandDTO, EmployerBrand> {

    @Mapping(source = "employer.id", target = "employerId")
    EmployerBrandDTO toDto(EmployerBrand employerBrand);

    @Mapping(source = "employerId", target = "employer")
    EmployerBrand toEntity(EmployerBrandDTO employerBrandDTO);

    default EmployerBrand fromId(Long id) {
        if (id == null) {
            return null;
        }
        EmployerBrand employerBrand = new EmployerBrand();
        employerBrand.setId(id);
        return employerBrand;
    }
}

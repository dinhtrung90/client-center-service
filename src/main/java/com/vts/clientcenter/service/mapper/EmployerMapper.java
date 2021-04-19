package com.vts.clientcenter.service.mapper;


import com.vts.clientcenter.domain.*;
import com.vts.clientcenter.service.dto.EmployerDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Employer} and its DTO {@link EmployerDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface EmployerMapper extends EntityMapper<EmployerDTO, Employer> {


    @Mapping(target = "employerDepartments", ignore = true)
    @Mapping(target = "removeEmployerDepartment", ignore = true)
    @Mapping(target = "employees", ignore = true)
    @Mapping(target = "removeEmployee", ignore = true)
    @Mapping(target = "employerBrands", ignore = true)
    @Mapping(target = "removeEmployerBrand", ignore = true)
    Employer toEntity(EmployerDTO employerDTO);

    default Employer fromId(Long id) {
        if (id == null) {
            return null;
        }
        Employer employer = new Employer();
        employer.setId(id);
        return employer;
    }
}

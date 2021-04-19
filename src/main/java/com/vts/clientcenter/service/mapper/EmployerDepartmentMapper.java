package com.vts.clientcenter.service.mapper;


import com.vts.clientcenter.domain.*;
import com.vts.clientcenter.service.dto.EmployerDepartmentDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link EmployerDepartment} and its DTO {@link EmployerDepartmentDTO}.
 */
@Mapper(componentModel = "spring", uses = {EmployerMapper.class})
public interface EmployerDepartmentMapper extends EntityMapper<EmployerDepartmentDTO, EmployerDepartment> {

    @Mapping(source = "employer.id", target = "employerId")
    EmployerDepartmentDTO toDto(EmployerDepartment employerDepartment);

    @Mapping(source = "employerId", target = "employer")
    EmployerDepartment toEntity(EmployerDepartmentDTO employerDepartmentDTO);

    default EmployerDepartment fromId(Long id) {
        if (id == null) {
            return null;
        }
        EmployerDepartment employerDepartment = new EmployerDepartment();
        employerDepartment.setId(id);
        return employerDepartment;
    }
}

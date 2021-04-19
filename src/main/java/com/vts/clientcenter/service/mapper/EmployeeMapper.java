package com.vts.clientcenter.service.mapper;


import com.vts.clientcenter.domain.*;
import com.vts.clientcenter.service.dto.EmployeeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Employee} and its DTO {@link EmployeeDTO}.
 */
@Mapper(componentModel = "spring", uses = {EmployerMapper.class, EmployerDepartmentMapper.class})
public interface EmployeeMapper extends EntityMapper<EmployeeDTO, Employee> {

    @Mapping(source = "employer.id", target = "employerId")
    @Mapping(source = "employerDepartment.id", target = "employerDepartmentId")
    EmployeeDTO toDto(Employee employee);

    @Mapping(source = "employerId", target = "employer")
    @Mapping(source = "employerDepartmentId", target = "employerDepartment")
    Employee toEntity(EmployeeDTO employeeDTO);

    default Employee fromId(Long id) {
        if (id == null) {
            return null;
        }
        Employee employee = new Employee();
        employee.setId(id);
        return employee;
    }
}

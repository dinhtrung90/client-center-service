package com.vts.clientcenter.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class EmployerDepartmentMapperTest {

    private EmployerDepartmentMapper employerDepartmentMapper;

    @BeforeEach
    public void setUp() {
        employerDepartmentMapper = new EmployerDepartmentMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(employerDepartmentMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(employerDepartmentMapper.fromId(null)).isNull();
    }
}

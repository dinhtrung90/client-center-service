package com.vts.clientcenter.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EmployeeMapperTest {
    private EmployeeMapper employeeMapper;

    @BeforeEach
    public void setUp() {
        employeeMapper = new EmployeeMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(employeeMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(employeeMapper.fromId(null)).isNull();
    }
}

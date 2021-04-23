package com.vts.clientcenter.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class EmployerMapperTest {

    private EmployerMapper employerMapper;

    @BeforeEach
    public void setUp() {
        employerMapper = new EmployerMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(employerMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(employerMapper.fromId(null)).isNull();
    }
}

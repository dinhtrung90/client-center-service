package com.vts.clientcenter.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class EmployerBrandMapperTest {

    private EmployerBrandMapper employerBrandMapper;

    @BeforeEach
    public void setUp() {
        employerBrandMapper = new EmployerBrandMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(employerBrandMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(employerBrandMapper.fromId(null)).isNull();
    }
}

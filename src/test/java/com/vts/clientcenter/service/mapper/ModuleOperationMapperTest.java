package com.vts.clientcenter.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ModuleOperationMapperTest {
    private ModuleOperationMapper moduleOperationMapper;

    @BeforeEach
    public void setUp() {
        moduleOperationMapper = new ModuleOperationMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(moduleOperationMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(moduleOperationMapper.fromId(null)).isNull();
    }
}

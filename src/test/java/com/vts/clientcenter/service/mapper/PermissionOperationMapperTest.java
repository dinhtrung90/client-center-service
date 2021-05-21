package com.vts.clientcenter.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PermissionOperationMapperTest {
    private PermissionOperationMapper permissionOperationMapper;

    @BeforeEach
    public void setUp() {
        permissionOperationMapper = new PermissionOperationMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(permissionOperationMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(permissionOperationMapper.fromId(null)).isNull();
    }
}

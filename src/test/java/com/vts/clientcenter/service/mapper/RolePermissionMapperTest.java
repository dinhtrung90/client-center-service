package com.vts.clientcenter.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RolePermissionMapperTest {
    private RolePermissionMapper rolePermissionMapper;

    @BeforeEach
    public void setUp() {
        rolePermissionMapper = new RolePermissionMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(rolePermissionMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(rolePermissionMapper.fromId(null)).isNull();
    }
}

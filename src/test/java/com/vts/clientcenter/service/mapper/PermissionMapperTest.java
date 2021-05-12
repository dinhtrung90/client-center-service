package com.vts.clientcenter.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PermissionMapperTest {
    private PermissionMapper permissionMapper;

    @BeforeEach
    public void setUp() {
        permissionMapper = new PermissionMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(permissionMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(permissionMapper.fromId(null)).isNull();
    }
}

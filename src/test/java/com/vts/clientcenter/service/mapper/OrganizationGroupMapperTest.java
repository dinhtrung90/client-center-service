package com.vts.clientcenter.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class OrganizationGroupMapperTest {

    private OrganizationGroupMapper organizationGroupMapper;

    @BeforeEach
    public void setUp() {
        organizationGroupMapper = new OrganizationGroupMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(organizationGroupMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(organizationGroupMapper.fromId(null)).isNull();
    }
}

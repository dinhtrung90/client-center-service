package com.vts.clientcenter.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class OrganizationBrandMapperTest {

    private OrganizationBrandMapper organizationBrandMapper;

    @BeforeEach
    public void setUp() {
        organizationBrandMapper = new OrganizationBrandMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(organizationBrandMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(organizationBrandMapper.fromId(null)).isNull();
    }
}

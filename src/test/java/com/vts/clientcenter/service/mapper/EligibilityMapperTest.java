package com.vts.clientcenter.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class EligibilityMapperTest {

    private EligibilityMapper eligibilityMapper;

    @BeforeEach
    public void setUp() {
        eligibilityMapper = new EligibilityMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(eligibilityMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(eligibilityMapper.fromId(null)).isNull();
    }
}

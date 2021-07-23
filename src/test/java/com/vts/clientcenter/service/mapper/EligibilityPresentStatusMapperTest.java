package com.vts.clientcenter.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class EligibilityPresentStatusMapperTest {

    private EligibilityPresentStatusMapper eligibilityPresentStatusMapper;

    @BeforeEach
    public void setUp() {
        eligibilityPresentStatusMapper = new EligibilityPresentStatusMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(eligibilityPresentStatusMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(eligibilityPresentStatusMapper.fromId(null)).isNull();
    }
}

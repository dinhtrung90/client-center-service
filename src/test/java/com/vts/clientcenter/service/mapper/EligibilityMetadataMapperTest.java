package com.vts.clientcenter.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class EligibilityMetadataMapperTest {

    private EligibilityMetadataMapper eligibilityMetadataMapper;

    @BeforeEach
    public void setUp() {
        eligibilityMetadataMapper = new EligibilityMetadataMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
//        assertThat(eligibilityMetadataMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(eligibilityMetadataMapper.fromId(null)).isNull();
    }
}

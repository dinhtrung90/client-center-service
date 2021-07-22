package com.vts.clientcenter.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.vts.clientcenter.web.rest.TestUtil;

public class EligibilityMetadataDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EligibilityMetadataDTO.class);
        EligibilityMetadataDTO eligibilityMetadataDTO1 = new EligibilityMetadataDTO();
        eligibilityMetadataDTO1.setId("1L");
        EligibilityMetadataDTO eligibilityMetadataDTO2 = new EligibilityMetadataDTO();
        assertThat(eligibilityMetadataDTO1).isNotEqualTo(eligibilityMetadataDTO2);
        eligibilityMetadataDTO2.setId(eligibilityMetadataDTO1.getId());
        assertThat(eligibilityMetadataDTO1).isEqualTo(eligibilityMetadataDTO2);
        eligibilityMetadataDTO2.setId("2L");
        assertThat(eligibilityMetadataDTO1).isNotEqualTo(eligibilityMetadataDTO2);
        eligibilityMetadataDTO1.setId(null);
        assertThat(eligibilityMetadataDTO1).isNotEqualTo(eligibilityMetadataDTO2);
    }
}

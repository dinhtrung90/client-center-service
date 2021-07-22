package com.vts.clientcenter.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.vts.clientcenter.web.rest.TestUtil;

public class EligibilityMetadataTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EligibilityMetadata.class);
        EligibilityMetadata eligibilityMetadata1 = new EligibilityMetadata();
        eligibilityMetadata1.setId("1L");
        EligibilityMetadata eligibilityMetadata2 = new EligibilityMetadata();
        eligibilityMetadata2.setId(eligibilityMetadata1.getId());
        assertThat(eligibilityMetadata1).isEqualTo(eligibilityMetadata2);
        eligibilityMetadata2.setId("2L");
        assertThat(eligibilityMetadata1).isNotEqualTo(eligibilityMetadata2);
        eligibilityMetadata1.setId(null);
        assertThat(eligibilityMetadata1).isNotEqualTo(eligibilityMetadata2);
    }
}

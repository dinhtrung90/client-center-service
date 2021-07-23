package com.vts.clientcenter.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.vts.clientcenter.web.rest.TestUtil;

public class EligibilityPresentStatusTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EligibilityPresentStatus.class);
        EligibilityPresentStatus eligibilityPresentStatus1 = new EligibilityPresentStatus();
        eligibilityPresentStatus1.setId(1L);
        EligibilityPresentStatus eligibilityPresentStatus2 = new EligibilityPresentStatus();
        eligibilityPresentStatus2.setId(eligibilityPresentStatus1.getId());
        assertThat(eligibilityPresentStatus1).isEqualTo(eligibilityPresentStatus2);
        eligibilityPresentStatus2.setId(2L);
        assertThat(eligibilityPresentStatus1).isNotEqualTo(eligibilityPresentStatus2);
        eligibilityPresentStatus1.setId(null);
        assertThat(eligibilityPresentStatus1).isNotEqualTo(eligibilityPresentStatus2);
    }
}

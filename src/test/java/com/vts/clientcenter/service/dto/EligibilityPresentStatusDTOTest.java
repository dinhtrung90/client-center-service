package com.vts.clientcenter.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.vts.clientcenter.web.rest.TestUtil;

public class EligibilityPresentStatusDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EligibilityPresentStatusDTO.class);
        EligibilityPresentStatusDTO eligibilityPresentStatusDTO1 = new EligibilityPresentStatusDTO();
        eligibilityPresentStatusDTO1.setId(1L);
        EligibilityPresentStatusDTO eligibilityPresentStatusDTO2 = new EligibilityPresentStatusDTO();
        assertThat(eligibilityPresentStatusDTO1).isNotEqualTo(eligibilityPresentStatusDTO2);
        eligibilityPresentStatusDTO2.setId(eligibilityPresentStatusDTO1.getId());
        assertThat(eligibilityPresentStatusDTO1).isEqualTo(eligibilityPresentStatusDTO2);
        eligibilityPresentStatusDTO2.setId(2L);
        assertThat(eligibilityPresentStatusDTO1).isNotEqualTo(eligibilityPresentStatusDTO2);
        eligibilityPresentStatusDTO1.setId(null);
        assertThat(eligibilityPresentStatusDTO1).isNotEqualTo(eligibilityPresentStatusDTO2);
    }
}

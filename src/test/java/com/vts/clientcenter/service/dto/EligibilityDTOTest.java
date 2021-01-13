package com.vts.clientcenter.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.vts.clientcenter.web.rest.TestUtil;

public class EligibilityDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EligibilityDTO.class);
        EligibilityDTO eligibilityDTO1 = new EligibilityDTO();
        eligibilityDTO1.setId(1L);
        EligibilityDTO eligibilityDTO2 = new EligibilityDTO();
        assertThat(eligibilityDTO1).isNotEqualTo(eligibilityDTO2);
        eligibilityDTO2.setId(eligibilityDTO1.getId());
        assertThat(eligibilityDTO1).isEqualTo(eligibilityDTO2);
        eligibilityDTO2.setId(2L);
        assertThat(eligibilityDTO1).isNotEqualTo(eligibilityDTO2);
        eligibilityDTO1.setId(null);
        assertThat(eligibilityDTO1).isNotEqualTo(eligibilityDTO2);
    }
}

package com.vts.clientcenter.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.vts.clientcenter.web.rest.TestUtil;

public class EmployerBrandDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmployerBrandDTO.class);
        EmployerBrandDTO employerBrandDTO1 = new EmployerBrandDTO();
        employerBrandDTO1.setId(1L);
        EmployerBrandDTO employerBrandDTO2 = new EmployerBrandDTO();
        assertThat(employerBrandDTO1).isNotEqualTo(employerBrandDTO2);
        employerBrandDTO2.setId(employerBrandDTO1.getId());
        assertThat(employerBrandDTO1).isEqualTo(employerBrandDTO2);
        employerBrandDTO2.setId(2L);
        assertThat(employerBrandDTO1).isNotEqualTo(employerBrandDTO2);
        employerBrandDTO1.setId(null);
        assertThat(employerBrandDTO1).isNotEqualTo(employerBrandDTO2);
    }
}

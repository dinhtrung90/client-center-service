package com.vts.clientcenter.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.vts.clientcenter.web.rest.TestUtil;

public class EmployerDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmployerDTO.class);
        EmployerDTO employerDTO1 = new EmployerDTO();
        employerDTO1.setId(1L);
        EmployerDTO employerDTO2 = new EmployerDTO();
        assertThat(employerDTO1).isNotEqualTo(employerDTO2);
        employerDTO2.setId(employerDTO1.getId());
        assertThat(employerDTO1).isEqualTo(employerDTO2);
        employerDTO2.setId(2L);
        assertThat(employerDTO1).isNotEqualTo(employerDTO2);
        employerDTO1.setId(null);
        assertThat(employerDTO1).isNotEqualTo(employerDTO2);
    }
}

package com.vts.clientcenter.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.vts.clientcenter.web.rest.TestUtil;

public class EmployerDepartmentDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmployerDepartmentDTO.class);
        EmployerDepartmentDTO employerDepartmentDTO1 = new EmployerDepartmentDTO();
        employerDepartmentDTO1.setId(1L);
        EmployerDepartmentDTO employerDepartmentDTO2 = new EmployerDepartmentDTO();
        assertThat(employerDepartmentDTO1).isNotEqualTo(employerDepartmentDTO2);
        employerDepartmentDTO2.setId(employerDepartmentDTO1.getId());
        assertThat(employerDepartmentDTO1).isEqualTo(employerDepartmentDTO2);
        employerDepartmentDTO2.setId(2L);
        assertThat(employerDepartmentDTO1).isNotEqualTo(employerDepartmentDTO2);
        employerDepartmentDTO1.setId(null);
        assertThat(employerDepartmentDTO1).isNotEqualTo(employerDepartmentDTO2);
    }
}

package com.vts.clientcenter.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.vts.clientcenter.web.rest.TestUtil;

public class EmployerDepartmentTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmployerDepartment.class);
        EmployerDepartment employerDepartment1 = new EmployerDepartment();
        employerDepartment1.setId(1L);
        EmployerDepartment employerDepartment2 = new EmployerDepartment();
        employerDepartment2.setId(employerDepartment1.getId());
        assertThat(employerDepartment1).isEqualTo(employerDepartment2);
        employerDepartment2.setId(2L);
        assertThat(employerDepartment1).isNotEqualTo(employerDepartment2);
        employerDepartment1.setId(null);
        assertThat(employerDepartment1).isNotEqualTo(employerDepartment2);
    }
}

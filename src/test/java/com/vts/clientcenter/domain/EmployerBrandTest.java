package com.vts.clientcenter.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.vts.clientcenter.web.rest.TestUtil;

public class EmployerBrandTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmployerBrand.class);
        EmployerBrand employerBrand1 = new EmployerBrand();
        employerBrand1.setId(1L);
        EmployerBrand employerBrand2 = new EmployerBrand();
        employerBrand2.setId(employerBrand1.getId());
        assertThat(employerBrand1).isEqualTo(employerBrand2);
        employerBrand2.setId(2L);
        assertThat(employerBrand1).isNotEqualTo(employerBrand2);
        employerBrand1.setId(null);
        assertThat(employerBrand1).isNotEqualTo(employerBrand2);
    }
}

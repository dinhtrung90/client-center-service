package com.vts.clientcenter.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.vts.clientcenter.web.rest.TestUtil;

public class OrganizationBrandTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrganizationBrand.class);
        OrganizationBrand organizationBrand1 = new OrganizationBrand();
        organizationBrand1.setId(1L);
        OrganizationBrand organizationBrand2 = new OrganizationBrand();
        organizationBrand2.setId(organizationBrand1.getId());
        assertThat(organizationBrand1).isEqualTo(organizationBrand2);
        organizationBrand2.setId(2L);
        assertThat(organizationBrand1).isNotEqualTo(organizationBrand2);
        organizationBrand1.setId(null);
        assertThat(organizationBrand1).isNotEqualTo(organizationBrand2);
    }
}

package com.vts.clientcenter.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.vts.clientcenter.web.rest.TestUtil;

public class OrganizationBrandDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrganizationBrandDTO.class);
        OrganizationBrandDTO organizationBrandDTO1 = new OrganizationBrandDTO();
        organizationBrandDTO1.setId(1L);
        OrganizationBrandDTO organizationBrandDTO2 = new OrganizationBrandDTO();
        assertThat(organizationBrandDTO1).isNotEqualTo(organizationBrandDTO2);
        organizationBrandDTO2.setId(organizationBrandDTO1.getId());
        assertThat(organizationBrandDTO1).isEqualTo(organizationBrandDTO2);
        organizationBrandDTO2.setId(2L);
        assertThat(organizationBrandDTO1).isNotEqualTo(organizationBrandDTO2);
        organizationBrandDTO1.setId(null);
        assertThat(organizationBrandDTO1).isNotEqualTo(organizationBrandDTO2);
    }
}

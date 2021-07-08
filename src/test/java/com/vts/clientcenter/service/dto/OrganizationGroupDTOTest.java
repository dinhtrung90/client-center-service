package com.vts.clientcenter.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.vts.clientcenter.web.rest.TestUtil;

public class OrganizationGroupDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrganizationGroupDTO.class);
        OrganizationGroupDTO organizationGroupDTO1 = new OrganizationGroupDTO();
        organizationGroupDTO1.setId(1L);
        OrganizationGroupDTO organizationGroupDTO2 = new OrganizationGroupDTO();
        assertThat(organizationGroupDTO1).isNotEqualTo(organizationGroupDTO2);
        organizationGroupDTO2.setId(organizationGroupDTO1.getId());
        assertThat(organizationGroupDTO1).isEqualTo(organizationGroupDTO2);
        organizationGroupDTO2.setId(2L);
        assertThat(organizationGroupDTO1).isNotEqualTo(organizationGroupDTO2);
        organizationGroupDTO1.setId(null);
        assertThat(organizationGroupDTO1).isNotEqualTo(organizationGroupDTO2);
    }
}

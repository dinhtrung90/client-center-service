package com.vts.clientcenter.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.vts.clientcenter.web.rest.TestUtil;

public class OrganizationGroupTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrganizationGroup.class);
        OrganizationGroup organizationGroup1 = new OrganizationGroup();
        organizationGroup1.setId(1L);
        OrganizationGroup organizationGroup2 = new OrganizationGroup();
        organizationGroup2.setId(organizationGroup1.getId());
        assertThat(organizationGroup1).isEqualTo(organizationGroup2);
        organizationGroup2.setId(2L);
        assertThat(organizationGroup1).isNotEqualTo(organizationGroup2);
        organizationGroup1.setId(null);
        assertThat(organizationGroup1).isNotEqualTo(organizationGroup2);
    }
}

package com.vts.clientcenter.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.vts.clientcenter.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

public class PermissionOperationTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PermissionOperation.class);
        PermissionOperation permissionOperation1 = new PermissionOperation();
        permissionOperation1.setId(1L);
        PermissionOperation permissionOperation2 = new PermissionOperation();
        permissionOperation2.setId(permissionOperation1.getId());
        assertThat(permissionOperation1).isEqualTo(permissionOperation2);
        permissionOperation2.setId(2L);
        assertThat(permissionOperation1).isNotEqualTo(permissionOperation2);
        permissionOperation1.setId(null);
        assertThat(permissionOperation1).isNotEqualTo(permissionOperation2);
    }
}

package com.vts.clientcenter.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.vts.clientcenter.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

public class PermissionOperationDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PermissionOperationDTO.class);
        PermissionOperationDTO permissionOperationDTO1 = new PermissionOperationDTO();
        permissionOperationDTO1.setId(1L);
        PermissionOperationDTO permissionOperationDTO2 = new PermissionOperationDTO();
        assertThat(permissionOperationDTO1).isNotEqualTo(permissionOperationDTO2);
        permissionOperationDTO2.setId(permissionOperationDTO1.getId());
        assertThat(permissionOperationDTO1).isEqualTo(permissionOperationDTO2);
        permissionOperationDTO2.setId(2L);
        assertThat(permissionOperationDTO1).isNotEqualTo(permissionOperationDTO2);
        permissionOperationDTO1.setId(null);
        assertThat(permissionOperationDTO1).isNotEqualTo(permissionOperationDTO2);
    }
}

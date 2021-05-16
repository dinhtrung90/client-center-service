package com.vts.clientcenter.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.vts.clientcenter.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

public class ModuleOperationDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ModuleOperationDTO.class);
        ModuleOperationDTO moduleOperationDTO1 = new ModuleOperationDTO();
        moduleOperationDTO1.setId(1L);
        ModuleOperationDTO moduleOperationDTO2 = new ModuleOperationDTO();
        assertThat(moduleOperationDTO1).isNotEqualTo(moduleOperationDTO2);
        moduleOperationDTO2.setId(moduleOperationDTO1.getId());
        assertThat(moduleOperationDTO1).isEqualTo(moduleOperationDTO2);
        moduleOperationDTO2.setId(2L);
        assertThat(moduleOperationDTO1).isNotEqualTo(moduleOperationDTO2);
        moduleOperationDTO1.setId(null);
        assertThat(moduleOperationDTO1).isNotEqualTo(moduleOperationDTO2);
    }
}

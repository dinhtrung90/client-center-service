package com.vts.clientcenter.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.vts.clientcenter.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

public class ModuleOperationTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ModuleOperation.class);
        ModuleOperation moduleOperation1 = new ModuleOperation();
        moduleOperation1.setId(1L);
        ModuleOperation moduleOperation2 = new ModuleOperation();
        moduleOperation2.setId(moduleOperation1.getId());
        assertThat(moduleOperation1).isEqualTo(moduleOperation2);
        moduleOperation2.setId(2L);
        assertThat(moduleOperation1).isNotEqualTo(moduleOperation2);
        moduleOperation1.setId(null);
        assertThat(moduleOperation1).isNotEqualTo(moduleOperation2);
    }
}

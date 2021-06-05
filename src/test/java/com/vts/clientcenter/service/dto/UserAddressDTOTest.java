package com.vts.clientcenter.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.vts.clientcenter.web.rest.TestUtil;

public class UserAddressDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserAddressDTO.class);
        UserAddressDTO userAddressDTO1 = new UserAddressDTO();
        userAddressDTO1.setId(1L);
        UserAddressDTO userAddressDTO2 = new UserAddressDTO();
        assertThat(userAddressDTO1).isNotEqualTo(userAddressDTO2);
        userAddressDTO2.setId(userAddressDTO1.getId());
        assertThat(userAddressDTO1).isEqualTo(userAddressDTO2);
        userAddressDTO2.setId(2L);
        assertThat(userAddressDTO1).isNotEqualTo(userAddressDTO2);
        userAddressDTO1.setId(null);
        assertThat(userAddressDTO1).isNotEqualTo(userAddressDTO2);
    }
}

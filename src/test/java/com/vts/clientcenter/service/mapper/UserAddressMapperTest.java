package com.vts.clientcenter.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class UserAddressMapperTest {

    private UserAddressMapper userAddressMapper;

    @BeforeEach
    public void setUp() {
        userAddressMapper = new UserAddressMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(userAddressMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(userAddressMapper.fromId(null)).isNull();
    }
}

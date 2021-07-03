package com.vts.clientcenter.service.mapper;


import com.vts.clientcenter.domain.*;
import com.vts.clientcenter.service.dto.UserAddressDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserAddress} and its DTO {@link UserAddressDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface UserAddressMapper extends EntityMapper<UserAddressDTO, UserAddress> {

    @Mapping(target = "userId", source = "user.id")
    UserAddressDTO toDto(UserAddress entity);

    @Mapping(target = "user.id", source = "userId")
    UserAddress toEntity(UserAddressDTO dto);

    default UserAddress fromId(Long id) {
        if (id == null) {
            return null;
        }
        UserAddress userAddress = new UserAddress();
        userAddress.setId(id);
        return userAddress;
    }
}

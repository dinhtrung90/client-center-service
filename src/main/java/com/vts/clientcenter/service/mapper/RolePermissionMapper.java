package com.vts.clientcenter.service.mapper;

import com.vts.clientcenter.domain.*;
import com.vts.clientcenter.service.dto.RolePermissionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RolePermission} and its DTO {@link RolePermissionDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RolePermissionMapper extends EntityMapper<RolePermissionDTO, RolePermission> {
    default RolePermission fromId(Long id) {
        if (id == null) {
            return null;
        }
        RolePermission rolePermission = new RolePermission();
        rolePermission.setId(id);
        return rolePermission;
    }
}

package com.vts.clientcenter.service.mapper;

import com.vts.clientcenter.domain.*;
import com.vts.clientcenter.service.dto.PermissionOperationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PermissionOperation} and its DTO {@link PermissionOperationDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PermissionOperationMapper extends EntityMapper<PermissionOperationDTO, PermissionOperation> {
    default PermissionOperation fromId(Long id) {
        if (id == null) {
            return null;
        }
        PermissionOperation permissionOperation = new PermissionOperation();
        permissionOperation.setId(id);
        return permissionOperation;
    }
}

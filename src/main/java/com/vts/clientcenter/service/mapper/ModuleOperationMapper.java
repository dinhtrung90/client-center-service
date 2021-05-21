package com.vts.clientcenter.service.mapper;

import com.vts.clientcenter.domain.*;
import com.vts.clientcenter.service.dto.ModuleOperationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ModuleOperation} and its DTO {@link ModuleOperationDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ModuleOperationMapper extends EntityMapper<ModuleOperationDTO, ModuleOperation> {
    default ModuleOperation fromId(Long id) {
        if (id == null) {
            return null;
        }
        ModuleOperation moduleOperation = new ModuleOperation();
        moduleOperation.setId(id);
        return moduleOperation;
    }
}

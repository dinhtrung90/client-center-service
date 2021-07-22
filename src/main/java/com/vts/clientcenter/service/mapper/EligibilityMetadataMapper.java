package com.vts.clientcenter.service.mapper;


import com.vts.clientcenter.domain.*;
import com.vts.clientcenter.service.dto.EligibilityMetadataDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link EligibilityMetadata} and its DTO {@link EligibilityMetadataDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface EligibilityMetadataMapper extends EntityMapper<EligibilityMetadataDTO, EligibilityMetadata> {



    default EligibilityMetadata fromId(String id) {
        if (id == null) {
            return null;
        }
        EligibilityMetadata eligibilityMetadata = new EligibilityMetadata();
        eligibilityMetadata.setId(id);
        return eligibilityMetadata;
    }
}

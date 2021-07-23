package com.vts.clientcenter.service.mapper;


import com.vts.clientcenter.domain.*;
import com.vts.clientcenter.service.dto.EligibilityPresentStatusDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link EligibilityPresentStatus} and its DTO {@link EligibilityPresentStatusDTO}.
 */
@Mapper(componentModel = "spring", uses = {EligibilityMapper.class})
public interface EligibilityPresentStatusMapper extends EntityMapper<EligibilityPresentStatusDTO, EligibilityPresentStatus> {

    @Mapping(source = "eligibility.id", target = "eligibilityId")
    EligibilityPresentStatusDTO toDto(EligibilityPresentStatus eligibilityPresentStatus);

    @Mapping(source = "eligibilityId", target = "eligibility")
    EligibilityPresentStatus toEntity(EligibilityPresentStatusDTO eligibilityPresentStatusDTO);

    default EligibilityPresentStatus fromId(Long id) {
        if (id == null) {
            return null;
        }
        EligibilityPresentStatus eligibilityPresentStatus = new EligibilityPresentStatus();
        eligibilityPresentStatus.setId(id);
        return eligibilityPresentStatus;
    }
}

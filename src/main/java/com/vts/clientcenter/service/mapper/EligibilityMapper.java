package com.vts.clientcenter.service.mapper;


import com.vts.clientcenter.domain.*;
import com.vts.clientcenter.service.dto.EligibilityDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Eligibility} and its DTO {@link EligibilityDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface EligibilityMapper extends EntityMapper<EligibilityDTO, Eligibility> {



    default Eligibility fromId(String id) {
        if (id == null) {
            return null;
        }
        Eligibility eligibility = new Eligibility();
        eligibility.setId(id);
        return eligibility;
    }
}

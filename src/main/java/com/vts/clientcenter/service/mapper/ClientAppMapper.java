package com.vts.clientcenter.service.mapper;


import com.vts.clientcenter.domain.ClientApp;
import com.vts.clientcenter.domain.Eligibility;
import com.vts.clientcenter.service.dto.ClientAppDto;
import com.vts.clientcenter.service.dto.EligibilityDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link ClientApp} and its DTO {@link ClientAppDto}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ClientAppMapper extends EntityMapper<ClientAppDto, ClientApp> {



    default ClientApp fromId(String id) {
        if (id == null) {
            return null;
        }
        ClientApp eligibility = new ClientApp();
        eligibility.setId(id);
        return eligibility;
    }
}

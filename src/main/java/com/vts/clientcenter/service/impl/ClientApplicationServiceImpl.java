package com.vts.clientcenter.service.impl;

import com.vts.clientcenter.config.Constants;
import com.vts.clientcenter.config.KeycloakConfig;
import com.vts.clientcenter.domain.Authority;
import com.vts.clientcenter.domain.ClientApp;
import com.vts.clientcenter.repository.AuthorityRepository;
import com.vts.clientcenter.repository.ClientAppRepository;
import com.vts.clientcenter.service.ClientApplicationService;
import com.vts.clientcenter.service.dto.ClientAppDto;
import com.vts.clientcenter.service.keycloak.KeycloakFacade;
import com.vts.clientcenter.service.mapper.ClientAppMapper;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.vts.clientcenter.config.Constants.SYSTEM_ACCOUNT;

@Service
@Transactional
public class ClientApplicationServiceImpl implements ClientApplicationService {

    @Autowired
    private KeycloakConfig setting;

    @Autowired
    @Qualifier("keycloakFacade")
    private KeycloakFacade keycloakFacade;

    @Autowired
    private ClientAppRepository clientAppRepository;

    @Autowired
    private ClientAppMapper  clientAppMapper;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Override
    public List<ClientAppDto> getApplications() {

        List<ClientAppDto> clientAppDtoList =  keycloakFacade.getClientRepresentation(setting.getRealmApp())
            .stream()
            .map( cr -> {
               ClientAppDto clientAppDto = new ClientAppDto();
               clientAppDto.setId(cr.getId());
               clientAppDto.setName(cr.getClientId());
               clientAppDto.setDesc(cr.getDescription());

                List<RoleRepresentation> clientRoles = keycloakFacade.getClientRoles(setting.getRealmApp(), cr.getId());
                List<Authority> role_access = clientRoles.stream().filter(ccr -> ccr.getName().startsWith("ROLE_ACCESS"))
                    .map(u -> {
                        Optional<Authority> authorityOptional = authorityRepository.findById(u.getName());
                        Authority authority;
                        if (!authorityOptional.isPresent()) {
                            authority = new Authority();
                            authority.setName(u.getName());
                            authority.setDescription(u.getDescription());
                            authority.setCreatedBy(SYSTEM_ACCOUNT);
                            authority.setCreatedDate(Instant.now());
                            authority.setLastModifiedDate(Instant.now());
                            authority.setLastModifiedBy(SYSTEM_ACCOUNT);
                            authorityRepository.save(authority);
                            return authority;
                        } else {
                            authority = authorityOptional.get();
                        }
                        return authority;
                    }).collect(Collectors.toList());
                clientAppDto.setAuthorities(role_access);
                return clientAppDto;
            }).collect(Collectors.toList());

        List<String> collectOfIdClients = clientAppDtoList.stream().map(ClientAppDto::getId).collect(Collectors.toList());
        boolean allMatch = clientAppRepository.findByIdIn(collectOfIdClients)
            .noneMatch(c -> collectOfIdClients.contains(c.getId()));

        if (allMatch){
            List<ClientApp> clientApps = clientAppMapper.toEntity(clientAppDtoList);
            clientAppRepository.saveAll(clientApps);
        }

        return clientAppDtoList;

    }
}

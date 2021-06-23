package com.vts.clientcenter.service.impl;

import com.vts.clientcenter.config.KeycloakConfig;
import com.vts.clientcenter.domain.Authority;
import com.vts.clientcenter.repository.AuthorityRepository;
import com.vts.clientcenter.security.SecurityUtils;
import com.vts.clientcenter.service.AbstractBaseService;
import com.vts.clientcenter.service.AuthorityService;
import com.vts.clientcenter.service.UserService;
import com.vts.clientcenter.service.dto.AuthorityDto;
import com.vts.clientcenter.service.dto.CreateRoleRequest;
import com.vts.clientcenter.service.dto.RoleDetailResponse;
import com.vts.clientcenter.service.keycloak.KeycloakFacade;
import com.vts.clientcenter.service.mapper.AuthorityMapper;
import org.mapstruct.ap.internal.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.vts.clientcenter.config.Constants.SYSTEM_ACCOUNT;

@Service
@Transactional
public class AuthorityServiceImpl extends AbstractBaseService implements AuthorityService {


    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private KeycloakConfig setting;

    @Autowired
    @Qualifier("keycloakFacade")
    private KeycloakFacade keycloakFacade;

    @Autowired
    private AuthorityMapper authorityMapper;

    public AuthorityServiceImpl(UserService userService) {
        super(userService);
    }


    @Override
    public Page<AuthorityDto> getAuthorities(Pageable pageable) {

        Page<Authority> allAuthorities = authorityRepository.findAll(pageable);

        return allAuthorities.map(u -> authorityMapper.authorityToDto(u));
    }


    @Override
    public RoleDetailResponse save(CreateRoleRequest dto) {

        String currentUserLogin = SecurityUtils.getCurrentUserLogin().orElse(SYSTEM_ACCOUNT);

        Optional<Authority> authorityOptional = authorityRepository.findById(dto.getRoleName());

        if (!authorityOptional.isPresent()) {
            handleCreateRole(dto, currentUserLogin);
        } else {
            handleUpdateRole(dto, currentUserLogin, authorityOptional.get());

        }

        return RoleDetailResponse
            .builder()
            .effectiveRoles(dto.getEffectiveRoles())
            .availablePrivileges(dto.getAvailablePrivileges())
            .build();
    }

    private void handleCreateRole(CreateRoleRequest dto, String createdBy) {

        Authority authority = new Authority();
        authority.setName(dto.getRoleName());
        authority.setDescription(dto.getDescription());
        authority.setCreatedBy(createdBy);
        authority.setLastModifiedBy(createdBy);
        authority.setLastModifiedDate(Instant.now());

        Set<Authority> authorities = Collections.asSet(dto.getEffectiveRoles(), dto.getAvailablePrivileges()).stream()
            .map(u -> authorityRepository.getOne(u))
            .collect(Collectors.toSet());
        authority.setCompositeRoles(authorities);

        keycloakFacade.createWithCompositeRoles(dto, setting.getRealmApp());

        authorityRepository.save(authority);

    }

    private void handleUpdateRole(CreateRoleRequest dto, String updateBy, Authority authority) {

        authority.setDescription(dto.getDescription());
        authority.setLastModifiedBy(updateBy);
        authority.setLastModifiedDate(Instant.now());
        keycloakFacade.updateRole(authority.getName(), setting.getRealmApp(), authority);

        Set<Authority> authorities = Collections.asSet(dto.getEffectiveRoles(), dto.getAvailablePrivileges()).stream()
            .map(u -> authorityRepository.getOne(u))
            .collect(Collectors.toSet());

        authority.setCompositeRoles(authorities);

        keycloakFacade.updateWithCompositeRoles(dto, setting.getRealmApp());

        authority.addCompositeRoles(authorities);
        authorityRepository.save(authority);


    }
}

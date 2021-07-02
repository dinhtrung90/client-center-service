package com.vts.clientcenter.service.impl;

import com.vts.clientcenter.config.KeycloakConfig;
import com.vts.clientcenter.domain.Authority;
import com.vts.clientcenter.domain.Permission;
import com.vts.clientcenter.repository.AuthorityRepository;
import com.vts.clientcenter.security.SecurityUtils;
import com.vts.clientcenter.service.AbstractBaseService;
import com.vts.clientcenter.service.AuthorityService;
import com.vts.clientcenter.service.UserService;
import com.vts.clientcenter.service.dto.AuthorityDto;
import com.vts.clientcenter.service.dto.CreateRoleRequest;
import com.vts.clientcenter.service.dto.PermissionDTO;
import com.vts.clientcenter.service.dto.RoleDetailResponse;
import com.vts.clientcenter.service.keycloak.KeycloakFacade;
import com.vts.clientcenter.service.mapper.AuthorityMapper;
import com.vts.clientcenter.service.mapper.PermissionMapper;
import com.vts.clientcenter.service.mapper.PermissionMapperImpl;
import org.mapstruct.ap.internal.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
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

    @Autowired
    private PermissionMapper permissionMapper;

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
            .availablePrivileges(permissionMapper.toDto(new ArrayList<>(authorityRepository.getOne(dto.getRoleName()).getPermissions())))
            .build();
    }

    private void handleCreateRole(CreateRoleRequest dto, String createdBy) {

        Authority authority = new Authority();
        authority.setName(dto.getRoleName());
        authority.setDescription(dto.getDescription());
        authority.setCreatedBy(createdBy);
        authority.setLastModifiedBy(createdBy);
        authority.setLastModifiedDate(Instant.now());

        Set<Authority> authorities = dto.getEffectiveRoles()
            .stream()
            .map(u -> authorityRepository.getOne(u))
            .collect(Collectors.toSet());

        authority.setCompositeRoles(authorities);

        List<PermissionDTO> availablePrivileges = dto.getAvailablePrivileges();

        List<Permission> permissions = permissionMapper.toEntity(availablePrivileges);

        authority.setPermissions(new HashSet<>(permissions));

        keycloakFacade.createWithCompositeRoles(dto, setting.getRealmApp(), setting.getClientUUID());

        authorityRepository.save(authority);

    }

    private void handleUpdateRole(CreateRoleRequest dto, String updateBy, Authority authority) {

        authority.setDescription(dto.getDescription());
        authority.setLastModifiedBy(updateBy);
        authority.setLastModifiedDate(Instant.now());

        keycloakFacade.updateRole(authority.getName(), setting.getRealmApp(), authority);

        Set<Authority> authorities = dto.getEffectiveRoles().stream()
            .map(u -> authorityRepository.getOne(u))
            .collect(Collectors.toSet());
        authority.addCompositeRoles(authorities);

        keycloakFacade.updateWithCompositeRoles(dto, setting.getRealmApp(), setting.getClientUUID());

        List<PermissionDTO> availablePrivileges = dto.getAvailablePrivileges();
        List<Permission> permissions = permissionMapper.toEntity(availablePrivileges);
        authority.addPermission(new HashSet<>(permissions));
        authorityRepository.save(authority);


    }
}

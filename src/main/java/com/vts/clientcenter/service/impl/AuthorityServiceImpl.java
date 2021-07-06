package com.vts.clientcenter.service.impl;

import com.vts.clientcenter.config.Constants;
import com.vts.clientcenter.config.KeycloakConfig;
import com.vts.clientcenter.domain.Authority;
import com.vts.clientcenter.domain.Module;
import com.vts.clientcenter.domain.Permission;
import com.vts.clientcenter.repository.AuthorityRepository;
import com.vts.clientcenter.repository.ModuleRepository;
import com.vts.clientcenter.security.SecurityUtils;
import com.vts.clientcenter.service.AbstractBaseService;
import com.vts.clientcenter.service.AuthorityService;
import com.vts.clientcenter.service.PermissionDetailDto;
import com.vts.clientcenter.service.UserService;
import com.vts.clientcenter.service.dto.AuthorityDto;
import com.vts.clientcenter.service.dto.CreateRoleRequest;
import com.vts.clientcenter.service.dto.PermissionDTO;
import com.vts.clientcenter.service.dto.RoleDetailResponse;
import com.vts.clientcenter.service.keycloak.KeycloakFacade;
import com.vts.clientcenter.service.mapper.AuthorityMapper;
import com.vts.clientcenter.service.mapper.PermissionMapper;
import com.vts.clientcenter.service.mapper.PermissionMapperImpl;
import com.vts.clientcenter.web.rest.errors.BadRequestAlertException;
import org.mapstruct.ap.internal.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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

    @Autowired
    private ModuleRepository moduleRepository;

    public AuthorityServiceImpl(UserService userService) {
        super(userService);
    }


    @Override
    public Page<AuthorityDto> getAuthorities(Pageable pageable) {

        Page<Authority> allAuthorities = authorityRepository.getAllByNameStartingWithAndNameIsNotContainingAndNameIsNotContaining(   "ROLE_", "_ACCESS", "ROLE_PERMISSION_", pageable);

        return allAuthorities.map(u -> authorityMapper.authorityToDto(u));
    }


    @Override
    public RoleDetailResponse save(CreateRoleRequest dto) {

        Optional<Authority> authorityOptional = authorityRepository.findById(dto.getRoleName());

        if (!authorityOptional.isPresent()) {
            handleCreateRole(dto);
        } else {
            handleUpdateRole(dto, authorityOptional.get());
        }

        Authority newAuthority = authorityRepository.getOne(dto.getRoleName());

        List<PermissionDetailDto> permissionDetailDto = getPermissionDetailDto(newAuthority);

        return RoleDetailResponse
            .builder()
            .success(true)
            .isCompositeRole(!CollectionUtils.isEmpty(newAuthority.getCompositeRoles()))
            .roleName(newAuthority.getName())
            .description(newAuthority.getDescription())
            .effectiveRoles(dto.getEffectiveRoles())
            .availablePrivileges(permissionDetailDto)
            .build();
    }

    private void handleCreateRole(CreateRoleRequest dto) {

        Authority authority = new Authority();
        authority.setName(dto.getRoleName());
        authority.setDescription(dto.getDescription());

        Set<Authority> authorities = dto.getEffectiveRoles()
            .stream()
            .map(u -> authorityRepository.getOne(u))
            .collect(Collectors.toSet());

        authority.addCompositeRoles(authorities);

        List<Permission> permissions = getPermissionFromCreateRequest(dto);

        authority.setPermissions(new HashSet<>(permissions));

        keycloakFacade.createWithCompositeRoles(dto, setting.getRealmApp(), setting.getClientUUID());

        authorityRepository.save(authority);

    }

    private void handleUpdateRole(CreateRoleRequest dto, Authority authority) {

        validateAuthority(dto.getRoleName());

        keycloakFacade.updateRole(authority.getName(), setting.getRealmApp(), authority);

        authority.setDescription(dto.getDescription());

        Set<Authority> authorities = dto.getEffectiveRoles().stream()
            .map(u -> authorityRepository.getOne(u))
            .collect(Collectors.toSet());
        authority.addCompositeRoles(authorities);

        keycloakFacade.updateWithCompositeRoles(dto, setting.getRealmApp(), setting.getClientUUID());

        List<Permission> permissions = getPermissionFromCreateRequest(dto);

        authority.addPermission(new HashSet<>(permissions));

        authorityRepository.save(authority);
    }

    private List<Permission> getPermissionFromCreateRequest(CreateRoleRequest dto) {
        List<PermissionDTO> availablePrivileges = dto.getAvailablePrivileges().stream()
            .filter(PermissionDetailDto::isSelected).map(u ->  {
                PermissionDTO permissionDTO = new PermissionDTO();
                permissionDTO.setName(u.getName());
                permissionDTO.setDescription(u.getDesc());
                return permissionDTO;
            }).collect(Collectors.toList());

        return permissionMapper.toEntity(availablePrivileges);
    }

    @Override
    public List<PermissionDetailDto> getAllPermissions() {

        List<Module> modules = moduleRepository.findAll();
        List<String> operations = Arrays.asList("Read", "Update", "Delete", "Create");
        List<PermissionDetailDto> permissions = modules.stream()
            .flatMap(u ->
            {
                String permission = "ROLE_PERMISSION_" + u.getName().toUpperCase() + "_";
                return operations.stream().map(o -> {
                    PermissionDetailDto detailDto = new PermissionDetailDto();
                    detailDto.setName(permission + o.toUpperCase());
                    detailDto.setDesc(o + " " + u.getName() + " " + "Permission");
                    return detailDto;
                });
            }).collect(Collectors.toList());

        syncPermissionsToKeycloak(permissions);

        return permissions;
    }

    @Async
    void syncPermissionsToKeycloak(List<PermissionDetailDto> permissions) {
        keycloakFacade.syncPermissionForClient(setting.getRealmApp(), setting.getClientUUID(), permissions);
    }

    private Authority validateAuthority(String roleName) {
        Optional<Authority> authorityOptional = authorityRepository.findByName(roleName);
        if (Objects.isNull(roleName)) {
            throw new BadRequestAlertException("RoleName is not null.", "RoleName", Constants.ROLE_NAME_NOT_NULL);
        }
        if (!authorityOptional.isPresent()) {
            throw new BadRequestAlertException("Role not found", "Role", Constants.USER_ROLE_NOT_FOUND);
        }

        return authorityOptional.get();
    }

    @Override
    public RoleDetailResponse getByRoleName(String roleName) {

        Authority authority = validateAuthority(roleName);

        Set<Authority> compositeRoles = authority.getCompositeRoles();

        List<PermissionDetailDto> detailDtoList = getPermissionDetailDto(authority);

        return RoleDetailResponse.builder()
            .roleName(roleName)
            .description(authority.getDescription())
            .success(true)
            .isCompositeRole(!compositeRoles.isEmpty())
            .availablePrivileges(detailDtoList)
            .effectiveRoles(compositeRoles.stream().map(Authority::getName).collect(Collectors.toList()))
            .build();
    }

    private List<PermissionDetailDto> getPermissionDetailDto(Authority authority) {
        Set<String> permissions = authority.getPermissions()
            .stream().map(Permission::getName).collect(Collectors.toSet());

        List<PermissionDetailDto> allPermissions = getAllPermissions();

        return allPermissions.stream().peek(permissionDetailDto -> {
            permissionDetailDto.setSelected(permissions.contains(permissionDetailDto.getName()));
        }).collect(Collectors.toList());
    }
}

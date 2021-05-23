package com.vts.clientcenter.service.impl;

import static java.util.Objects.isNull;

import com.vts.clientcenter.config.Constants;
import com.vts.clientcenter.domain.*;
import com.vts.clientcenter.domain.enumeration.OperationEnum;
import com.vts.clientcenter.repository.*;
import com.vts.clientcenter.service.AbstractBaseService;
import com.vts.clientcenter.service.PermissionDetailDto;
import com.vts.clientcenter.service.RolePermissionExtensionService;
import com.vts.clientcenter.service.UserService;
import com.vts.clientcenter.service.dto.EditPermissionRequestDto;
import com.vts.clientcenter.service.dto.EditPermissionResponseDto;
import com.vts.clientcenter.service.dto.RolePermissionDTO;
import com.vts.clientcenter.service.mapper.RolePermissionMapper;
import com.vts.clientcenter.web.rest.errors.BadRequestAlertException;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import javax.validation.constraints.NotNull;

@Service
@Transactional
public class RolePermissionExtensionServiceImpl extends AbstractBaseService implements RolePermissionExtensionService {
    private final Logger log = LoggerFactory.getLogger(RolePermissionExtensionServiceImpl.class);

    private final RolePermissionRepository rolePermissionRepository;

    private final RolePermissionMapper rolePermissionMapper;

    private final AuthorityRepository authorityRepository;

    private final ModuleOperationRepository moduleOperationRepository;

    private final PermissionOperationRepository permissionOperationRepository;

    private final ModuleOperationRepository operationRepository;

    private final PermissionRepository permissionRepository;

    public RolePermissionExtensionServiceImpl(
        UserService userService,
        RolePermissionRepository rolePermissionRepository,
        RolePermissionMapper rolePermissionMapper,
        AuthorityRepository authorityRepository,
        ModuleOperationRepository moduleOperationRepository,
        PermissionOperationRepository permissionOperationRepository,
        ModuleOperationRepository operationRepository,
        PermissionRepository permissionRepository) {
        super(userService);
        this.rolePermissionRepository = rolePermissionRepository;
        this.rolePermissionMapper = rolePermissionMapper;
        this.authorityRepository = authorityRepository;
        this.moduleOperationRepository = moduleOperationRepository;
        this.permissionOperationRepository = permissionOperationRepository;
        this.operationRepository = operationRepository;
        this.permissionRepository = permissionRepository;
    }

    @Override
    public RolePermissionDTO save(RolePermissionDTO rolePermissionDTO) {

        String userLogin = getUserLogin();

        if (Objects.isNull(rolePermissionDTO.getId())) {
            rolePermissionDTO.setCreatedDate(Instant.now());
            rolePermissionDTO.setCreatedBy(userLogin);
        }

        rolePermissionDTO.setLastModifiedBy(userLogin);
        rolePermissionDTO.setLastModifiedDate(Instant.now());
        rolePermissionDTO.setLastModifiedDate(Instant.now());
        RolePermission rolePermission = rolePermissionMapper.toEntity(rolePermissionDTO);
        rolePermission = rolePermissionRepository.save(rolePermission);
        return rolePermissionMapper.toDto(rolePermission);
    }

    @Override
    public EditPermissionResponseDto saveDetail(EditPermissionRequestDto dto) {

        String userLogin = getUserLogin();

        if (Objects.isNull(dto.getRoleName())) {
            throw new BadRequestAlertException("Role not existed.", "UserRole", Constants.USER_ROLE_NOT_FOUND);
        }

        Optional<Authority> authorityOptional = authorityRepository.findById(dto.getRoleName());

        boolean present = authorityOptional.isPresent();
        if (!present) {
            throw new BadRequestAlertException("Role Name not existed.", "UserRole", Constants.USER_ROLE_NOT_FOUND);
        }

        Authority authority = authorityOptional.get();

        authority.setDescription(dto.getDescription());

        authority.setLastModifiedBy(userLogin);

        authority.setLastModifiedDate(Instant.now());

        List<PermissionDetailDto> permissionDetails = dto.getPermissionDetails();

        Set<RolePermission> rolePermissionList =  authority.getRolePermissions();

        List<Long> updatingIds = rolePermissionList
            .stream()
            .filter(d -> permissionDetails.stream().map(PermissionDetailDto::getId).collect(Collectors.toSet()).contains(d))
            .map(RolePermission::getId)
            .collect(Collectors.toList());

        rolePermissionRepository.deleteByIdNotIn(updatingIds);

        List<ModuleOperation> moduleOperations = moduleOperationRepository.findAll();

        Set<RolePermission> savingRolePermissions = new HashSet<>();

        for (int i = 0; i < permissionDetails.size(); i++) {

            PermissionDetailDto u = permissionDetails.get(i);

            Optional<Permission> permissionOptional = permissionRepository.findById(u.getPermissionId());

            if (!permissionOptional.isPresent()) {
                continue;
            }

            Permission permission = permissionOptional.get();

            List<OperationEnum> operationEnumList = u.getOperations();

            Set<ModuleOperation> mappingOperations = moduleOperations.stream()
                .filter(mo -> operationEnumList.contains(mo.getName())).collect(Collectors.toSet());

            RolePermission rolePermission;
            if (isNull(u.getId())) {
                rolePermission = RolePermission.builder()
                    .permission(permission)
                    .permissionId(permission.getId())
                    .role(authority)
                    .roleName(authority.getName())
                    .operations(mappingOperations)
                    .build();
            } else {
                Optional<RolePermission> rolePermissionOptional = rolePermissionList.stream().filter(rp -> u.getId().equals(rp.getId())).findFirst();
                if (!rolePermissionOptional.isPresent()) {
                    continue;
                }
                rolePermission = rolePermissionOptional.get();
                rolePermission.setLastModifiedDate(Instant.now());
                rolePermission.setLastModifiedBy(userLogin);
                rolePermission.setPermission(permission);
                rolePermission.setPermissionId(permission.getId());
                rolePermission.setRoleName(authority.getName());
                rolePermission.setRole(authority);
                rolePermission.setOperations(mappingOperations);
            }

            savingRolePermissions.add(rolePermission);

        }

        authority.setRolePermissions(savingRolePermissions);

        List<RolePermission> rolePermissions = rolePermissionRepository.saveAll(savingRolePermissions);

        for (PermissionDetailDto permissionDetail : dto.getPermissionDetails()) {
            rolePermissions.stream()
                .filter(rolePermission -> rolePermission.getPermissionId().equals(permissionDetail.getPermissionId()))
                .forEach(rolePermission -> permissionDetail.setId(rolePermission.getId()));
        }


        return EditPermissionResponseDto
            .builder()
            .lastModifiedDate(Instant.now())
            .roleName(dto.getRoleName())
            .description(dto.getDescription())
            .permissionDetails(dto.getPermissionDetails())
            .modifiedBy(userLogin)
            .build();
    }

    @Override
    public EditPermissionResponseDto getDetailRole(String roleName) {

        String userLogin = getUserLogin();

        Optional<Authority> authorityOptional = authorityRepository.findById(roleName);

        boolean present = authorityOptional.isPresent();
        if (!present) {
            throw new BadRequestAlertException("Role Name not existed.", "UserRole", Constants.USER_ROLE_NOT_FOUND);
        }

        Authority authority = authorityOptional.get();

        List<PermissionDetailDto> permissionDetails = new ArrayList<>();

        Set<RolePermission> rolePermissionList = authority.getRolePermissions();

        List<@NotNull Long> permissionIds = rolePermissionList.stream().map(RolePermission::getPermissionId).collect(Collectors.toList());

        List<Permission> permissions = permissionRepository.findAllById(permissionIds);

        for (RolePermission rolePermission : rolePermissionList) {

            Optional<Permission> optionalPermission = permissions.stream()
                .filter(p -> p.getId().equals(rolePermission.getPermissionId())).findFirst();

            if (!optionalPermission.isPresent()) {
                continue;
            }

            Permission permission = optionalPermission.get();

            PermissionDetailDto detailDto = PermissionDetailDto
                .builder()
                .id(rolePermission.getId())
                .permissionId(rolePermission.getPermissionId())
                .permissionName(permission.getName())
                .permissionDesc(permission.getDescription())
                .operations(rolePermission.getOperations().stream().map(ModuleOperation::getName).collect(Collectors.toList()))
                .build();
            permissionDetails.add(detailDto);
        }

        return EditPermissionResponseDto
            .builder()
            .lastModifiedDate(Instant.now())
            .roleName(authority.getName())
            .description(authority.getDescription())
            .permissionDetails(permissionDetails)
            .modifiedBy(userLogin)
            .build();
    }


}

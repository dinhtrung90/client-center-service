package com.vts.clientcenter.service.impl;

import static java.util.Objects.isNull;

import com.vts.clientcenter.config.Constants;
import com.vts.clientcenter.domain.Authority;
import com.vts.clientcenter.domain.ModuleOperation;
import com.vts.clientcenter.domain.PermissionOperation;
import com.vts.clientcenter.domain.RolePermission;
import com.vts.clientcenter.domain.enumeration.OperationEnum;
import com.vts.clientcenter.repository.AuthorityRepository;
import com.vts.clientcenter.repository.ModuleOperationRepository;
import com.vts.clientcenter.repository.PermissionOperationRepository;
import com.vts.clientcenter.repository.RolePermissionRepository;
import com.vts.clientcenter.security.SecurityUtils;
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

import com.vts.clientcenter.web.rest.errors.UnAuthorizedRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public RolePermissionExtensionServiceImpl(
        UserService userService,
        RolePermissionRepository rolePermissionRepository,
        RolePermissionMapper rolePermissionMapper,
        AuthorityRepository authorityRepository,
        ModuleOperationRepository moduleOperationRepository,
        PermissionOperationRepository permissionOperationRepository,
        ModuleOperationRepository operationRepository
    ) {
        super(userService);
        this.rolePermissionRepository = rolePermissionRepository;
        this.rolePermissionMapper = rolePermissionMapper;
        this.authorityRepository = authorityRepository;
        this.moduleOperationRepository = moduleOperationRepository;
        this.permissionOperationRepository = permissionOperationRepository;
        this.operationRepository = operationRepository;
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

        List<RolePermission> rolePermissionList = rolePermissionRepository.findByRoleName(authority.getName());

        List<RolePermission> updatePermissionObjects = rolePermissionList
            .stream()
            .filter(d -> permissionDetails.stream().map(PermissionDetailDto::getId).collect(Collectors.toSet()).contains(d))
            .collect(Collectors.toList());

        List<Long> updatingIds = updatePermissionObjects.stream().map(RolePermission::getId).collect(Collectors.toList());

        rolePermissionRepository.deleteByIdNotIn(updatingIds);

        permissionOperationRepository.deleteByRolePermissionIdIn(updatingIds);

        List<ModuleOperation> moduleOperations = moduleOperationRepository.findAll();

        List<PermissionOperation> operations = new ArrayList<>();

        for (int i = 0; i < permissionDetails.size(); i++) {
            PermissionDetailDto u = permissionDetails.get(i);

            RolePermission rolePermission;
            if (isNull(u.getId())) {
                rolePermission =
                    new RolePermission()
                        .permissionId(u.getPermissionId())
                        .roleName(dto.getRoleName())
                        .createdBy(userLogin)
                        .lastModifiedBy(userLogin)
                        .createdDate(Instant.now())
                        .lastModifiedDate(Instant.now());
            } else {
                rolePermission = rolePermissionRepository.getOne(u.getId());
                rolePermission.setLastModifiedDate(Instant.now());
                rolePermission.setLastModifiedBy(userLogin);
            }

            rolePermission = rolePermissionRepository.save(rolePermission);

            u.setId(rolePermission.getId());

            for (OperationEnum operation : u.getOperations()) {
                PermissionOperation permissionOperation = new PermissionOperation();
                permissionOperation.setRolePermissionId(rolePermission.getId());
                permissionOperation.setOperationId(
                    moduleOperations.stream().filter(a -> a.getName().equals(operation)).findFirst().get().getId()
                );
                operations.add(permissionOperation);
            }
        }
        permissionOperationRepository.saveAll(operations);

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

        List<RolePermission> rolePermissionList = rolePermissionRepository.findByRoleName(authority.getName());

        for (RolePermission rolePermission : rolePermissionList) {
            List<PermissionOperation> permissionOperations = permissionOperationRepository.findByRolePermissionId(rolePermission.getId());

            Set<Long> operationIds = permissionOperations.stream().map(PermissionOperation::getOperationId).collect(Collectors.toSet());

            List<ModuleOperation> operationList = operationRepository.findAllById(operationIds);

            PermissionDetailDto detailDto = PermissionDetailDto
                .builder()
                .id(rolePermission.getId())
                .permissionId(rolePermission.getPermissionId())
                .permissionName(roleName)
                .operations(operationList.stream().map(ModuleOperation::getName).collect(Collectors.toList()))
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

package com.vts.clientcenter.service.impl;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import com.okta.sdk.resource.group.Group;
import com.vts.clientcenter.config.Constants;
import com.vts.clientcenter.dao.UserDao;
import com.vts.clientcenter.domain.*;
import com.vts.clientcenter.domain.enumeration.OperationEnum;
import com.vts.clientcenter.events.GroupCreationEvent;
import com.vts.clientcenter.repository.*;
import com.vts.clientcenter.security.AuthoritiesConstants;
import com.vts.clientcenter.service.*;
import com.vts.clientcenter.service.dto.EditPermissionRequestDto;
import com.vts.clientcenter.service.dto.EditPermissionResponseDto;
import com.vts.clientcenter.service.dto.RolePermissionDTO;
import com.vts.clientcenter.service.dto.UserDTO;
import com.vts.clientcenter.service.mapper.RolePermissionMapper;
import com.vts.clientcenter.service.mapper.UserMapper;
import com.vts.clientcenter.web.rest.errors.BadRequestAlertException;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private final OktaService oktaService;

    private final ApplicationEventPublisher applicationEventPublisher;

    private final CacheManager cacheManager;

    private final UserDao userDao;

    private final UserMapper userMapper;

    public RolePermissionExtensionServiceImpl(
        UserService userService,
        RolePermissionRepository rolePermissionRepository,
        RolePermissionMapper rolePermissionMapper,
        AuthorityRepository authorityRepository,
        ModuleOperationRepository moduleOperationRepository,
        PermissionOperationRepository permissionOperationRepository,
        ModuleOperationRepository operationRepository,
        PermissionRepository permissionRepository, OktaService oktaService, ApplicationEventPublisher applicationEventPublisher, CacheManager cacheManager, UserDao userDao, UserMapper userMapper) {
        super(userService);
        this.rolePermissionRepository = rolePermissionRepository;
        this.rolePermissionMapper = rolePermissionMapper;
        this.authorityRepository = authorityRepository;
        this.moduleOperationRepository = moduleOperationRepository;
        this.permissionOperationRepository = permissionOperationRepository;
        this.operationRepository = operationRepository;
        this.permissionRepository = permissionRepository;
        this.oktaService = oktaService;
        this.applicationEventPublisher = applicationEventPublisher;
        this.cacheManager = cacheManager;
        this.userDao = userDao;
        this.userMapper = userMapper;
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


    @Override
    public EditPermissionResponseDto createRole(EditPermissionRequestDto dto) {

        //api create role
        String userLogin = getUserLogin();

        if (Objects.isNull(dto.getRoleName())) {
            throw new BadRequestAlertException("Role not existed.", "UserRole", Constants.USER_ROLE_NOT_FOUND);
        }

        Optional<Authority> authorityOptional = authorityRepository.findById(dto.getRoleName());

        if (authorityOptional.isPresent()) {
            throw new BadRequestAlertException("Authority have existed", "UserRole", Constants.USER_ROLE_NOT_FOUND);
        }

        // create on okta service
        Group group = oktaService.createGroup(dto.getRoleName(), dto.getDescription());

        applicationEventPublisher.publishEvent(GroupCreationEvent.builder().group(group).build());

        Authority authority = new Authority();
        authority.setName(dto.getRoleName());
        authority.setDescription(dto.getDescription());
        authority.setCreatedBy(userLogin);
        authority.setLastModifiedBy(userLogin);
        authority.setLastModifiedDate(Instant.now());
        authority.setCreatedDate(Instant.now());
        authorityRepository.save(authority);

        List<PermissionDetailDto> permissionDetails = dto.getPermissionDetails();

        List<ModuleOperation> moduleOperations = moduleOperationRepository.findAll();

        Set<RolePermission> savingRolePermissions = new HashSet<>();

        for (int i = 0; i < permissionDetails.size(); i++) {


            PermissionDetailDto u = permissionDetails.get(i);

            if (nonNull(u.getId())) {
               throw new BadRequestAlertException("Create Roles can not have ID value.", "Roles", Constants.ID_IS_NULL);
            }

            Optional<Permission> permissionOptional = permissionRepository.findById(u.getPermissionId());

            if (!permissionOptional.isPresent()) {
                throw new BadRequestAlertException("Permission Not Existed.", "Permission", Constants.PERMISSION_IS_NULL);
            }

            Permission permission = permissionOptional.get();

            List<OperationEnum> operationEnumList = u.getOperations();

            Set<ModuleOperation> mappingOperations = moduleOperations.stream()
                .filter(mo -> operationEnumList.contains(mo.getName())).collect(Collectors.toSet());

            RolePermission rolePermission;

            rolePermission = RolePermission.builder()
                .permission(permission)
                .permissionId(permission.getId())
                .role(authority)
                .roleName(authority.getName())
                .operations(mappingOperations)
                .build();
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
    public void removeDetailRole(String roleName) {

        //api create role
        if (Objects.isNull(roleName)) {
            throw new BadRequestAlertException("Role not existed.", "UserRole", Constants.USER_ROLE_NOT_FOUND);
        }

        if (roleName.equalsIgnoreCase(AuthoritiesConstants.USER) ||
            roleName.equalsIgnoreCase(AuthoritiesConstants.ADMIN) ||
            roleName.equalsIgnoreCase(AuthoritiesConstants.SUPER_ADMIN) ||
                roleName.equalsIgnoreCase(AuthoritiesConstants.SUPERVISOR)) {
            throw new BadRequestAlertException("Role can not remove", "UserRole", Constants.ROLE_NOT_DELETE);
        }

        Optional<Authority> authorityOptional = authorityRepository.findByName(roleName);

        if (!authorityOptional.isPresent()) {
            throw new BadRequestAlertException("Role not existed.", "UserRole", Constants.USER_ROLE_NOT_FOUND);
        }

        oktaService.removeGroup(roleName);

        userDao.handleRemoveFromRole(roleName);
    }

    @Override
    public UserDTO assignRoleForUser(String roleName, String userId) {

        //api create role
        String userLogin = getUserLogin();

        if (Objects.isNull(roleName)) {
            throw new BadRequestAlertException("Role not existed.", "UserRole", Constants.USER_ROLE_NOT_FOUND);
        }

        if (Objects.isNull(userId)) {
            throw new BadRequestAlertException("User not existed.", "UserRole", Constants.USER_NOT_FOUND);
        }

        User user = getUserRepository().getOne(userId);

        Authority authority = authorityRepository.getOne(roleName);

        user.addAuthority(authority);

        getUserRepository().save(user);

        return userMapper.userToUserDTO(user);
    }

    @Override
    public List<UserDTO> assignRoleForUsers(String roleName, List<String> userIds) {

        //api create role
        String userLogin = getUserLogin();

        if (Objects.isNull(roleName)) {
            throw new BadRequestAlertException("Role not existed.", "UserRole", Constants.USER_ROLE_NOT_FOUND);
        }

        Authority authority = authorityRepository.getOne(roleName);

        List<User> users = getUserRepository().findAllById(userIds);

        for (User user : users) {
            boolean contains = user.getAuthorities().stream().map(Authority::getName).collect(Collectors.toSet()).contains(roleName);
            if (contains) {
                throw new BadRequestAlertException(String.format("Role existed for userId = %s", user.getId()), "UserRole", Constants.USER_ROLE_NOT_FOUND);
            }
            user.addAuthority(authority);
            user.setLastModifiedBy(userLogin);
            user.setLastModifiedDate(Instant.now());
            clearUserCaches(user);
        }

        getUserRepository().saveAll(users);

        return userMapper.usersToUserDTOs(users);
    }

    private void clearUserCaches(User user) {
        Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE)).evict(user.getLogin());
        if (user.getEmail() != null) {
            Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE)).evict(user.getEmail());
        }
    }
}

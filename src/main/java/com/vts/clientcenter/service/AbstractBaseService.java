package com.vts.clientcenter.service;

import com.vts.clientcenter.config.Constants;
import com.vts.clientcenter.domain.*;
import com.vts.clientcenter.domain.enumeration.OperationEnum;
import com.vts.clientcenter.repository.*;
import com.vts.clientcenter.security.SecurityUtils;
import com.vts.clientcenter.web.rest.errors.BadRequestAlertException;
import com.vts.clientcenter.web.rest.errors.UnAuthorizedRequestAlertException;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
@Transactional
@Getter
@Setter
public class AbstractBaseService {
    private final UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RolePermissionRepository rolePermissionRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private PermissionOperationRepository permissionOperationRepository;

    @Autowired
    private ModuleOperationRepository moduleOperationRepository;

    private Supplier<User> userLogin = this::getUserByLogin;

    public AbstractBaseService(UserService userService) {
        this.userService = userService;
    }

    private User getUserByLogin() {
        Optional<String> currentUserLoginOptional = SecurityUtils.getCurrentUserLogin();
        String userLogin = currentUserLoginOptional.get();
        Optional<User> userOptional = userService.getUserWithAuthoritiesByLogin(userLogin);
        if (!userOptional.isPresent()) {
            throw new BadRequestAlertException("User Not Found", "User", Constants.USER_NOT_FOUND);
        }
        return userOptional.get();
    }

    public boolean hasPermission(String roleName, String permission, List<OperationEnum> operations) {
        User user = getUserByLogin();

        List<Authority> authorities = user
            .getAuthorities()
            .stream()
            .filter(r -> r.getName().equalsIgnoreCase(roleName))
            .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(authorities)) {
            throw new UnAuthorizedRequestAlertException("User is not authorized.", "UserRole", Constants.USER_UNAUTHORIZED);
        }

        Authority authority = authorities.get(0);

        // fetching permission By Roles

        List<RolePermission> rolePermissions = rolePermissionRepository.findByRoleName(authority.getName());

        Optional<RolePermission> rolePermissionOptional = rolePermissions.stream().filter(r -> isValid(r, permission)).findFirst();

        if (!rolePermissionOptional.isPresent()) {
            throw new UnAuthorizedRequestAlertException("Permission not supported yet.", "UserRole", Constants.PERMISSION_NOT_SUPPORTED);
        }

        RolePermission rolePermission = rolePermissionOptional.get();

        List<PermissionOperation> permissionOperations = permissionOperationRepository.findByRolePermissionId(rolePermission.getId());

        List<Long> idOperations = permissionOperations.stream().map(PermissionOperation::getOperationId).collect(Collectors.toList());

        List<ModuleOperation> operationInRoles = moduleOperationRepository.findAllById(idOperations);

        return operationInRoles.stream().map(ModuleOperation::getName).collect(Collectors.toSet()).containsAll(operations);
    }

    private boolean isValid(RolePermission r, String permission) {
        Permission permissionObj = permissionRepository.getOne(r.getPermissionId());
        return permissionObj.getName().equalsIgnoreCase(permission);
    }

    public String getUserLogin() {

        Optional<String> currentUserLoginOptional = SecurityUtils.getCurrentUserLogin();
        if (currentUserLoginOptional.isPresent()) {
            throw new UnAuthorizedRequestAlertException("User not authorized.", "Users", Constants.USER_UNAUTHORIZED);
        }
        return currentUserLoginOptional.get();
    }
}

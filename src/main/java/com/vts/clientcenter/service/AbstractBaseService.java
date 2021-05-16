package com.vts.clientcenter.service;

import com.vts.clientcenter.config.Constants;
import com.vts.clientcenter.domain.User;
import com.vts.clientcenter.repository.RolePermissionRepository;
import com.vts.clientcenter.repository.UserRepository;
import com.vts.clientcenter.security.SecurityUtils;
import com.vts.clientcenter.web.rest.errors.BadRequestAlertException;
import java.util.Optional;
import java.util.function.Supplier;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    //    private boolean hasPermission(String roleName, PermissionAction action) {
    //        User user = getUserByLogin();
    //        user.getAuthorities().stream()
    //        .forEach( r -> {
    //            List<Long> collectPermission = r.getPermissions().stream().map(Permission::getId).collect(Collectors.toList());
    //            List<RolePermission> permissionList = rolePermissionRepository.findAllByPermission_idIn(collectPermission);
    //
    //            permissionList.stream()
    //                .filter(rolePermission -> rolePermission.getRoleName().equalsIgnoreCase(roleName))
    //                .filter(rolePermission -> rolePermission.)
    //
    //        });
    //    }

}

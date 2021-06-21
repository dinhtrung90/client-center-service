package com.vts.clientcenter.service;

import com.vts.clientcenter.config.Constants;
import com.vts.clientcenter.config.KeycloakConfig;
import com.vts.clientcenter.domain.*;
import com.vts.clientcenter.domain.enumeration.OperationEnum;
import com.vts.clientcenter.repository.*;
import com.vts.clientcenter.security.SecurityUtils;
import com.vts.clientcenter.service.keycloak.KeycloakFacade;
import com.vts.clientcenter.web.rest.errors.BadRequestAlertException;
import com.vts.clientcenter.web.rest.errors.UnAuthorizedRequestAlertException;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
@Transactional
@Getter
@Setter
public class AbstractBaseService {

    @Autowired
    protected KeycloakConfig setting;

    @Autowired
    @Qualifier("keycloakFacade")
    protected KeycloakFacade keycloakFacade;

    private final UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PermissionRepository permissionRepository;


    private Supplier<User> userLogin = this::getUserByLogin;

    public AbstractBaseService(UserService userService) {
        this.userService = userService;
    }

    public User getUserByLogin() {
        Optional<String> currentUserLoginOptional = SecurityUtils.getCurrentUserLogin();
        String userLogin = currentUserLoginOptional.get();
        Optional<User> userOptional = userService.getUserWithAuthoritiesByLogin(userLogin);
        if (!userOptional.isPresent()) {
            throw new BadRequestAlertException("User Not Found", "User", Constants.USER_NOT_FOUND);
        }
        return userOptional.get();
    }

    public String getUserLogin() {

        Optional<String> currentUserLoginOptional = SecurityUtils.getCurrentUserLogin();
        if (!currentUserLoginOptional.isPresent()) {
            throw new UnAuthorizedRequestAlertException("User not authorized.", "Users", Constants.USER_UNAUTHORIZED);
        }
        return currentUserLoginOptional.get();
    }
}

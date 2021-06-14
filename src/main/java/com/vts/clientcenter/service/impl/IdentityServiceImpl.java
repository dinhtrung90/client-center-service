package com.vts.clientcenter.service.impl;

import com.vts.clientcenter.config.Constants;
import com.vts.clientcenter.config.KeycloakConfig;
import com.vts.clientcenter.helpers.PasswordGenerator;
import com.vts.clientcenter.service.IdentityService;
import com.vts.clientcenter.service.dto.UserDTO;
import com.vts.clientcenter.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.security.RandomUtil;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RealmsResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.ws.rs.core.Response;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class IdentityServiceImpl implements IdentityService {

    private Keycloak keycloak;

    @Autowired
    private KeycloakConfig setting;

    @PostConstruct
    void init() {

        keycloak = KeycloakBuilder
            .builder()
            .clientId(setting.getClientId())
            .clientSecret(setting.getClientSecret())
            .realm(setting.getRealmName())
            .grantType(OAuth2Constants.PASSWORD)
            .serverUrl(setting.getAuthorizeUrl())
            .username("Admin")
            .password("Pa55w0rd")
            .build();

    }

    @Override
    public UserDTO createUser(UserDTO userDto) throws Exception {

        RealmResource realmResource = keycloak.realm(setting.getRealmName());

        UsersResource usersResource = realmResource.users();

        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername(userDto.getLogin());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());

        Response response = usersResource.create(user);
        String userId = CreatedResponseUtil.getCreatedId(response);

        String password = PasswordGenerator.generateRandomPassword(8);

        CredentialRepresentation passwordCred = new CredentialRepresentation();
        passwordCred.setTemporary(false);
        passwordCred.setType(CredentialRepresentation.PASSWORD);
        passwordCred.setValue(password);
        UserResource userResource = usersResource.get(userId);

        // Set password credential
        userResource.resetPassword(passwordCred);


        List<String> notSupportedRoles = userDto.getAuthorities()
            .stream()
            .filter(au -> !realmResource.roles().list().stream().map(RoleRepresentation::getName).collect(Collectors.toList()).contains(au))
            .collect(Collectors.toList());

        if (!CollectionUtils.isEmpty(notSupportedRoles)) {
            throw new BadRequestAlertException("Roles not supported yet.", "Roles", Constants.ROLE_NOT_MATCH);
        }

        List<RoleRepresentation> roleRepresentations = userDto.getAuthorities()
            .stream()
            .map(au -> realmResource
                .roles()
                .get(au)
                .toRepresentation())
            .collect(Collectors.toList());


        userResource.roles().realmLevel() //
            .add(roleRepresentations);

        usersResource.get(userId).executeActionsEmail(Arrays.asList("VERIFY_EMAIL", "UPDATE_PASSWORD"));

        userDto.setId(userId);

        return userDto;
    }
}

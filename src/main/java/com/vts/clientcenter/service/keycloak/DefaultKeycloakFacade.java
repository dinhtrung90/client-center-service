package com.vts.clientcenter.service.keycloak;

import com.vts.clientcenter.config.Constants;
import com.vts.clientcenter.domain.Authority;
import com.vts.clientcenter.domain.enumeration.AccountStatus;
import com.vts.clientcenter.service.dto.*;
import com.vts.clientcenter.web.rest.errors.BadRequestAlertException;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.*;
import org.keycloak.representations.idm.*;
import org.keycloak.util.JsonSerialization;
import org.springframework.util.CollectionUtils;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static com.vts.clientcenter.config.Constants.ACCOUNT_STATUS_FIELD;
import static java.util.Objects.nonNull;

public class DefaultKeycloakFacade implements KeycloakFacade {

    private final Keycloak keycloak;

    public DefaultKeycloakFacade(Keycloak keycloak) {
        this.keycloak = keycloak;
    }

    @Override
    public void assignUserRole(String realmId, UserDTO userDTO) {

        UserResource userResource = getUserResource(realmId, userDTO.getId());

        List<RoleRepresentation> existedRolesRes = userResource
            .roles()
            .realmLevel()
            .listEffective()
            .stream()
            .filter(r -> r.getName().startsWith("ROLE_"))
            .collect(Collectors.toList());

        boolean isMatch = userDTO
            .getAuthorities()
            .stream()
            .allMatch(r -> existedRolesRes.stream().map(re -> re.getName()).collect(Collectors.toSet()).contains(r.getName()));

        if (isMatch) {
            return;
        }

        List<RoleRepresentation> roles = getRealmResource(realmId).roles().list();

        List<AuthorityDto> notSupportedRoles = userDTO
            .getAuthorities()
            .stream()
            .filter(au -> !roles.stream().map(RoleRepresentation::getName).collect(Collectors.toList()).contains(au.getName()))
            .collect(Collectors.toList());

        if (!CollectionUtils.isEmpty(notSupportedRoles)) {
            throw new BadRequestAlertException("Roles not supported yet.", "Roles", Constants.ROLE_NOT_MATCH);
        }

        Set<AuthorityDto> newAuthorities = userDTO
            .getAuthorities()
            .stream()
            .filter(r -> !existedRolesRes.stream().map(RoleRepresentation::getName).collect(Collectors.toSet()).contains(r.getName()))
            .collect(Collectors.toSet());

        List<RoleRepresentation> roleRepresentations = newAuthorities
            .stream()
            .map(au -> getRealmResource(realmId).roles().get(au.getName()).toRepresentation())
            .collect(Collectors.toList());

        userResource.roles().realmLevel().add(roleRepresentations);
    }

    @Override
    public void executeActionEmail(String realmId, String userId, List<String> actions) {
        UserResource userResource = getUserResource(realmId, userId);

        try {
            userResource.executeActionsEmail(actions);
        } catch (ClientErrorException e) {
            ErrorRepresentation error = e.getResponse().readEntity(ErrorRepresentation.class);
            e.printStackTrace();
        }


    }

    @Override
    public void sendVerifiedEmail(String realmId, String userId) {
        UserResource userResource = getUserResource(realmId, userId);
        userResource.sendVerifyEmail();
    }

    @Override
    public void sendResetPasswordEmail(String realmId, String userId) {
        UserResource userResource = getUserResource(realmId, userId);
        userResource.resetPasswordEmail();
    }

    @Override
    public void resetPassword(String realmId, String userId, String password) {
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setValue(password);
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setTemporary(false);
    }

    @Override
    public UpdateUserResponse updateUser(String realmId, UserDTO userDto) {
        UserResource userResource = getUserResource(realmId, userDto.getId());
        UserRepresentation userRepresentation = userResource.toRepresentation();
        userRepresentation.setEmail(userDto.getEmail());
        userRepresentation.setUsername(userDto.getLogin());
        userRepresentation.setFirstName(userDto.getFirstName());
        userRepresentation.setLastName(userDto.getLastName());
        return UpdateUserResponse.builder().success(true).userInfo(userDto).build();
    }

    @Override
    public UserReferenceDto  createUser(String realmId, CreateAccountRequest userInfo) {
        UserRepresentation ur = new UserRepresentation();
        ur.setUsername(userInfo.getLogin());
        ur.setFirstName(userInfo.getFirstName());
        ur.setLastName(userInfo.getLastName());
        ur.setEmail(userInfo.getEmail());
        ur.setEmailVerified(false);
        ur.setEnabled(true);
        Map<String, List<String>> attributes = new HashMap<>();
        attributes.put(ACCOUNT_STATUS_FIELD, Collections.singletonList(AccountStatus.PENDING.name()));
        ur.setAttributes(attributes);
        CredentialRepresentation password = new CredentialRepresentation();
        password.setValue(userInfo.getTempPassword());
        password.setType(CredentialRepresentation.PASSWORD);
        password.setTemporary(userInfo.isIsTempPassword());
        ur.setCredentials(Collections.singletonList(password));

        try (ClosableResponseWrapper wrapper = new ClosableResponseWrapper(findUsersResource(realmId).create(ur))) {
            if (Response.Status.fromStatusCode(wrapper.getResponse().getStatus()) == Response.Status.CREATED) {
                return new UserReferenceDto(wrapper.getResponse().getLocation());
            }
            return null;
        }
    }

    @Override
    public void deleteUser(String realmId, String userId) {
        try (ClosableResponseWrapper wrapper = new ClosableResponseWrapper(findUsersResource(realmId).delete(userId))) {
            if (Response.Status.fromStatusCode(wrapper.getResponse().getStatus()) == Response.Status.NO_CONTENT) {
                return;
            }
            throw new RuntimeException("DELETE_USER_FAILED");
        }
    }

    @Override
    public List<Authority> syncRoles(String realm) {
        RealmResource realmResource = getRealmResource(realm);

        return realmResource
            .roles()
            .list()
            .stream()
            .filter(r -> r.getName().startsWith("ROLE_"))
            .map(this::mapRoleRepresentationToAuthority)
            .collect(Collectors.toList());
    }

    private Authority mapRoleRepresentationToAuthority(RoleRepresentation dto) {
        Authority authority = new Authority();
        authority.setCreatedDate(Instant.now());
        authority.setLastModifiedDate(Instant.now());
        authority.setDescription(dto.getDescription());
        authority.setName(dto.getName());
        return authority;
    }

    @Override
    public RoleDetailResponse createWithCompositeRoles(CreateRoleRequest request, String realmName) {

        RolesResource rolesResource = keycloak.realm(realmName).roles();
        //create new role
        boolean isComposite = !CollectionUtils.isEmpty(request.getEffectiveRoles());

        RoleRepresentation roleRepresentation = new RoleRepresentation();
        roleRepresentation.setName(request.getRoleName());
        roleRepresentation.setDescription(request.getDescription());
        roleRepresentation.setComposite(isComposite);
        rolesResource.create(roleRepresentation);

        RoleDetailResponse response = RoleDetailResponse.builder()
            .effectiveRoles(request.getEffectiveRoles())
            .availablePrivileges(request.getAvailablePrivileges())
            .build();

        if (!isComposite) {
            return response;
        }

        Set<String> compositeRoles = org.mapstruct.ap.internal.util.Collections.asSet(request.getEffectiveRoles(), request.getEffectiveRoles());

        //get effective roles
        List<RoleRepresentation> compositeRolesRepresentations = rolesResource.list()
            .stream()
            .filter(rr -> compositeRoles.contains(rr.getName()))
            .collect(Collectors.toList());

        RoleResource roleResource = rolesResource.get(request.getRoleName());
        roleResource.addComposites(compositeRolesRepresentations);


        return response;
    }

    @Override
    public RoleDetailResponse updateWithCompositeRoles(CreateRoleRequest request, String realmName) {

        RolesResource rolesResource = keycloak.realm(realmName).roles();

        boolean isComposite = !CollectionUtils.isEmpty(request.getEffectiveRoles());

        RoleResource roleResource = rolesResource.get(request.getRoleName());

        RoleRepresentation roleRepresentation = roleResource.toRepresentation();

        roleRepresentation.setName(request.getRoleName());
        roleRepresentation.setDescription(request.getDescription());
        roleRepresentation.setComposite(isComposite);
        roleResource.update(roleRepresentation);

        Set<RoleRepresentation> deleteRoleRepresentations = org.mapstruct.ap.internal.util.Collections.asSet(roleResource.getRealmRoleComposites(), roleResource.getClientRoleComposites(realmName));

        roleResource.deleteComposites(new ArrayList<>(deleteRoleRepresentations));

        if (isComposite) {
            Set<String> effectiveRoles = org.mapstruct.ap.internal.util.Collections.asSet(request.getAvailablePrivileges(), request.getEffectiveRoles());

            List<RoleRepresentation> compositeRolesRepresentations = rolesResource.list()
                .stream()
                .filter(rr -> effectiveRoles.contains(rr.getName()))
                .collect(Collectors.toList());

            roleResource.addComposites(compositeRolesRepresentations);
        }

        return RoleDetailResponse
            .builder()
            .availablePrivileges(request.getAvailablePrivileges())
            .effectiveRoles(request.getEffectiveRoles())
            .build();
    }

    @Override
    public void createRole(Authority authority, String realmName) {
        RoleRepresentation roleRepresentation = new RoleRepresentation();
        roleRepresentation.setName(authority.getName());
        roleRepresentation.setDescription(authority.getDescription());
        keycloak.realm(realmName).roles().create(roleRepresentation);
    }

    @Override
    public void updateRole(String olRoleName, String realmName, Authority updateAuthority) {
        RoleResource roleResource = getRoleResource(realmName).get(olRoleName);
        RoleRepresentation roleRepresentation = roleResource.toRepresentation();
        roleRepresentation.setDescription(updateAuthority.getDescription());
        roleRepresentation.setName(updateAuthority.getName());
        roleResource.update(roleRepresentation);
    }

    @Override
    public UserDTO findUserById(String realmId, String userId) {
        UserResource userResource = getUserResource(realmId, userId);
        UserRepresentation userRepresentation = userResource.toRepresentation();
        List<AuthorityDto> effectiveRoles = findEffectiveRoleByUserId(realmId, userRepresentation.getId());
        UserDTO userDto = mapUserRepresentationToUserDto(userRepresentation);
        userDto.setAuthorities(new HashSet<>(effectiveRoles));
        return userDto;
    }

    @Override
    public Optional<UserDTO> searchUserByUserName(String realmId, String userName) {
        Optional<UserRepresentation> userRepresentationOptional = getRealmResource(realmId).users().search(userName).stream().findFirst();

        if (!userRepresentationOptional.isPresent()) {
            return Optional.empty();
        }

        UserRepresentation userRepresentation = userRepresentationOptional.get();

        List<AuthorityDto> effectiveRoles = findEffectiveRoleByUserId(realmId, userRepresentation.getId());
        UserDTO userDto = mapUserRepresentationToUserDto(userRepresentation);
        userDto.setAuthorities(new HashSet<>(effectiveRoles));

        return Optional.of(userDto);
    }

    @Override
    public List<AuthorityDto> findEffectiveRoleByUserId(String realmId, String userId) {
        UserResource userResource = getRealmResource(realmId).users().get(userId);

        List<RoleRepresentation> effectiveRoleRepresentations = userResource.roles().realmLevel().listEffective();

        return effectiveRoleRepresentations
            .stream()
            .filter(r -> r.getName().startsWith("ROLE_"))
            .map(au -> AuthorityDto.builder().name(au.getName()).description(au.getDescription()).build())
            .collect(Collectors.toList());
    }

    private UserDTO mapUserRepresentationToUserDto(UserRepresentation userRepresentation) {
        UserDTO userDto = new UserDTO();
        userDto.setId(userRepresentation.getId());
        userDto.setActivated(userRepresentation.isEmailVerified() && userRepresentation.isEnabled());
        userDto.setEmail(userRepresentation.getEmail());
        userDto.setFirstName(userRepresentation.getFirstName());
        userDto.setLastName(userRepresentation.getLastName());
        userDto.setLogin(userRepresentation.getUsername());
        userDto.setLangKey(Constants.DEFAULT_LANGUAGE);
        userDto.setVerifiedEmail(userRepresentation.isEmailVerified());
        userDto.setEnabled(userRepresentation.isEnabled());

        List<String> accountStatus = nonNull(userRepresentation.getAttributes()) ? userRepresentation.getAttributes().get(ACCOUNT_STATUS_FIELD): new ArrayList<>();

        if (!CollectionUtils.isEmpty(accountStatus)) {
            userDto.setAccountStatus(AccountStatus.valueOf(accountStatus.get(0)));
        }
        return userDto;
    }

    @Override
    public boolean deleteRole(String roleId, String realmName) {
        try {
            getRoleResource(realmName).deleteRole(roleId);
            return true;
        } catch (ClientErrorException e) {
            handleClientErrorException(e);
        }
        return false;
    }

    public UserResource getUserResource(String realmId, String userId) {
        return findUsersResource(realmId).get(userId);
    }

    public UsersResource findUsersResource(String realmName) {
        RealmResource realmResource = getRealmResource(realmName);
        return realmResource.users();
    }

    private String findClientUuid(String realmName, String clientId) {
        ClientsResource clientsResource = findClientsResource(realmName);
        return findClientUuid(clientsResource, clientId);
    }

    private String findClientUuid(ClientsResource clientsResource, String clientId) {
        List<ClientRepresentation> clientRepresentations = clientsResource.findByClientId(clientId);
        if (clientRepresentations.isEmpty()) {
            throw new NotFoundException("client not found for clientId '" + clientId + "'");
        }
        return clientRepresentations.stream().findFirst().get().getId();
    }

    private ClientResource findClientResource(String realmName, String clientId) {
        ClientsResource clientsResource = findClientsResource(realmName);
        String clientUuid = findClientUuid(clientsResource, clientId);
        return clientsResource.get(clientUuid);
    }

    private ClientsResource findClientsResource(String realmName) {
        return getRealmResource(realmName).clients();
    }

    private RolesResource getRoleResource(String realmId) {
        return keycloak.realm(realmId).roles();
    }

    private RealmResource getRealmResource(String realmName) {
        return keycloak.realm(realmName);
    }

    private static void handleClientErrorException(ClientErrorException e) {
        e.printStackTrace();
        Response response = e.getResponse();
        try {
            System.out.println("status: " + response.getStatus());
            System.out.println("reason: " + response.getStatusInfo().getReasonPhrase());
            Map error = JsonSerialization.readValue((ByteArrayInputStream) response.getEntity(), Map.class);
            System.out.println("error: " + error.get("error"));
            System.out.println("error_description: " + error.get("error_description"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /// create keycloak client facade

    class ClosableResponseWrapper implements AutoCloseable {
        private final Response response;

        public ClosableResponseWrapper(Response response) {
            this.response = response;
        }

        public Response getResponse() {
            return response;
        }

        public void close() {
            response.close();
        }
    }

    public static KeycloakClientFacadeBuilder builder() {
        return new KeycloakClientFacadeBuilder();
    }
}

package com.vts.clientcenter.service.keycloak;


import com.vts.clientcenter.domain.Authority;
import com.vts.clientcenter.domain.enumeration.AccountStatus;
import com.vts.clientcenter.service.dto.*;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface KeycloakFacade {
    List<Authority> syncRoles(String realm);

    void createRole(Authority authority, String realmName);

    boolean deleteRole(String roleId, String realmName);

    void updateRole(String oldRoleName, String realmName, Authority updateAuthority);

    UserDTO findUserById(String realmId, String userId);

    UserRepresentation getUserRepresentationById(String realmId, String userId);

    Optional<UserDTO> searchUserByUserName(String realmId, String userName);

    List<AuthorityDto> findEffectiveRoleByUserId(String realmId, String userId);

    UserReferenceDto createUser(String realmId, CreateAccountRequest userInfo);

    void deleteUser(String realmId, String userId);

    List<RoleRepresentation> assignUserRole(String realmId, UserDTO userDTO);

    void executeActionEmail(String realmId, String userId, List<String> actions);

    void sendVerifiedEmail(String realmId, String userId);

    void sendResetPasswordEmail(String realmId, String userId);

    void resetPassword(String realmId, String userId, String password);

    UserRepresentation updateUser(String realmId, UpdateAccountRequest userDto);

    RoleDetailResponse createWithCompositeRoles(CreateRoleRequest request, String realmName, String clientUUID);

    RoleDetailResponse updateWithCompositeRoles(CreateRoleRequest dto, String realmApp, String clientUUID);

    void updateUserStatus(AccountStatus accountStatus, String realmId, String userId,  Instant updatedAt);

    void forceApproveAccount(AccountStatus active, String realmApp, String userId,  Instant updatedAt);
}

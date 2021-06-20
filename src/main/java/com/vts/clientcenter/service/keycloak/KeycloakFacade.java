package com.vts.clientcenter.service.keycloak;


import com.vts.clientcenter.domain.Authority;
import com.vts.clientcenter.service.dto.*;

import java.util.List;
import java.util.Optional;

public interface KeycloakFacade {
    List<Authority> syncRoles(String realm);

    boolean createRole(Authority authority, String realmName);

    boolean deleteRole(String roleId, String realmName);

    boolean updateRole(String roleName, String realmName, Authority updateAuthority);

    UserDTO findUserById(String realmId, String userId);

    Optional<UserDTO> searchUserByUserName(String realmId, String userName);

    List<AuthorityDto> findEffectiveRoleByUserId(String realmId, String userId);

    UserReferenceDto createUser(String realmId, CreateAccountRequest userInfo);

    void deleteUser(String realmId, String userId);

    void assignUserRole(String realmId, UserDTO userDTO);

    void executeActionEmail(String realmId, String userId, List<String> actions);

    void sendVerifiedEmail(String realmId, String userId);

    void sendResetPasswordEmail(String realmId, String userId);

    UpdateUserResponse updateUser(String realmId, UserDTO userDto);
}

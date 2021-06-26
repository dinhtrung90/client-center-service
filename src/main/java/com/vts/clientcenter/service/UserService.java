package com.vts.clientcenter.service;

import com.vts.clientcenter.config.Constants;
import com.vts.clientcenter.config.KeycloakConfig;
import com.vts.clientcenter.domain.Authority;
import com.vts.clientcenter.domain.User;
import com.vts.clientcenter.domain.UserProfile;
import com.vts.clientcenter.domain.enumeration.AccountStatus;
import com.vts.clientcenter.domain.enumeration.Gender;
import com.vts.clientcenter.repository.AuthorityRepository;
import com.vts.clientcenter.repository.UserProfileRepository;
import com.vts.clientcenter.repository.UserRepository;
import com.vts.clientcenter.security.SecurityUtils;
import com.vts.clientcenter.service.dto.AuthorityDto;
import com.vts.clientcenter.service.dto.UserDTO;
import com.vts.clientcenter.service.keycloak.KeycloakFacade;
import com.vts.clientcenter.service.mapper.UserMapper;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static com.vts.clientcenter.config.Constants.*;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class UserService {
    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final UserProfileRepository userProfileRepository;

    private final AuthorityRepository authorityRepository;

    private final CacheManager cacheManager;

    private final UserMapper userMapper;

    @Autowired
    private KeycloakConfig setting;

    @Autowired
    @Qualifier("keycloakFacade")
    private KeycloakFacade keycloakFacade;

    public UserService(
        UserRepository userRepository,
        UserProfileRepository userProfileRepository, AuthorityRepository authorityRepository,
        CacheManager cacheManager,
        OktaService oktaService,
        UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
        this.authorityRepository = authorityRepository;
        this.cacheManager = cacheManager;
        this.userMapper = userMapper;
    }

    /**
     * Update basic information (first name, last name, email, language) for the current user.
     *
     * @param firstName first name of user.
     * @param lastName  last name of user.
     * @param email     email id of user.
     * @param langKey   language key.
     */
    public void updateUser(String firstName, String lastName, String email, String langKey) {
        SecurityUtils
            .getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(
                user -> {
                    user.setFirstName(firstName);
                    user.setLastName(lastName);
                    if (email != null) {
                        user.setEmail(email.toLowerCase());
                    }
                    user.setLangKey(langKey);
                    this.clearUserCaches(user);
                    log.debug("Changed Information for User: {}", user);
                }
            );
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> getAllManagedUsers(Pageable pageable) {
        return userRepository.findAllByLoginNot(pageable, Constants.ANONYMOUS_USER).map(userMapper::userToDto);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthoritiesByLogin(String login) {
        return userRepository.findOneWithAuthoritiesByLogin(login);
    }

    /**
     * Gets a list of all the authorities.
     * @return a list of all the authorities.
     */
    @Transactional(readOnly = true)
    public List<String> getAuthorities() {
        return authorityRepository.findAll().stream().map(Authority::getName).collect(Collectors.toList());
    }

    private User syncUserWithIdP(Map<String, Object> details, User user) {
        // save authorities in to sync user roles/groups between IdP and JHipster's local database
//        Collection<String> dbAuthorities = getAuthorities();
//        Collection<String> userAuthorities = user.getAuthorities().stream().map(Authority::getName).collect(Collectors.toList());
//        for (String authority : userAuthorities) {
//            if (!dbAuthorities.contains(authority)) {
//                log.debug("Saving authority '{}' in local database", authority);
//                Authority authorityToSave = new Authority();
//                authorityToSave.setName(authority);
//                authorityRepository.save(authorityToSave);
//            }
//        }
        // save account in to sync users between IdP and JHipster's local database
        Optional<User> existingUser = userRepository.findOneByLogin(user.getLogin());
        if (existingUser.isPresent()) {
            // if IdP sends last updated information, use it to determine if an update should happen
            if (details.get("updated_at") != null) {
                Instant dbModifiedDate = existingUser.get().getLastModifiedDate();
                Instant idpModifiedDate = (Instant) details.get("updated_at");
                if (idpModifiedDate.isAfter(dbModifiedDate)) {
                    log.debug("Updating user '{}' in local database", user.getLogin());
                    updateUser(user.getFirstName(), user.getLastName(), user.getEmail(), user.getLangKey());
                }
                // no last updated info, blindly update
            } else {
                log.debug("Updating user '{}' in local database", user.getLogin());
                updateUser(user.getFirstName(), user.getLastName(), user.getEmail(), user.getLangKey());
            }
        } else {
        }
        return user;
    }

    public static AccountStatus handleAccountStatus(User user) {
        if (!user.hasEnabled()) {
            return AccountStatus.BANNED;
        }
        if (!user.hasVerifiedEmail())  {
            return AccountStatus.INACTIVE;
        }
        //enable and verified email

        if (!user.isApproved()) {
            return AccountStatus.PENDING; // -> waiting approval
        }
        return  AccountStatus.ACTIVE;
    }

    private User syncUserFromKeycloak(Map<String, Object> details, User user) {
        Optional<User> existingUser = userRepository.findOneByLogin(user.getLogin());
        User newUser = new User();
        if (!existingUser.isPresent()) {
            // save account in to sync users between IdP and JHipster's local database
            log.debug("Saving user '{}' in local database", user.getLogin());
            UserRepresentation userRepresentation = mapUserRepresentationToUser(user.getId(), newUser, user.getLogin(), false, Instant.now());

            //sync roles
            Set<Authority> userRoles = syncRolesByUserId(user.getId());
            for (Authority userRole : userRoles) {
                newUser.addAuthority(userRole);
            }
            // sync profile
            UserProfile profile = UserProfile.builder()
                .build();
            mapUserRepresentationToProfile(userRepresentation, profile);
            profile.setUser(newUser);
            userProfileRepository.save(profile);
            this.clearUserCaches(newUser);
        } else {
            newUser = existingUser.get();
            if (details.get(ACCOUNT_UPDATED_AT_FLAG_FIELD) != null) {
                Instant dbModifiedDate = existingUser.get().getLastModifiedDate();
                Instant idpModifiedDate = (Instant) details.get(ACCOUNT_UPDATED_AT_FLAG_FIELD);
                if (idpModifiedDate.isAfter(dbModifiedDate)) {
                    log.debug("Updating user '{}' in local database", user.getLogin());
                    UserRepresentation userRepresentation = mapUserRepresentationToUser(user.getId(), newUser, user.getLogin(), false, idpModifiedDate);
                    UserProfile profile = newUser.getUserProfile();
                    mapUserRepresentationToProfile(userRepresentation, profile);
                    userRepository.save(newUser);
                    this.clearUserCaches(newUser);
                }
            }
            //update  status
            AccountStatus accountStatus = handleAccountStatus(newUser);
            if (!newUser.getAccountStatus().equals(accountStatus)) {
                newUser.setAccountStatus(accountStatus);
                userRepository.save(newUser);
                this.clearUserCaches(newUser);
                keycloakFacade.updateUserStatus(accountStatus, setting.getRealmApp(), newUser.getId(), Instant.now());
            }
        }
        return newUser;
    }

    private Set<Authority>  syncRolesByUserId(String userId) {
        List<AuthorityDto> effectiveRoles = keycloakFacade.findEffectiveRoleByUserId(setting.getRealmApp(), userId);
        List<String> roles = effectiveRoles.stream().map(AuthorityDto::getName).collect(Collectors.toList());
        return authorityRepository.findAllByNameIn(roles);
    }

    private void mapUserRepresentationToProfile(UserRepresentation userRepresentation, UserProfile profile) {
        if (Objects.nonNull(userRepresentation.getAttributes())) {
            List<String> genders = userRepresentation.getAttributes().get(ACCOUNT_GENDER_FIELD);
            if (!CollectionUtils.isEmpty(genders)) {
                profile.setGender(Gender.valueOf(genders.get(0)));
            } else {
                profile.setGender(Gender.Unknown);
            }
            // phones
            List<String> phones = userRepresentation.getAttributes().get(ACCOUNT_PHONE_FIELD);
            if (!CollectionUtils.isEmpty(phones)) {
                profile.setPhone(phones.get(0));
            } else {
                profile.setPhone("Pls provide phone");
            }
        }
    }

    private UserRepresentation mapUserRepresentationToUser(String userId, User newUser,  String createdBy, boolean isUpdated, Instant updateAt) {
        UserRepresentation userRepresentation = keycloakFacade.getUserRepresentationById(setting.getRealmApp(), userId);
        newUser.setId(userRepresentation.getId());
        newUser.setEmail(userRepresentation.getEmail());
        newUser.setFirstName(userRepresentation.getFirstName());
        newUser.setLastName(userRepresentation.getLastName());
        newUser.setLogin(userRepresentation.getUsername());
        newUser.setLangKey(Constants.DEFAULT_LANGUAGE);
        newUser.setHasVerifiedEmail(userRepresentation.isEmailVerified());
        newUser.setHasEnabled(userRepresentation.isEnabled());
        if (!isUpdated) {
            newUser.setCreatedBy(createdBy);
            newUser.setCreatedDate(Instant.now());
        }
        newUser.setLastModifiedDate(updateAt);
        newUser.setLastModifiedBy(createdBy);

        if (Objects.nonNull(userRepresentation.getAttributes())) {
            AccountStatus accountStatus = handleAccountStatus(newUser);
            keycloakFacade.updateUserStatus(accountStatus, setting.getRealmApp(), userId, updateAt);
            newUser.setAccountStatus(accountStatus);
        }
        return userRepresentation;
    }


    /**
     * Returns the user from an OAuth 2.0 login or resource server with JWT.
     * Synchronizes the user in the local repository.
     *
     * @param authToken the authentication token.
     * @return the user from the authentication.
     */
    @Transactional
    public UserDTO getUserFromAuthentication(AbstractAuthenticationToken authToken) {
        Map<String, Object> attributes;
        if (authToken instanceof OAuth2AuthenticationToken) {
            attributes = ((OAuth2AuthenticationToken) authToken).getPrincipal().getAttributes();
        } else if (authToken instanceof JwtAuthenticationToken) {
            attributes = ((JwtAuthenticationToken) authToken).getTokenAttributes();
        } else {
            throw new IllegalArgumentException("AuthenticationToken is not OAuth2 or JWT!");
        }
        // syncUserFromKeycloak
        User user = getUser(attributes);
        User newUser = syncUserFromKeycloak(attributes, user);
        return userMapper.userToDto(newUser);
    }

    private static User getUser(Map<String, Object> details) {
        User user = new User();
        // handle resource server JWT, where sub claim is email and uid is ID
        if (details.get("uid") != null) {
            user.setId((String) details.get("uid"));
            user.setLogin((String) details.get("sub"));
        } else {
            user.setId((String) details.get("sub"));
        }
        if (details.get("preferred_username") != null) {
            user.setLogin(((String) details.get("preferred_username")).toLowerCase());
        } else if (user.getLogin() == null) {
            user.setLogin(user.getId());
        }
        if (details.get("given_name") != null) {
            user.setFirstName((String) details.get("given_name"));
        }
        if (details.get("family_name") != null) {
            user.setLastName((String) details.get("family_name"));
        }
        if (details.get("email_verified") != null) {
            user.setHasVerifiedEmail((Boolean) details.get("email_verified"));
        }

        if (details.get("email") != null) {
            user.setEmail(((String) details.get("email")).toLowerCase());
        } else {
            user.setEmail((String) details.get("sub"));
        }
        if (details.get("langKey") != null) {
            user.setLangKey((String) details.get("langKey"));
        } else if (details.get("locale") != null) {
            // trim off country code if it exists
            String locale = (String) details.get("locale");
            if (locale.contains("_")) {
                locale = locale.substring(0, locale.indexOf('_'));
            } else if (locale.contains("-")) {
                locale = locale.substring(0, locale.indexOf('-'));
            }
            user.setLangKey(locale.toLowerCase());
        } else {
            // set langKey to default if not specified by IdP
            user.setLangKey(Constants.DEFAULT_LANGUAGE);
        }
        //        if (details.get("picture") != null) {
        //            user.setImageUrl((String) details.get("picture"));
        //        }

        if (details.get("account_status") != null) {
            user.setAccountStatus(AccountStatus.valueOf(((String) details.get("account_status")).toUpperCase()));
        } else {
            user.setAccountStatus(AccountStatus.PENDING);
        }
        if (details.get("account_enabled")  != null) {
            user.setHasEnabled((Boolean) details.get("account_enabled"));
        } else {
            user.setHasEnabled(false);
        }
        return user;
    }

    private void clearUserCaches(User user) {
        Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE)).evict(user.getLogin());
        if (user.getEmail() != null) {
            Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE)).evict(user.getEmail());
        }
    }

    public void clearCachesAllUsers() {

        userRepository.findAll()
            .forEach(this::clearUserCaches);
    }


}

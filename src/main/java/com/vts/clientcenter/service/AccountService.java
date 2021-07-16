package com.vts.clientcenter.service;

import com.vts.clientcenter.config.Constants;
import com.vts.clientcenter.config.KeycloakConfig;
import com.vts.clientcenter.domain.*;
import com.vts.clientcenter.domain.enumeration.AccountStatus;
import com.vts.clientcenter.domain.enumeration.ActionsEmail;
import com.vts.clientcenter.events.UserCreatedEvent;
import com.vts.clientcenter.repository.*;
import com.vts.clientcenter.security.SecurityUtils;
import com.vts.clientcenter.service.dto.*;
import com.vts.clientcenter.service.keycloak.KeycloakFacade;
import com.vts.clientcenter.service.mapper.ClientAppMapper;
import com.vts.clientcenter.service.mapper.UserAddressMapper;
import com.vts.clientcenter.service.mapper.UserMapper;
import com.vts.clientcenter.service.mapper.UserProfileMapper;
import com.vts.clientcenter.web.rest.errors.BadRequestAlertException;
import org.mapstruct.ap.internal.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.ws.rs.ClientErrorException;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static com.vts.clientcenter.config.Constants.*;

@Service
@Transactional
public class AccountService {
    private final Logger log = LoggerFactory.getLogger(AccountService.class);

    @Autowired
    private OktaService oktaService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private UserQueryService userQueryService;

    @Autowired
    private KeycloakConfig setting;

    @Autowired
    @Qualifier("keycloakFacade")
    private KeycloakFacade keycloakFacade;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private UserProfileMapper userProfileMapper;

    @Autowired
    private UserAddressMapper userAddressMapper;

    @Autowired
    private UserAddressRepository userAddressRepository;

    @Autowired
    private ClientAppMapper clientAppMapper;

    @Autowired
    private ClientAppRepository clientAppRepository;

    @Transactional

    public UserReferenceDto createUserAccount(CreateAccountRequest request) {

        String userLogin = SecurityUtils.getCurrentUserLogin().orElse(Constants.SYSTEM_ACCOUNT);

        Optional<User> existingUser = userRepository.findOneByEmailIgnoreCase(request.getEmail());

        if (existingUser.isPresent()) {
            throw new BadRequestAlertException("Account has existed.", "User", Constants.USER_HAS_EXISTED);
        }

        UserReferenceDto referenceDto = keycloakFacade.createUser(setting.getRealmApp(), request);

        if (Objects.isNull(referenceDto)) {
            throw new BadRequestAlertException("Can not create user.", "Users", "UserName");
        }

        User user =  new User();
        user.setId(referenceDto.getUserId());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setLogin(request.getLogin());
        user.setAccountStatus(AccountStatus.INACTIVE);
        user.setHasVerifiedEmail(false);
        user.setHasEnabled(true);
        user.setApproved(false);
        user.setLangKey(request.getLangKey() != null ? request.getLangKey() : DEFAULT_LANGUAGE);

        applicationEventPublisher.publishEvent(new UserCreatedEvent(user));

        // create profile
        UserProfile profile = new UserProfile();
        profile.setHomePhone(request.getHomePhone());
        profile.setPhone(request.getMobilePhone());
        profile.setBirthDate(request.getBirthDate().toInstant());
        profile.setGender(request.getGender());
        profile.setUser(user);
        user.setUserProfile(profile);
        userProfileRepository.save(profile);

        List<String> requiredActions = new ArrayList<>();
        requiredActions.add(ActionsEmail.VERIFY_EMAIL.name());
        if (request.isIsTempPassword())  {
            requiredActions.add(ActionsEmail.UPDATE_PASSWORD.name());
        }

        keycloakFacade.executeActionEmail(
            setting.getRealmApp(),
            referenceDto.getUserId(),
            (requiredActions)
        );

        return referenceDto;
    }

    public void removeAccountFromKeycloak(String userId)  {
        keycloakFacade.deleteUser(setting.getRealmApp(), userId);
        Optional<User> userOptional = userRepository.findById(userId);
        userOptional.ifPresent( u -> userRepository.delete(u));
    }

//    private Validator buildValidator() {
//        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
//        return validatorFactory.getValidator();
//    }

    @Transactional
    public UserFullInfoResponse getAccount(String userId) {

        User user = validateUserId(userId);

        UserDTO keycloakUser = keycloakFacade.findUserById(setting.getRealmApp(), userId);

        getAccountStatusFromKeycloak(keycloakUser, user);

        userRepository.save(user);
        this.clearUserCaches(user);

        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfileRepository.getOne(userId));

        return UserFullInfoResponse.builder()
            .userDto(userMapper.userToDto(user))
            .userProfileDto(userProfileDTO)
            .build();
    }

    @Transactional(readOnly = true)
    public List<String> getAuthorities() {
        return authorityRepository.findAll().stream().map(Authority::getName).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> getAccounts(UserCriteria criteria, Pageable pageable) {
        return userQueryService.findByCriteria(criteria, pageable);
    }

    private void clearUserCaches(User user) {
        Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE)).evict(user.getLogin());
        if (user.getEmail() != null) {
            Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE)).evict(user.getEmail());
        }
    }

    public ApiResponse setRequiredActions(String userId, List<String> actions) {

        UserDTO userDto = keycloakFacade.findUserById(setting.getRealmApp(), userId);
        if (Objects.isNull(userDto)) {
            throw new BadRequestAlertException("Account not found.", "User", Constants.USER_NOT_FOUND);
        }

        keycloakFacade.executeActionEmail(setting.getRealmApp(), userId, actions);

        return ApiResponse.builder().response(userDto).isIsSuccess(true).build();
    }

    public ApiResponse resendVerifyEmail(String userId) {

        UserDTO userDto = keycloakFacade.findUserById(setting.getRealmApp(), userId);
        if (Objects.isNull(userDto)) {
            throw new BadRequestAlertException("Account not found.", "User", Constants.USER_NOT_FOUND);
        }


        boolean isSuccess = true;
        String message = "Sending email Successfully";
        HttpStatus statusCode = HttpStatus.OK;
        try {
            keycloakFacade.sendVerifiedEmail(setting.getRealmApp(), userId);
        } catch (ClientErrorException e) {
            e.printStackTrace();
            isSuccess = false;
            message = e.getMessage();
            statusCode = HttpStatus.BAD_REQUEST;
        }

        ResendVerifyEmailResponse res = ResendVerifyEmailResponse.builder()
            .userId(userId)
            .email(userDto.getEmail()).build();

        return ApiResponse.builder().response(res).isIsSuccess(isSuccess).message(message).statusCode(statusCode).build();
    }

    public ApiResponse resetPasswordUser(String userId) {

        UserDTO userDto = keycloakFacade.findUserById(setting.getRealmApp(), userId);
        if (Objects.isNull(userDto)) {
            throw new BadRequestAlertException("Account not found.", "User", Constants.USER_NOT_FOUND);
        }

        boolean isSuccess = true;
        String message = "Reset Password Successfully";
        HttpStatus statusCode = HttpStatus.OK;
        try {
            keycloakFacade.sendResetPasswordEmail(setting.getRealmApp(), userId);
        } catch (ClientErrorException e) {
            e.printStackTrace();
            isSuccess = false;
            message = e.getMessage();
            statusCode = HttpStatus.BAD_REQUEST;
        }

        ResendVerifyEmailResponse res = ResendVerifyEmailResponse.builder()
            .userId(userId)
            .email(userDto.getEmail()).build();

        return ApiResponse.builder().response(res).isIsSuccess(isSuccess).message(message).statusCode(statusCode).build();
    }

    @Transactional
    public UserDTO updateUserInfo(UpdateAccountRequest userDto) {

        String createdBy = SecurityUtils.getCurrentUserLogin().orElse(SYSTEM_ACCOUNT);

        User user = validateUserId(userDto.getUserId());

        if (Objects.isNull(userDto.getAccountStatus())){
            throw new BadRequestAlertException("User Status Not Null.", "USER", Constants.USER_STATUS_NOT_NULL);
        }

        // update info local
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setLastModifiedBy(createdBy);
        user.setLastModifiedDate(Instant.now());
        user.setLangKey(userDto.getLangKey() != null ? userDto.getLangKey() : DEFAULT_LANGUAGE);
        getAccountStatusByNewStatus(userDto.getAccountStatus(), user);

        UserProfile profile = user.getUserProfile();
        profile.setGender(userDto.getGender());
        profile.setPhone(userDto.getMobilePhone());
        profile.setBirthDate(userDto.getBirthDate().toInstant());
        profile.setHomePhone(userDto.getMobilePhone());
        profile.setLastModifiedBy(createdBy);
        profile.setLastModifiedDate(Instant.now());

        keycloakFacade.updateUser(setting.getRealmApp(), user, userDto.getTempPassword(), userDto.isIsTempPassword());

        userRepository.save(user);
        clearUserCaches(user);
        return userMapper.userToDto(user);
    }

    private void getAccountStatusFromKeycloak(UserDTO keycloakUser, User user) {
        AccountStatus targetAccountStatus = UserService.getAccountStatus(keycloakUser.isEnabled(), keycloakUser.isVerifiedEmail(), keycloakUser.isApproved());
        getAccountStatusByNewStatus(targetAccountStatus, user);
    }

    private void getAccountStatusByNewStatus(AccountStatus targetAccountStatus, User user) {
        if (!user.getAccountStatus().equals(targetAccountStatus)) {
            // handle update account status
            switch (targetAccountStatus) {
                case ACTIVE:
                    user.setHasEnabled(true);
                    user.setHasVerifiedEmail(true);
                    user.setApproved(true);
                    break;
                case PENDING:
                    user.setHasEnabled(true);
                    user.setHasVerifiedEmail(true);
                    user.setApproved(false);
                    break;
                case INACTIVE:
                    user.setHasEnabled(true);
                    user.setHasVerifiedEmail(false);
                    user.setApproved(false);
                    break;
                case BANNED:
                    user.setHasEnabled(false);
                    user.setHasVerifiedEmail(false);
                    user.setApproved(false);
                    break;
            }
            //TODO: send notification email for users
            user.setAccountStatus(targetAccountStatus);
            keycloakFacade.updateUserStatus(targetAccountStatus, setting.getRealmApp(), user.getId(), Instant.now());
        }
    }

    public UserFullInfoResponse approveAccount(String userId) {

        String createdBy = SecurityUtils.getCurrentUserLogin().orElse(SYSTEM_ACCOUNT);

        User user = validateUserId(userId);

        UserFullInfoResponse response = UserFullInfoResponse.builder()
            .build();


        if (!user.hasEnabled() && !SecurityUtils.isCurrentUserInRole(ROLE_SUPER_ADMIN)) {
            throw new BadRequestAlertException("User can not be approved, this account banned by admin", "User", userId);
        }
        if (!user.hasVerifiedEmail() && !SecurityUtils.isCurrentUserInRole(ROLE_SUPER_ADMIN))  {
            throw new BadRequestAlertException("User can not be approved. this account need verify email before", "User", userId);
        }
        //enable and verified email
        boolean isForceUpdate = user.hasEnabled() && user.hasVerifiedEmail();
        user.setAccountStatus(AccountStatus.ACTIVE);
        user.setLastModifiedBy(createdBy);
        user.setLastModifiedDate(Instant.now());
        if (!isForceUpdate) {
            user.setHasEnabled(true);
            user.setHasVerifiedEmail(true);
        }
        user.setApproved(true);

        keycloakFacade.forceApproveAccount(AccountStatus.ACTIVE, setting.getRealmApp(), userId, Instant.now(), !isForceUpdate && SecurityUtils.isCurrentUserInRole(ROLE_SUPER_ADMIN));

        userRepository.save(user);
        this.clearUserCaches(user);

        response.setUserDto(userMapper.userToDto(user));
        response.setUserProfileDto(userProfileMapper.toDto(user.getUserProfile()));
        return response;
    }

    @Transactional(readOnly = true)
    public Page<UserAddressDTO> getAddressesByUserId(String userId, Pageable pageable) {

        validateUserId(userId);

        return userAddressRepository.findAllByUserId(userId, pageable).map(u -> userAddressMapper.toDto(u));
    }

    private User validateUserId(String userId) {
        if (Objects.isNull(userId)) {
            throw new BadRequestAlertException("User Not Found", "USER", Constants.ID_NOT_NULL);
        }

        Optional<User> existingUser = userRepository.findById(userId);

        if (!existingUser.isPresent()) {
            throw new BadRequestAlertException("User Not Found", "USER", Constants.USER_NOT_FOUND);
        }
        return existingUser.get();
    }


    @Transactional
    public UserAddressDTO createUserAddress(UserAddressDTO addressDTO) {

        String createdBy = SecurityUtils.getCurrentUserLogin().orElse(SYSTEM_ACCOUNT);
        User user = validateUserId(addressDTO.getUserId());
        UserAddress userAddress = userAddressMapper.toEntity(addressDTO);
        userAddress.setCreatedBy(createdBy);
        userAddress.setCreatedDate(Instant.now());
        userAddress.setLastModifiedDate(Instant.now());
        userAddress.setLastModifiedBy(createdBy);
        userAddress.setUser(user);
        UserAddress savingAddress = userAddressRepository.save(userAddress);
        UserAddressDTO dto = userAddressMapper.toDto(savingAddress);
        this.clearUserCaches(user);
        return dto;
    }

    @Transactional
    public UserAddressDTO updateUserAddress(String userId, UserAddressDTO addressDTO) {

        User user = validateUserId(userId);

        Optional<UserAddress> userAddressOptional = userAddressRepository.findById(addressDTO.getId());

        if (!userAddressOptional.isPresent()) {
            throw new BadRequestAlertException("User Address Not Found", "USER", Constants.USER_ADDRESS_NOT_FOUND);
        }

        String createdBy = SecurityUtils.getCurrentUserLogin().orElse(SYSTEM_ACCOUNT);
        UserAddress userAddress = userAddressMapper.toEntity(addressDTO);
        userAddress.setLastModifiedDate(Instant.now());
        userAddress.setLastModifiedBy(createdBy);

        UserAddressDTO userAddressDTO = userAddressMapper.toDto(userAddressRepository.save(userAddress));
        this.clearUserCaches(user);
        return userAddressDTO;
    }

    public Optional<UserAddressDTO>  getUserAddress(String userId, Long addressId) {
        validateUserId(userId);
        return userAddressRepository.findById(addressId).map(a -> userAddressMapper.toDto(a));
    }

    @Transactional
    public void deleteUserAddress(String userId, Long addressId) {
        User user = validateUserId(userId);
        userAddressRepository.deleteById(addressId);
        this.clearUserCaches(user);
    }

    @Transactional
    public UserRoleMappingResponse createUserRoleMapping(String userId, UserRoleMappingRequest request) {

        User user = validateUserId(userId);

        List<Authority> assignRoles = new ArrayList<>();
        if (!CollectionUtils.isEmpty(request.getAssignRoles())) {
            assignRoles = new ArrayList<>(authorityRepository.findAllByNameIn(request.getAssignRoles()));
        }

        List<Authority> clientRoles = new ArrayList<>();
        List<ClientApp> clientApps = clientAppMapper.toEntity(request.getAccessibleApps());
        if (!CollectionUtils.isEmpty(clientApps)) {
            for (ClientApp clientApp : clientApps) {
                ClientApp clientObject = clientAppRepository.fetchById(clientApp.getId());
                clientRoles.addAll(clientObject.getAuthorities());
            }
        }

        List<Authority> effectiveRoles = keycloakFacade.updateUserRoleMapping( setting.getRealmApp(), userId, assignRoles, clientApps);

        Set<Authority> authorityEntities = authorityRepository.getAllByNameIn(effectiveRoles.stream().map(Authority::getName).collect(Collectors.toList()));

        List<Authority> effectiveTotal = Collections.join(new ArrayList<>(authorityEntities), clientRoles);

        //local roles
        List<Authority> authorities = effectiveTotal.stream().filter(r -> r.getName().startsWith("ROLE_")).collect(Collectors.toList());
        user.addAuthorities(authorities);

        userRepository.save(user);
        this.clearUserCaches(user);
        return UserRoleMappingResponse.builder()
            .userId(userId)
            .effectiveRoles(authorities.stream().map(Authority::getName).collect(Collectors.toList()))
            .build();
    }

    public void terminateAccount(String userId, boolean isTerminated) {
        User user = validateUserId(userId);
        user.setTerminated(isTerminated);
        user.setHasEnabled(!isTerminated);
        AccountStatus accountStatus = UserService.handleAccountStatus(user);
        user.setAccountStatus(accountStatus);
        keycloakFacade.updateUser(setting.getRealmApp(), user, null, false);
        userRepository.save(user);
        this.clearUserCaches(user);
        //TODO: send notifications
    }
}

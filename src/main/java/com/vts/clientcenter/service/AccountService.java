package com.vts.clientcenter.service;

import com.okta.sdk.resource.user.UserActivationToken;
import com.vts.clientcenter.config.Constants;
import com.vts.clientcenter.config.KeycloakConfig;
import com.vts.clientcenter.domain.Authority;
import com.vts.clientcenter.domain.User;
import com.vts.clientcenter.domain.UserAddress;
import com.vts.clientcenter.domain.UserProfile;
import com.vts.clientcenter.domain.enumeration.AccountStatus;
import com.vts.clientcenter.domain.enumeration.ActionsEmail;
import com.vts.clientcenter.domain.enumeration.Gender;
import com.vts.clientcenter.events.UserCreatedEvent;
import com.vts.clientcenter.helpers.DateUtil;
import com.vts.clientcenter.repository.AuthorityRepository;
import com.vts.clientcenter.repository.UserAddressRepository;
import com.vts.clientcenter.repository.UserProfileRepository;
import com.vts.clientcenter.repository.UserRepository;
import com.vts.clientcenter.security.SecurityUtils;
import com.vts.clientcenter.service.dto.*;
import com.vts.clientcenter.service.keycloak.KeycloakFacade;
import com.vts.clientcenter.service.mapper.UserAddressMapper;
import com.vts.clientcenter.service.mapper.UserMapper;
import com.vts.clientcenter.service.mapper.UserProfileMapper;
import com.vts.clientcenter.web.rest.errors.BadRequestAlertException;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
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

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.ws.rs.ClientErrorException;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

        //insert local
        Optional<UserDTO> userDTOOptional = keycloakFacade.searchUserByUserName(setting.getRealmApp(), request.getLogin());

        if (!userDTOOptional.isPresent()) {
            throw new BadRequestAlertException("Can not get user.", "Users", "UserName");
        }

        UserDTO userDTO = userDTOOptional.get();
        userDTO.setAuthorities(request.getAuthorities());
        userDTO.setApproved(false); // force false when create account.
        List<RoleRepresentation> allRoles = keycloakFacade.assignUserRole(setting.getRealmApp(), userDTOOptional.get());

        User user = userMapper.userDTOToUser(userDTO);
        Set<Authority> userRoles = authorityRepository.findAllByNameIn(allRoles.stream().map(RoleRepresentation::getName).collect(Collectors.toList()));
        user.setAuthorities(userRoles);
        applicationEventPublisher.publishEvent(new UserCreatedEvent(user));

        // create profile
        UserProfile profile = Objects.nonNull(request.getUserProfileDto()) ?
            userProfileMapper.toEntity(request.getUserProfileDto()) : new UserProfile();

        profile.setCreatedBy(userLogin);
        profile.setCreatedDate(Instant.now());
        profile.setLastModifiedBy(userLogin);
        profile.setLastModifiedDate(Instant.now());
        profile.setUser(user);
        userProfileRepository.save(profile);

        List<String> requiredActions = new ArrayList<>();
        requiredActions.add(ActionsEmail.VERIFY_EMAIL.name());
        if (request.isIsTempPassword())  {
            requiredActions.add(ActionsEmail.UPDATE_PASSWORD.name());
        }
        keycloakFacade.executeActionEmail(
            setting.getRealmApp(),
            userDTOOptional.get().getId(),
            (requiredActions)
        );

        keycloakFacade.sendVerifiedEmail(setting.getRealmApp(), user.getId());

        referenceDto.setUserId(user.getId());

        List<UserAddress> createObjects = request
            .getUserAddressList()
            .stream()
            .filter(userAddressDTO -> Objects.isNull(userAddressDTO.getId()))
            .flatMap(
                dto -> {
                    dto.setLastModifiedDate(Instant.now());
                    dto.setLastModifiedBy(userLogin);
                    dto.setCreatedBy(userLogin);
                    dto.setCreatedDate(Instant.now());
                    dto.setUserId(user.getId());
                    return Stream.of(userAddressMapper.toEntity(dto));
                }
            )
            .collect(Collectors.toList());

        userAddressRepository.saveAll(createObjects.stream().peek(u -> u.setUser(user)).collect(Collectors.toList()));

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

    @Transactional(readOnly = true)
    public UserFullInfoResponse getAccount(String userId) {

        Optional<User> userOptional = userRepository.findByUserId(userId);
        if (!userOptional.isPresent()) {
            throw new BadRequestAlertException("User Not Found", "USER", Constants.USER_NOT_FOUND);
        }

        User user = userOptional.get();

        UserDTO userDTO = userMapper.userToUserDTO(user);

        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfileRepository.getOne(userId));

        List<UserAddress> userAddresses = new ArrayList<>(user.getUserAddresses());

        List<UserAddressDTO> dtoList = userAddressMapper.toDto(userAddresses);

        return UserFullInfoResponse.builder()
            .userAddressList(dtoList)
            .userDto(userDTO)
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

        if (Objects.isNull(userDto.getUserId())) {
            throw new BadRequestAlertException("User Not Found", "USER", Constants.ID_NOT_NULL);
        }

        Optional<User> existingUser = userRepository.findById(userDto.getUserId());

        if (!existingUser.isPresent()) {
            throw new BadRequestAlertException("User Not Found", "USER", Constants.USER_NOT_FOUND);
        }

        if (Objects.isNull(userDto.getAccountStatus())){
            throw new BadRequestAlertException("User Status Not Null.", "USER", Constants.USER_STATUS_NOT_NULL);
        }

        User user = existingUser.get();

        if (!user.getAccountStatus().equals(userDto.getAccountStatus())) {
            // handle update account status
            switch (userDto.getAccountStatus()) {
                case ACTIVE:
                    userDto.setEnable(true);
                    userDto.setVerifiedEmail(true);
                    userDto.setApproved(true);
                    break;
                case PENDING:
                    userDto.setEnable(true);
                    userDto.setVerifiedEmail(true);
                    userDto.setApproved(false);
                    break;
                case INACTIVE:
                    userDto.setEnable(true);
                    userDto.setVerifiedEmail(false);
                    userDto.setApproved(false);
                    break;
                case BANNED:
                    userDto.setEnable(false);
                    userDto.setVerifiedEmail(false);
                    userDto.setApproved(false);
                    break;
            }
            //TODO: send notification email for users
        }

        keycloakFacade.updateUser(setting.getRealmApp(), userDto);

        // update info local
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setHasVerifiedEmail(userDto.isVerifiedEmail());
        user.setHasEnabled(userDto.isEnable());
        user.setApproved(userDto.isApproved());
        user.setAccountStatus(userDto.getAccountStatus());
        user.setLastModifiedBy(createdBy);
        user.setLastModifiedDate(Instant.now());

        UserProfile profile = user.getUserProfile();
        profile.setGender(userDto.getGender());
        profile.setPhone(userDto.getMobilePhone());
        profile.setBirthDate(userDto.getBirthDate().toInstant());
        profile.setHomePhone(userDto.getMobilePhone());

        userRepository.save(user);
        clearUserCaches(user);
        return userMapper.userToDto(user);
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
        response.setUserAddressList(userAddressMapper.toDto(new ArrayList<>(user.getUserAddresses())));
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
}

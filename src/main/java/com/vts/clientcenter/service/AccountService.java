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

    @Autowired
    private DateUtil dateUtil;

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

        List<String> requiredActions = Arrays.asList(ActionsEmail.VERIFY_EMAIL.name());
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
    public UserFullInfoResponse updateUser(UpdateAccountRequest userDto) {

        String createdBy = SecurityUtils.getCurrentUserLogin().orElse(SYSTEM_ACCOUNT);
        if (Objects.isNull(userDto.getUserId())) {
            throw new BadRequestAlertException("User Not Found", "USER", Constants.ID_NOT_NULL);
        }

        Optional<User> existingUser = userRepository.findById(userDto.getUserId());

        if (!existingUser.isPresent()) {
            throw new BadRequestAlertException("User Not Found", "USER", Constants.USER_NOT_FOUND);
        }

        if (CollectionUtils.isEmpty(userDto.getAuthorities())) {
            throw new BadRequestAlertException("Role can not empty.", "USER", Constants.USER_ROLE_NOT_FOUND);
        }
        UserFullInfoResponse response = UserFullInfoResponse.builder()
            .build();
        UserRepresentation userRepresentation = keycloakFacade.updateUser(setting.getRealmApp(), userDto);

        // update info local
        User user = existingUser.get();
        mapUserRepresentationToUser(userRepresentation, user, createdBy);

        // update roles
        List<AuthorityDto> effectiveRoles = keycloakFacade.findEffectiveRoleByUserId(setting.getRealmApp(), userDto.getUserId());
        List<String> roles = effectiveRoles.stream().map(AuthorityDto::getName).collect(Collectors.toList());
        Set<Authority> authorities = user.getAuthorities();
        List<String> currentUserRoles = authorities.stream().map(Authority::getName).collect(Collectors.toList());
        boolean match = currentUserRoles.containsAll(roles);
        if (!match) {

            Set<Authority> savingRoles = authorityRepository.findAllByNameIn(roles);

            //remove not in list local
            authorities.stream()
                .filter(r -> !roles.contains(r.getName()))
                .forEach(user::removeAuthority);

            for (Authority savingRole : savingRoles) {
                if (!currentUserRoles.contains(savingRole.getName())) {
                    user.addAuthority(savingRole);
                }
            }
        }

        response.setUserDto(userMapper.userToDto(user));

        userRepository.save(user);

        // update address
        List<UserAddress> userAddresses = userAddressMapper.toEntity(userDto.getUserAddressList());
        Set<UserAddress> userAddressesLocal = user.getUserAddresses();
        if (!userAddressesLocal.containsAll(userAddresses)) {

            List<Long> deleteItems = userAddressesLocal.stream()
                .filter(p -> !userAddresses.stream().map(UserAddress::getId).collect(Collectors.toList()).contains(p.getId()))
                .map(UserAddress::getId).collect(Collectors.toList());

            userAddressRepository.deleteAllByIdIn(deleteItems);

            userAddressRepository.saveAll(userAddresses);
        }
        response.setUserProfileDto(userProfileMapper.toDto(user.getUserProfile()));
        response.setUserAddressList(userAddressMapper.toDto(new ArrayList<>(userAddressRepository.findAllByUserId(user.getId()))));
        clearUserCaches(user);
        return response;
    }

    private void mapUserRepresentationToUser(UserRepresentation userRepresentation, User user, String createdBy) {

        user.setId(userRepresentation.getId());
        user.setEmail(userRepresentation.getEmail());
        user.setFirstName(userRepresentation.getFirstName());
        user.setLastName(userRepresentation.getLastName());
        user.setLogin(userRepresentation.getUsername());
        user.setHasVerifiedEmail(userRepresentation.isEmailVerified());
        user.setHasEnabled(userRepresentation.isEnabled());
        user.setLastModifiedBy(createdBy);

        UserProfile profile = user.getUserProfile();
        if (Objects.nonNull(userRepresentation.getAttributes())) {
            AccountStatus accountStatus = UserService.handleAccountStatus(user);
            user.setAccountStatus(accountStatus);
            keycloakFacade.updateUserStatus(accountStatus, setting.getRealmApp(), user.getId(), Instant.now());
        }

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

        List<String> updateAtStrings = userRepresentation.getAttributes().get(ACCOUNT_UPDATED_AT_FLAG_FIELD);
        if (!CollectionUtils.isEmpty(phones)) {
            String updatedAtFlag = updateAtStrings.get(0);
            Date updatedAt = dateUtil.parse(updatedAtFlag, DATE_STANDARD_FORMAT);
            user.setLastModifiedDate(updatedAt.toInstant());
        } else {
            user.setLastModifiedDate(Instant.now());
        }

    }

    public UserFullInfoResponse approveAccount(String userId) {
        String createdBy = SecurityUtils.getCurrentUserLogin().orElse(SYSTEM_ACCOUNT);
        if (Objects.isNull(userId)) {
            throw new BadRequestAlertException("User Not Found", "USER", Constants.ID_NOT_NULL);
        }

        Optional<User> existingUser = userRepository.findById(userId);

        if (!existingUser.isPresent()) {
            throw new BadRequestAlertException("User Not Found", "USER", Constants.USER_NOT_FOUND);
        }
        User user = existingUser.get();
        UserFullInfoResponse response = UserFullInfoResponse.builder()
            .build();


        if (!user.hasEnabled() && !SecurityUtils.isCurrentUserInRole(ROLE_SUPER_ADMIN)) {
            throw new BadRequestAlertException("User can not be approved, this account banned by admin", "User", userId);
        }
        if (!user.hasVerifiedEmail() && !SecurityUtils.isCurrentUserInRole(ROLE_SUPER_ADMIN))  {
            throw new BadRequestAlertException("User can not be approved. this account need verify email before", "User", userId);
        }
        //enable and verified email
        boolean isValidAccount = user.hasEnabled() && user.hasVerifiedEmail();
        user.setAccountStatus(AccountStatus.ACTIVE);
        user.setLastModifiedBy(createdBy);
        user.setLastModifiedDate(Instant.now());
        if (isValidAccount) {
            keycloakFacade.updateUserStatus(AccountStatus.ACTIVE, setting.getRealmApp(), userId, Instant.now());
        } else {
            keycloakFacade.forceApproveAccount(AccountStatus.ACTIVE, setting.getRealmApp(), userId, Instant.now());
            user.setHasEnabled(true);
            user.setHasVerifiedEmail(true);
        }

        userRepository.save(user);
        this.clearUserCaches(user);

        response.setUserDto(userMapper.userToDto(user));
        response.setUserProfileDto(userProfileMapper.toDto(user.getUserProfile()));
        response.setUserAddressList(userAddressMapper.toDto(new ArrayList<>(user.getUserAddresses())));
        return response;
    }
}

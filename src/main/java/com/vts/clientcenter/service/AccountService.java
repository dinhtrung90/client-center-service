package com.vts.clientcenter.service;

import com.okta.sdk.resource.user.UserActivationToken;
import com.vts.clientcenter.config.Constants;
import com.vts.clientcenter.config.KeycloakConfig;
import com.vts.clientcenter.domain.Authority;
import com.vts.clientcenter.domain.User;
import com.vts.clientcenter.domain.UserAddress;
import com.vts.clientcenter.domain.UserProfile;
import com.vts.clientcenter.domain.enumeration.ActionsEmail;
import com.vts.clientcenter.events.UserCreatedEvent;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AbstractAuthenticationToken;
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
        keycloakFacade.assignUserRole(setting.getRealmApp(), userDTOOptional.get());

        User user = userMapper.userDTOToUser(userDTO);
        Set<Authority> userRoles = authorityRepository.findAllByNameIn(userDTO.getAuthorities().stream().map(AuthorityDto::getName).collect(Collectors.toList()));
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

        keycloakFacade.executeActionEmail(
            setting.getRealmApp(),
            userDTOOptional.get().getId(),
            (Arrays.asList(ActionsEmail.UPDATE_PASSWORD.name(), ActionsEmail.VERIFY_EMAIL.name()))
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

    @Transactional
    public UserDTO updateAccount(UserDTO userDTO) throws Exception {


        if (Objects.isNull(userDTO.getId())) {
            throw new BadRequestAlertException("User Not Found", "USER", Constants.ID_NOT_NULL);
        }

        Optional<User> existingUser = userRepository.findOneByLogin(userDTO.getLogin());

        if (!existingUser.isPresent()) {
            throw new BadRequestAlertException("User Not Found", "USER", Constants.USER_NOT_FOUND);
        }

        if (CollectionUtils.isEmpty(userDTO.getAuthorities())) {
            throw new BadRequestAlertException("Role can not empty.", "USER", Constants.USER_ROLE_NOT_FOUND);
        }

        return userDTO;
    }

    private Validator buildValidator() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        return validatorFactory.getValidator();
    }

    @Transactional
    public ActivatedPayload activateAccount(String userId) throws Exception {
        String login = SecurityUtils.getCurrentUserLogin().orElse(Constants.SYSTEM_ACCOUNT);
        if (Objects.isNull(userId)) {
            throw new BadRequestAlertException("User Not Found", "USER", Constants.ID_NOT_NULL);
        }

        Optional<User> existingUser = userRepository.findById(userId);

        User user = existingUser.orElseThrow(() -> new BadRequestAlertException("User not found", "User", Constants.USER_NOT_FOUND));

        UserActivationToken account = oktaService.activateAccount(user.getId());

        user.setLastModifiedBy(login);

        user.setLastModifiedDate(Instant.now());

        user.setActivated(false);

        userRepository.save(user);

        this.clearUserCaches(user);

        return ActivatedPayload.builder().success(true).userId(userId).build();
    }

    public UserDTO getAccount(String userId) {
        return null;
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


}

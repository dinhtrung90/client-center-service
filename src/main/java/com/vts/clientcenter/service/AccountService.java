package com.vts.clientcenter.service;

import com.okta.sdk.resource.user.UserActivationToken;
import com.vts.clientcenter.config.Constants;
import com.vts.clientcenter.domain.Authority;
import com.vts.clientcenter.domain.User;
import com.vts.clientcenter.domain.UserAddress;
import com.vts.clientcenter.domain.UserProfile;
import com.vts.clientcenter.events.UserCreatedEvent;
import com.vts.clientcenter.helpers.PasswordGenerator;
import com.vts.clientcenter.repository.AuthorityRepository;
import com.vts.clientcenter.repository.UserAddressRepository;
import com.vts.clientcenter.repository.UserProfileRepository;
import com.vts.clientcenter.repository.UserRepository;
import com.vts.clientcenter.security.AuthoritiesConstants;
import com.vts.clientcenter.security.SecurityUtils;
import com.vts.clientcenter.service.dto.ActivatedPayload;
import com.vts.clientcenter.service.dto.UserAddressDTO;
import com.vts.clientcenter.service.dto.UserDTO;
import com.vts.clientcenter.service.dto.UserProfileDTO;
import com.vts.clientcenter.service.mapper.UserAddressMapper;
import com.vts.clientcenter.service.mapper.UserProfileMapper;
import com.vts.clientcenter.web.rest.AccountResource;
import com.vts.clientcenter.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.security.RandomUtil;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private UserAddressMapper userAddressMapper;

    @Autowired
    private UserAddressRepository userAddressRepository;

    @Autowired
    private UserProfileMapper userProfileMapper;


    @Transactional
    public UserDTO createUserAccount(UserDTO userDTO) throws Exception {

        Optional<User> existingUser = userRepository.findOneByEmailIgnoreCase(userDTO.getEmail());

        if (existingUser.isPresent()) {
            throw new BadRequestAlertException("Account has existed.", "User", Constants.USER_HAS_EXISTED);
        }

        String password = PasswordGenerator.generateRandomPassword(8);

        com.okta.sdk.resource.user.User oktaUser = oktaService.createOktaAccount(userDTO, password);

        // send email to users
        UserActivationToken activate = oktaUser.activate(true);


        String loginUser = SecurityUtils.getCurrentUserLogin().orElse(Constants.SYSTEM_ACCOUNT);

        User user = new User();
        user.setLangKey(userDTO.getLangKey() != null ? userDTO.getLangKey() : Constants.DEFAULT_LANGUAGE);
        user.setLogin(userDTO.getEmail());
        user.setActivated(false);
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setCreatedDate(Instant.now());
        user.setLastModifiedBy(loginUser);
        user.setLastModifiedDate(Instant.now());
        user.setCreatedBy(loginUser);
        user.setActivationKey(activate.getActivationToken());
        user.setActivationUrl(activate.getActivationUrl());
        user.setStatus(oktaUser.getStatus());

        //add role for users
        Collection<String> dbAuthorities = getAuthorities();

        if (CollectionUtils.isEmpty(userDTO.getAuthorities())) {

            userDTO.getAuthorities().add(AuthoritiesConstants.USER);

        } else {

            Optional<String> userRoleOptional = userDTO.getAuthorities().stream()
                .filter(p -> p.equalsIgnoreCase(AuthoritiesConstants.USER))
                .findFirst();

            if (!userRoleOptional.isPresent()) {
                userDTO.getAuthorities().add(AuthoritiesConstants.USER);
            }
        }

        List<Authority> authorities = new ArrayList<>();
        for (String authority : userDTO.getAuthorities()) {
            if (dbAuthorities.contains(authority)) {
                Authority authorityToSave = new Authority();
                authorityToSave.setName(authority);
                authorities.add(authorityToSave);
            }
        }
        user.setAuthorities(new HashSet<>(authorities));

        userDTO.setId(oktaUser.getId());

        user.setId(oktaUser.getId());

        //TODO publish user to notification
        applicationEventPublisher.publishEvent(new UserCreatedEvent(user));

        //add profile user
        UserProfile userProfile;
        if (Objects.nonNull(userDTO.getUserProfileDto())) {

            UserProfileDTO userProfileDto = userDTO.getUserProfileDto();

            userProfile = new UserProfile()
                .birthDate(userProfileDto.getBirthDate())
                .createdBy(loginUser)
                .lastModifiedBy(loginUser)
                .lastModifiedDate(Instant.now())
                .createdDate(Instant.now())
                .user(user)
                .gender(userProfileDto.getGender())
                .phone(userDTO.getPhone());

        } else {
            userProfile = new UserProfile()
                .createdBy(loginUser)
                .lastModifiedBy(loginUser)
                .lastModifiedDate(Instant.now())
                .user(user)
                .phone(userDTO.getPhone())
                .createdDate(Instant.now());
        }

        // add address
        if (!CollectionUtils.isEmpty(userDTO.getUserAddressList())) {

            List<UserAddressDTO> userAddressList = userDTO.getUserAddressList();
            for (UserAddressDTO a : userAddressList) {
                a.setCreatedBy(loginUser);
                a.setLastModifiedBy(loginUser);
                a.setCreatedDate(Instant.now());
                a.setLastModifiedDate(Instant.now());
            }

            List<UserAddress> userAddresses = userAddressMapper.toEntity(userAddressList);

            for (UserAddress userAddress : userAddresses) {
                user.addUserAddress(userAddress);
            }

        } else {

            user.setUserAddresses(new HashSet<>(new ArrayList<>()));
        }

        Validator validator = buildValidator();
        Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);

        if (CollectionUtils.isEmpty(constraintViolations)) {

            User savingUser = userRepository.save(user);
            userProfile.setUser(savingUser);
            userProfileRepository.save(userProfile);

            UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);
            userDTO.setUserProfileDto(userProfileDTO);

            userDTO.setUserAddressList(userAddressMapper.toDto(new ArrayList<>(savingUser.getUserAddresses())));

        } else {

            oktaService.removeAccount(userDTO.getId());
            throw new BadRequestAlertException("User invalid", "UserValidator", Constants.USER_VALIDATOR_ERR);
        }

        return userDTO;
    }

    @Transactional
    public UserDTO updateAccount(UserDTO userDTO) throws Exception {
        String login = SecurityUtils.getCurrentUserLogin().orElse("System");
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

        com.okta.sdk.resource.user.User account = oktaService.updateAccount(userDTO);

        User user = existingUser.get();
        user.setLangKey(userDTO.getLangKey());
        user.setLogin(userDTO.getEmail());
        user.setActivated(account.getActivated() != null);
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setLastModifiedBy(login);
        user.setLastModifiedDate(Instant.now());

        Collection<String> dbAuthorities = getAuthorities();
        List<Authority> authorities = new ArrayList<>();
        for (String authority : userDTO.getAuthorities()) {
            if (dbAuthorities.contains(authority)) {
                Authority authorityToSave = new Authority();
                authorityToSave.setName(authority);
                authorities.add(authorityToSave);
            }
        }
        user.setAuthorities(new HashSet<>(authorities));

        Validator validator = buildValidator();
        Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);

        if (CollectionUtils.isEmpty(constraintViolations)) {
            userRepository.save(user);
        }
        this.clearUserCaches(user);

        return userDTO;
    }

    private Validator buildValidator() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        return validatorFactory.getValidator();
    }

    @Transactional
    public ActivatedPayload activateAccount(String key) throws Exception {
        String login = SecurityUtils.getCurrentUserLogin().orElse("System");
        if (Objects.isNull(key)) {
            throw new BadRequestAlertException("User Not Found", "USER", Constants.ID_NOT_NULL);
        }

        Optional<User> existingUser = userRepository.findOneByActivationKey(key);

        User user = existingUser.orElseThrow(() -> new BadRequestAlertException("User not found", "User", Constants.USER_NOT_FOUND));

        this.clearUserCaches(user);

        log.debug("Activated user: {}", user);

        com.okta.sdk.resource.user.User account = oktaService.activateAccount(user.getId());

        user.setLastModifiedBy(login);
        user.setLastModifiedDate(Instant.now());
        user.setActivationKey(null);
        user.setActivated(true);
        userRepository.save(user);
        this.clearUserCaches(user);

        return ActivatedPayload.builder().success(account.getActivated() != null).userId(account.getId()).build();
    }

    public UserDTO getAccount(String userId) {
        if (Objects.isNull(userId)) {
            throw new BadRequestAlertException("UserId can not be null.", "USER", Constants.ID_NOT_NULL);
        }

        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            throw new BadRequestAlertException("User not found", "USER", Constants.USER_NOT_FOUND);
        }

        User user = userOptional.get();

        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setAuthorities(user.getAuthorities().stream().map(Authority::getName).collect(Collectors.toSet()));
        userDTO.setActivated(user.getActivated());
        userDTO.setCreatedBy(user.getCreatedBy());
        userDTO.setEmail(user.getEmail());
        userDTO.setLogin(user.getLogin());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setLangKey(user.getLangKey());
        userDTO.setLastModifiedBy(user.getLastModifiedBy());
        userDTO.setLastModifiedDate(user.getLastModifiedDate());
        userDTO.setCreatedDate(user.getCreatedDate());
        return userDTO;
    }

    @Transactional(readOnly = true)
    public List<String> getAuthorities() {
        return authorityRepository.findAll().stream().map(Authority::getName).collect(Collectors.toList());
    }

    private void clearUserCaches(User user) {
        Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE)).evict(user.getLogin());
        if (user.getEmail() != null) {
            Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE)).evict(user.getEmail());
        }
    }
}

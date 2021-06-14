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
import com.vts.clientcenter.service.dto.*;
import com.vts.clientcenter.service.mapper.UserAddressMapper;
import com.vts.clientcenter.service.mapper.UserProfileMapper;
import com.vts.clientcenter.web.rest.AccountResource;
import com.vts.clientcenter.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.security.RandomUtil;
import io.swagger.models.auth.In;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Autowired
    private UserQueryService userQueryService;

    @Autowired
    private IdentityService identityService;

    @Transactional
    public UserDTO createUserAccount(UserDTO userDTO) throws Exception {

        Optional<User> existingUser = userRepository.findOneByEmailIgnoreCase(userDTO.getEmail());

        if (existingUser.isPresent()) {
            throw new BadRequestAlertException("Account has existed.", "User", Constants.USER_HAS_EXISTED);
        }

        userDTO = identityService.createUser(userDTO);

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


        //add role for users
        Collection<String> dbAuthorities = getAuthorities();

        if (CollectionUtils.isEmpty(userDTO.getAuthorities())) {
            userDTO.getAuthorities().add(AuthoritiesConstants.USER);
        } else {
            Optional<String> userRoleOptional = userDTO
                .getAuthorities()
                .stream()
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

        user.setId(userDTO.getId());

        //TODO publish user to notification
        applicationEventPublisher.publishEvent(new UserCreatedEvent(user));

        //add profile user
        UserProfile userProfile;
        if (Objects.nonNull(userDTO.getUserProfileDto())) {
            UserProfileDTO userProfileDto = userDTO.getUserProfileDto();

            userProfile =
                new UserProfile()
                    .birthDate(userProfileDto.getBirthDate())
                    .createdBy(loginUser)
                    .lastModifiedBy(loginUser)
                    .lastModifiedDate(Instant.now())
                    .createdDate(Instant.now())
                    .user(user)
                    .gender(userProfileDto.getGender())
                    .phone(userDTO.getPhone());
        } else {
            userProfile =
                new UserProfile()
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
        String userLogin = SecurityUtils.getCurrentUserLogin().orElse(Constants.SYSTEM_ACCOUNT);

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
        user.setLastModifiedBy(userLogin);
        user.setLastModifiedDate(Instant.now());

        Set<String> dbUserAuthorities = userDTO.getAuthorities();

        Optional<String> roleUserOptional = dbUserAuthorities
            .stream()
            .filter(roleName -> roleName.equalsIgnoreCase(AuthoritiesConstants.USER))
            .findFirst();

        if (!roleUserOptional.isPresent()) {
            throw new BadRequestAlertException("Role User can not remove.", "RoleUser", Constants.ROLE_NOT_DELETE);
        }

        for (Authority authority : user.getAuthorities()) {
            user.removeAuthority(authority);
        }

        Set<Authority> authorities = authorityRepository.findAllByNameIn(new ArrayList<>(userDTO.getAuthorities()));

        user.setAuthorities(authorities);

        Validator validator = buildValidator();
        Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);

        if (CollectionUtils.isEmpty(constraintViolations)) {
            userRepository.save(user);
        }

        // saving profile
        if (Objects.nonNull(userDTO.getUserProfileDto())) {
            UserProfileDTO userProfileDto = userDTO.getUserProfileDto();

            Optional<UserProfile> userProfileOptional = userProfileRepository.findById(user.getId());

            userProfileOptional.ifPresent(
                userProfile -> {
                    userProfile.setBirthDate(userProfileDto.getBirthDate());
                    userProfile.setGender(userProfileDto.getGender());
                    userProfile.setLastModifiedBy(userLogin);
                    userProfile.setLastModifiedDate(Instant.now());
                    userProfile.setPhone(userProfileDto.getPhone());
                    userProfile.setHomePhone(userProfileDto.getHomePhone());
                    userProfile.setAvatarUrl(userProfileDto.getAvatarUrl());
                    userProfileRepository.save(userProfile);
                }
            );
        }

        //saving address
        if (!CollectionUtils.isEmpty(userDTO.getUserAddressList())) {
            List<UserAddress> dbAddresses = userAddressRepository.findAllByUserId(user.getId());

            Set<UserAddress> updateAddresses = dbAddresses
                .stream()
                .filter(
                    address ->
                        userDTO
                            .getUserAddressList()
                            .stream()
                            .filter(add -> Objects.nonNull(add.getId()))
                            .map(UserAddressDTO::getId)
                            .collect(Collectors.toSet())
                            .contains(address.getId())
                )
                .collect(Collectors.toSet());

            userAddressRepository.deleteAllByIdNotIn(updateAddresses.stream().map(UserAddress::getId).collect(Collectors.toList()));

            List<UserAddress> updateObjects = updateAddresses
                .stream()
                .flatMap(
                    address -> {
                        userDTO
                            .getUserAddressList()
                            .stream()
                            .filter(userAddressDTO -> userAddressDTO.getId().equals(address.getId()))
                            .peek(
                                dto -> {
                                    address.setAddressLine1(dto.getAddressLine1());
                                    address.setAddressLine2(dto.getAddressLine2());
                                    address.setCity(dto.getCity());
                                    address.setCountry(dto.getCountry());
                                    address.setLastModifiedBy(userLogin);
                                    address.setLastModifiedDate(Instant.now());
                                    address.setLatitude(dto.getLatitude());
                                    address.setLongitude(dto.getLongitude());
                                }
                            );

                        return Stream.of(address);
                    }
                )
                .collect(Collectors.toList());

            userAddressRepository.saveAll(updateObjects);

            List<UserAddress> createObjects = userDTO
                .getUserAddressList()
                .stream()
                .filter(userAddressDTO -> Objects.isNull(userAddressDTO.getId()))
                .flatMap(
                    dto -> {
                        dto.setLastModifiedDate(Instant.now());
                        dto.setLastModifiedBy(userLogin);
                        dto.setCreatedBy(userLogin);
                        dto.setCreatedDate(Instant.now());
                        return Stream.of(userAddressMapper.toEntity(dto));
                    }
                )
                .collect(Collectors.toList());

            userAddressRepository.saveAll(createObjects);
        }

        this.clearUserCaches(user);

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
        user.setActivationKey(null);
        user.setActivated(false);
        user.setActivationUrl(account.getActivationUrl());
        user.setActivationKey(account.getActivationToken());
        userRepository.save(user);
        this.clearUserCaches(user);

        return ActivatedPayload.builder().success(true).userId(userId).build();
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

        Optional<UserProfile> userProfileOptional = userProfileRepository.findById(userId);
        userProfileOptional.ifPresent(userProfile -> userDTO.setUserProfileDto(userProfileMapper.toDto(userProfile)));

        List<UserAddress> userAddresses = userAddressRepository.findAllByUserId(userId);
        userDTO.setUserAddressList(userAddressMapper.toDto(userAddresses));

        return userDTO;
    }

    @Transactional(readOnly = true)
    public List<String> getAuthorities() {
        return authorityRepository.findAll().stream().map(Authority::getName).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PagingResponse<UserDTO> getAccounts(UserCriteria criteria, Pageable pageable) {
        PagingResponse<UserDTO> response = new PagingResponse<>();
        Page<UserDTO> userPages = userQueryService.findByCriteria(criteria, pageable);
        response.setTotalPage(userPages.getTotalPages());
        response.setSize(userPages.getSize());
        response.setItems(userPages.getContent());
        return response;
    }

    private void clearUserCaches(User user) {
        Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE)).evict(user.getLogin());
        if (user.getEmail() != null) {
            Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE)).evict(user.getEmail());
        }
    }
}

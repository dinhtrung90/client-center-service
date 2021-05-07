package com.vts.clientcenter.service;

import com.sun.xml.bind.v2.runtime.reflect.opt.Const;
import com.vts.clientcenter.config.Constants;
import com.vts.clientcenter.domain.Authority;
import com.vts.clientcenter.domain.User;
import com.vts.clientcenter.helpers.PasswordGenerator;
import com.vts.clientcenter.kafka.KafkaSender;
import com.vts.clientcenter.repository.AuthorityRepository;
import com.vts.clientcenter.repository.UserRepository;
import com.vts.clientcenter.security.SecurityUtils;
import com.vts.clientcenter.service.dto.ActivatedPayload;
import com.vts.clientcenter.service.dto.UserDTO;
import com.vts.clientcenter.web.rest.errors.BadRequestAlertException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.security.auth.login.AccountNotFoundException;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class AccountService {

    @Autowired
    private OktaService oktaService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private KafkaSender<UserDTO> kafkaSender;

    @Transactional
    public UserDTO createUserAccount(UserDTO userDTO) throws Exception {

        Optional<User> existingUser = userRepository.findOneByEmailIgnoreCase(userDTO.getEmail());

        if (existingUser.isPresent()) {
            throw new BadRequestAlertException("Account has existed.", "User", Constants.USER_HAS_EXISTED);
        }

        String password = PasswordGenerator.generateRandomPassword(8);

        com.okta.sdk.resource.user.User oktaUser = oktaService.createOktaAccount(userDTO, password);

        String login = SecurityUtils.getCurrentUserLogin().orElse("System");


        User user = new User();
        user.setId(oktaUser.getId());
        user.setLangKey(Constants.DEFAULT_LANGUAGE);
        user.setLogin(userDTO.getEmail());
        user.setActivated(false);
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setImageUrl(userDTO.getImageUrl());
        user.setCreatedDate(Instant.now());
        user.setLastModifiedBy(login);
        user.setLastModifiedDate(Instant.now());
        user.setCreatedBy(login);
        user.setPhone(userDTO.getPhone());

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

        userRepository.save(user);

        userDTO.setId(user.getId());

        kafkaSender.sendCustomMessage(userDTO, Constants.TOPIC_CREATE_USER_ACCOUNT_MAIL);

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
        user.setImageUrl(userDTO.getImageUrl());
        user.setLastModifiedBy(login);
        user.setLastModifiedDate(Instant.now());
        user.setPhone(userDTO.getPhone());

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

        userRepository.save(user);

        this.clearUserCaches(user);

        return userDTO;
    }

    @Transactional
    public ActivatedPayload activateAccount(UserDTO userDTO) throws Exception {
        String login = SecurityUtils.getCurrentUserLogin().orElse("System");
        if (Objects.isNull(userDTO.getId())) {
            throw new BadRequestAlertException("User Not Found", "USER", Constants.ID_NOT_NULL);
        }
        com.okta.sdk.resource.user.User account = oktaService.activateAccount(userDTO);

        Optional<User> existingUser = userRepository.findOneByLogin(userDTO.getLogin());

        User user = existingUser.orElseThrow( () -> new BadRequestAlertException("User not found", "User", Constants.USER_NOT_FOUND));

        user.setLastModifiedBy(login);
        user.setLastModifiedDate(Instant.now());
        user.setActivated(account.getActivated() != null);
        userRepository.save(user);
        this.clearUserCaches(user);

        return ActivatedPayload.builder()
            .success(account.getActivated() != null)
            .userId(account.getId())
            .build();

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
        userDTO.setImageUrl(user.getImageUrl());
        userDTO.setLangKey(user.getLangKey());
        userDTO.setLastModifiedBy(user.getLastModifiedBy());
        userDTO.setLastModifiedDate(user.getLastModifiedDate());
        userDTO.setCreatedDate(user.getCreatedDate());
        return  userDTO;
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

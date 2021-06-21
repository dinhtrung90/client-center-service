package com.vts.clientcenter.service.impl;

import com.vts.clientcenter.domain.Authority;
import com.vts.clientcenter.repository.AuthorityRepository;
import com.vts.clientcenter.security.SecurityUtils;
import com.vts.clientcenter.service.AbstractBaseService;
import com.vts.clientcenter.service.AuthorityService;
import com.vts.clientcenter.service.UserService;
import com.vts.clientcenter.service.dto.AuthorityDto;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.vts.clientcenter.config.Constants.SYSTEM_ACCOUNT;

@Service
@Transactional
public class AuthorityServiceImpl extends AbstractBaseService implements AuthorityService {


    @Autowired
    private AuthorityRepository authorityRepository;

    public AuthorityServiceImpl(UserService userService) {
        super(userService);
    }

    @Override
    public List<AuthorityDto> getAuthorities() {
        return authorityRepository
            .findAll()
            .stream()
            .map(
                r ->
                    AuthorityDto
                        .builder()
                        .name(r.getName())
                        .description(r.getDescription())
                        .createdDate(r.getCreatedDate())
                        .lastModifiedDate(r.getLastModifiedDate())
                        .build()
            )
            .collect(Collectors.toList());
    }

    @Override
    public AuthorityDto save(AuthorityDto dto) {
        String currentUserLogin = SecurityUtils.getCurrentUserLogin().orElse(SYSTEM_ACCOUNT);

        Optional<Authority> authorityOptional = authorityRepository.findById(dto.getName());

        Authority authority = null;

        if (!authorityOptional.isPresent()) {

            authority = new Authority();
            authority.setName(dto.getName());
            authority.setDescription(dto.getDescription());
            authority.setCreatedBy(currentUserLogin);
            authority.setLastModifiedBy(currentUserLogin);
            authority.setLastModifiedDate(Instant.now());
            keycloakFacade.createRole(authority, setting.getRealmApp());
        } else {
            authority = authorityOptional.get();
            authority.setDescription(dto.getDescription());
            authority.setLastModifiedBy(currentUserLogin);
            authority.setLastModifiedDate(Instant.now());
            keycloakFacade.updateRole(authority.getName(), setting.getRealmApp(), authority);
        }
        authorityRepository.save(authority);

        return AuthorityDto
            .builder()
            .lastModifiedDate(authority.getLastModifiedDate())
            .createdDate(authority.getCreatedDate())
            .name(authority.getName())
            .description(authority.getDescription())
            .build();
    }
}

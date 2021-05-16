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
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                        .roleName(r.getName())
                        .description(r.getDescription())
                        .createDate(r.getCreatedDate())
                        .modifiedDate(r.getLastModifiedDate())
                        .build()
            )
            .collect(Collectors.toList());
    }

    @Override
    public AuthorityDto save(AuthorityDto dto) {
        Optional<String> currentUserLogin = SecurityUtils.getCurrentUserLogin();

        Optional<Authority> authorityOptional = authorityRepository.findById(dto.getRoleName());

        if (!authorityOptional.isPresent()) {
            Authority authority = new Authority();
            authority.setName(dto.getRoleName());
            authority.setDescription(dto.getDescription());
            authority.setCreatedBy(currentUserLogin.get());
            authority.setLastModifiedBy(currentUserLogin.get());
            authority.setLastModifiedDate(Instant.now());
        }

        Authority authority = authorityOptional.get();
        authority.setDescription(dto.getDescription());
        authority.setLastModifiedBy(currentUserLogin.get());
        authority.setLastModifiedDate(Instant.now());

        authorityRepository.save(authority);

        return AuthorityDto
            .builder()
            .modifiedDate(authority.getLastModifiedDate())
            .createDate(authority.getCreatedDate())
            .roleName(authority.getName())
            .description(authority.getDescription())
            .build();
    }
}

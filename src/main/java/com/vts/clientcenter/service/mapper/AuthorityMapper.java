package com.vts.clientcenter.service.mapper;

import com.vts.clientcenter.domain.Authority;
import com.vts.clientcenter.service.dto.AuthorityDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class AuthorityMapper {

    public AuthorityDto authorityToDto(Authority authority) {
        if (Objects.isNull(authority)) {
            return null;
        }
        return AuthorityDto
            .builder()
            .name(authority.getName())
            .description(authority.getDescription())
            .createdBy(authority.getCreatedBy())
            .lastModifiedBy(authority.getLastModifiedBy())
            .lastModifiedDate(authority.getLastModifiedDate())
            .createdDate(authority.getCreatedDate())
            .build();
    }

    public Authority dtoToAuthority(AuthorityDto dto) {
        if (Objects.isNull(dto)) {
            return null;
        }
        Authority authority = new Authority();
        authority.setCreatedDate(dto.getCreatedDate());
        authority.setCreatedBy(dto.getCreatedBy());
        authority.setLastModifiedDate(dto.getLastModifiedDate());
        authority.setDescription(dto.getDescription());
        authority.setName(dto.getName());
        authority.setLastModifiedBy(dto.getLastModifiedBy());

        return authority;
    }

    public List<AuthorityDto> authorityToDtos(List<Authority> authorities) {
        return authorities.stream().filter(Objects::nonNull).map(this::authorityToDto).collect(Collectors.toList());
    }

    public List<Authority> dtoToAuthorities(List<AuthorityDto> authorities) {
        return authorities.stream().filter(Objects::nonNull).map(this::dtoToAuthority).collect(Collectors.toList());
    }
}

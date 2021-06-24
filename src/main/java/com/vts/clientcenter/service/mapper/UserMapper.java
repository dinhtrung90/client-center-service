package com.vts.clientcenter.service.mapper;
import java.util.*;
import java.util.stream.Collectors;

import com.vts.clientcenter.domain.Authority;
import com.vts.clientcenter.domain.User;
import com.vts.clientcenter.service.dto.AuthorityDto;
import com.vts.clientcenter.service.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Mapper for the entity {@link User} and its DTO called {@link UserDTO}.
 *
 * Normal mappers are generated using MapStruct, this one is hand-coded as MapStruct
 * support is still in beta, and requires a manual step with an IDE.
 */
@Service
public class UserMapper {
    @Autowired
    private AuthorityMapper authorityMapper;

    public List<UserDTO> usersToUserDTOs(List<User> users) {
        return users.stream().filter(Objects::nonNull).map(this::userToUserDTO).collect(Collectors.toList());
    }

    public UserDTO userToUserDTO(User user) {
        return userToDto(user);
    }

    public List<User> userDTOsToUsers(List<UserDTO> userDTOs) {
        return userDTOs.stream().filter(Objects::nonNull).map(this::userDTOToUser).collect(Collectors.toList());
    }

    public UserDTO userToDto(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setLogin(user.getLogin());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setActivated(user.getActivated());
        dto.setLangKey(user.getLangKey());
        dto.setCreatedBy(user.getCreatedBy());
        dto.setCreatedDate(user.getCreatedDate());
        dto.setLastModifiedBy(user.getLastModifiedBy());
        dto.setLastModifiedDate(user.getLastModifiedDate());
        dto.setEnabled(user.hasEnabled());
        dto.setVerifiedEmail(user.hasVerifiedEmail());
        dto.setAccountStatus(user.getAccountStatus());
        ArrayList<Authority> authorities = new ArrayList<>(user.getAuthorities());
        List<AuthorityDto> authorityDtos = authorityMapper.authorityToDtos(authorities);
        dto.setAuthorities(new HashSet<>(authorityDtos));
        return dto;
    }

    public User userDTOToUser(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        } else {
            User user = new User();
            user.setId(userDTO.getId());
            user.setLogin(userDTO.getLogin());
            user.setFirstName(userDTO.getFirstName());
            user.setLastName(userDTO.getLastName());
            user.setEmail(userDTO.getEmail());
            user.setActivated(userDTO.isActivated());
            user.setLangKey(userDTO.getLangKey());
            Set<Authority> authorities = new HashSet<>(authorityMapper.dtoToAuthorities(new ArrayList<>(userDTO.getAuthorities())));
            user.setAuthorities(authorities);
            user.setHasEnabled(userDTO.isEnabled());
            user.setHasVerifiedEmail(userDTO.isVerifiedEmail());
            user.setAccountStatus(userDTO.getAccountStatus());
            return user;
        }
    }

    private Set<Authority> authoritiesFromStrings(Set<String> authoritiesAsString) {
        Set<Authority> authorities = new HashSet<>();

        if (authoritiesAsString != null) {
            authorities =
                authoritiesAsString
                    .stream()
                    .map(
                        string -> {
                            Authority auth = new Authority();
                            auth.setName(string);
                            return auth;
                        }
                    )
                    .collect(Collectors.toSet());
        }

        return authorities;
    }

    public User userFromId(String id) {
        if (id == null) {
            return null;
        }
        User user = new User();
        user.setId(id);
        return user;
    }
}

package com.vts.clientcenter.security.oauth2;

import com.vts.clientcenter.repository.UserRepository;
import com.vts.clientcenter.security.SecurityUtils;
import java.util.Collection;
import java.util.List;

import com.vts.clientcenter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class JwtGrantedAuthorityConverter implements Converter<Jwt, Collection<GrantedAuthority>> {


    public JwtGrantedAuthorityConverter() {
        // Bean extracting authority.
    }

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {


        List<GrantedAuthority> grantedAuthorities = SecurityUtils.extractAuthorityFromClaims(jwt.getClaims());

        return grantedAuthorities;
    }
}

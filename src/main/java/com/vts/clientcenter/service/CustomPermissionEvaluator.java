package com.vts.clientcenter.service;

import com.vts.clientcenter.domain.Authority;
import com.vts.clientcenter.domain.Permission;
import com.vts.clientcenter.repository.AuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomPermissionEvaluator implements PermissionEvaluator {

    @Autowired
    AuthorityRepository authorityRepository;

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if ((authentication == null) || (targetDomainObject == null) || !(permission instanceof String)) {
            return false;
        }

        return hasPrivilege(authentication, targetDomainObject.toString().toUpperCase(), permission.toString().toUpperCase());
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {

        if ((authentication == null) || (targetType == null) || !(permission instanceof String)) {
            return false;
        }

        return hasPrivilege(authentication, targetType.toLowerCase(), ((String) permission).toUpperCase());
    }

    private boolean hasPrivilege(Authentication auth, String targetType, String permission) {
        List<String> authorities = auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());

        long count = authorityRepository.getAllByNameIn(authorities)
            .stream()
            .flatMap(r -> {
                Set<Permission> permissions = r.getPermissions();
                return permissions.stream();
            })
            .filter(r -> r.getName().contains("ROLE_PERMISSION_" + targetType.toUpperCase() + "_" + permission))
            .count();

        return count > 0 ;
    }
}

package com.vts.clientcenter.security;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.util.CollectionUtils;

/**
 * Utility class for Spring Security.
 */
public final class SecurityUtils {

    @Value("${app.keycloak.clientId}")
    private static String clientId;

    private SecurityUtils() {}

    /**
     * Get the login of the current user.
     *
     * @return the login of the current user.
     */
    public static Optional<String> getCurrentUserLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(extractPrincipal(securityContext.getAuthentication()));
    }

    private static String extractPrincipal(Authentication authentication) {
        if (authentication == null) {
            return null;
        } else if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
            return springSecurityUser.getUsername();
        } else if (authentication instanceof JwtAuthenticationToken) {
            return (String) ((JwtAuthenticationToken) authentication).getToken().getClaims().get("email");
        } else if (authentication.getPrincipal() instanceof DefaultOidcUser) {
            Map<String, Object> attributes = ((DefaultOidcUser) authentication.getPrincipal()).getAttributes();
            if (attributes.containsKey("preferred_username")) {
                return (String) attributes.get("preferred_username");
            }
        } else if (authentication.getPrincipal() instanceof String) {
            return (String) authentication.getPrincipal();
        }
        return null;
    }

    /**
     * Check if a user is authenticated.
     *
     * @return true if the user is authenticated, false otherwise.
     */
    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && getAuthorities(authentication).noneMatch(AuthoritiesConstants.ANONYMOUS::equals);
    }

    /**
     * If the current user has a specific authority (security role).
     * <p>
     * The name of this method comes from the {@code isUserInRole()} method in the Servlet API.
     *
     * @param authority the authority to check.
     * @return true if the current user has the authority, false otherwise.
     */
    public static boolean isCurrentUserInRole(String authority) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && getAuthorities(authentication).anyMatch(authority::equals);
    }

    private static Stream<String> getAuthorities(Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities = authentication instanceof JwtAuthenticationToken
            ? extractAuthorityFromClaims(((JwtAuthenticationToken) authentication).getToken().getClaims())
            : authentication.getAuthorities();
        return authorities.stream().map(GrantedAuthority::getAuthority);
    }

    public static List<GrantedAuthority> extractAuthorityFromClaims(Map<String, Object> claims) {
        return mapKeycloakRolesToAuthorities(getRealmRolesFrom(claims), getClientRolesFrom(claims, clientId));
    }

    @SuppressWarnings("unchecked")
//    private static Collection<String> getRolesFromClaims(Map<String, Object> claims) {
//        return (Collection<String>) claims.getOrDefault("groups", claims.getOrDefault("roles", new ArrayList<>()));
//    }
//
//    private static List<GrantedAuthority> mapRolesToGrantedAuthorities(Collection<String> roles) {
//        return roles.stream().filter(role -> role.startsWith("ROLE_")).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
//    }


    protected static List<GrantedAuthority>  mapKeycloakRolesToAuthorities(Set<String> realmRoles,
                                                                           Set<String> clientRoles) {
        List<GrantedAuthority> combinedAuthorities = new ArrayList<>();

        List<SimpleGrantedAuthority> authorities = realmRoles.stream().filter(role -> role.startsWith("ROLE_")).map(SimpleGrantedAuthority::new).collect(Collectors.toList());

        combinedAuthorities.addAll(authorities);

        List<SimpleGrantedAuthority> clientAuthorities = clientRoles.stream().filter(role -> role.startsWith("ROLE_")).map(SimpleGrantedAuthority::new).collect(Collectors.toList());

        combinedAuthorities.addAll(clientAuthorities);

        return combinedAuthorities;
    }

    protected static Set<String> getRealmRolesFrom( Map<String, Object> claims) {

        Map<String, Object> realmAccess = (Map<String, Object>) claims.get("realm_access");

        if (CollectionUtils.isEmpty(realmAccess)) {
            return Collections.emptySet();
        }

        Collection<String> realmRoles = (Collection<String>) realmAccess.get("roles");
        if (CollectionUtils.isEmpty(realmRoles)) {
            return Collections.emptySet();
        }

        return realmRoles.stream().map(SecurityUtils::normalizeRole).collect(Collectors.toSet());
    }

    protected static Set<String> getClientRolesFrom(Map<String, Object> claims, String clientId) {

        Map<String, Object> resourceAccess =( Map<String, Object>) claims.get("resource_access");

        if (CollectionUtils.isEmpty(resourceAccess)) {
            return Collections.emptySet();
        }

        Map<String, List<String>> clientAccess = (Map<String, List<String>>) resourceAccess.get(clientId);
        if (CollectionUtils.isEmpty(clientAccess)) {
            return Collections.emptySet();
        }

        List<String> clientRoles = clientAccess.get("roles");
        if (CollectionUtils.isEmpty(clientRoles)) {
            return Collections.emptySet();
        }

        return clientRoles.stream().map(SecurityUtils::normalizeRole).collect(Collectors.toSet());
    }

    private static String normalizeRole(String role) {
        return role.replace('-', '_');
    }
}

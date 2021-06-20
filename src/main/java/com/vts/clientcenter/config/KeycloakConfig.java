package com.vts.clientcenter.config;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Builder
@Configuration
@NoArgsConstructor
@AllArgsConstructor
public class KeycloakConfig {
    @Value("${app.keycloak.authorizeUrl}")
    private String serverUrl;

    @Value("${app.keycloak.clientId}")
    private String clientId;

    @Value("${app.keycloak.clientSecret}")
    private String clientSecret;

    @Value("${app.keycloak.realmName}")
    private String realmName;

    @Value("${app.keycloak.connectionPoolSize}")
    private Integer connectionPoolSize;

    @Value("${app.keycloak.grantType}")
    private String grantType;

    @Value("${app.keycloak.admin.username}")
    private String username;

    @Value("${app.keycloak.admin.password}")
    private String password;

    @Value("${app.keycloak.realmApp}")
    private String realmApp;
}

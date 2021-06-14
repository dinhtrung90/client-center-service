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
    private String authorizeUrl;

    @Value("${app.keycloak.clientId}")
    private String clientId;

    @Value("${app.keycloak.clientSecret}")
    private String clientSecret;

    @Value("${app.keycloak.realmName}")
    private String realmName;

}

package com.vts.clientcenter.config;

import com.vts.clientcenter.service.keycloak.DefaultKeycloakFacade;
import com.vts.clientcenter.service.keycloak.KeycloakFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakClientConfig {

    @Autowired
    private KeycloakConfig setting;

    @Bean
    KeycloakFacade keycloakFacade() {
        return DefaultKeycloakFacade
            .builder()
            .setServerUrl(setting.getServerUrl()) //
            .setRealmId(setting.getRealmName()) //
            .setClientId(setting.getClientId()) //
            .setClientSecret(setting.getClientSecret()) //
            .setUsername(setting.getUsername())
            .setPassword(setting.getPassword())
            .build();
    }
}

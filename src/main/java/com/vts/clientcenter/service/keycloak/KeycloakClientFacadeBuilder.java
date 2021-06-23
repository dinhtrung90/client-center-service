package com.vts.clientcenter.service.keycloak;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.token.TokenService;

public class KeycloakClientFacadeBuilder {
    private String serverUrl;

    private String realmId;

    private String clientId;

    private String clientSecret;

    private String username;

    private String password;

    private ResteasyClient resteasyClient;


    public KeycloakFacade build() {
        KeycloakBuilder builder = username == null
            ? newKeycloakFromClientCredentials()
            : newKeycloakFromPasswordCredentials(username, password);

        if (resteasyClient != null) {
            builder = builder.resteasyClient(resteasyClient);
        }

        return new DefaultKeycloakFacade(builder.build());
    }

    private KeycloakBuilder newKeycloakFromClientCredentials() {
        return KeycloakBuilder
            .builder() //
            .realm(realmId) //
            .serverUrl(serverUrl) //
            .clientId(clientId) //
            .clientSecret(clientSecret) //
            .grantType(OAuth2Constants.CLIENT_CREDENTIALS);
    }

    private KeycloakBuilder newKeycloakFromPasswordCredentials(String username, String password) {
        return newKeycloakFromClientCredentials() //
            .username(username) //
            .password(password) //
            .grantType(OAuth2Constants.PASSWORD);
    }

    public KeycloakClientFacadeBuilder setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
        return this;
    }

    public KeycloakClientFacadeBuilder setRealmId(String realmId) {
        this.realmId = realmId;
        return this;
    }

    public KeycloakClientFacadeBuilder setClientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public KeycloakClientFacadeBuilder setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
        return this;
    }

    public KeycloakClientFacadeBuilder setUsername(String username) {
        this.username = username;
        return this;
    }

    public KeycloakClientFacadeBuilder setPassword(String password) {
        this.password = password;
        return this;
    }

    public KeycloakClientFacadeBuilder setResteasyClient(ResteasyClient resteasyClient) {
        this.resteasyClient = resteasyClient;
        return this;
    }

}

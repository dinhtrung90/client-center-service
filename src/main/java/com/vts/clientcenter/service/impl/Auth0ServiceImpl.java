package com.vts.clientcenter.service.impl;

import com.auth0.client.auth.AuthAPI;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.auth.TokenHolder;
import com.auth0.net.AuthRequest;
import com.vts.clientcenter.service.Auth0Service;
import com.vts.clientcenter.service.dto.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

@Service
@Transactional
public class Auth0ServiceImpl implements Auth0Service {


    private AuthAPI auth;

    private ClientRegistration registration;

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

    private String redirectUri;


    private String  audience;

    @PostConstruct
    void init() {
        registration = clientRegistrationRepository.findByRegistrationId("oidc");

        String issuer = (String) this.registration.getProviderDetails().getConfigurationMetadata().get("issuer");

        auth = new AuthAPI(issuer, registration.getClientId(), registration.getClientSecret());


    }

    @Override
    public void createGroup() {

    }

    @Override
    public void createRole() {

    }

    @Override
    public LoginResponse login(String username, String password) {

        AuthRequest request = auth.login(username, password)
            .setAudience(audience)
            .setScope("openid profile");
        try {
            TokenHolder holder = request.execute();

            return LoginResponse.builder()
                .idToken(holder.getIdToken())
                .accessToken(holder.getAccessToken())
                .build();

        } catch (Auth0Exception exception) {
            // api error
            exception.printStackTrace();
        }

        return null;
    }
}

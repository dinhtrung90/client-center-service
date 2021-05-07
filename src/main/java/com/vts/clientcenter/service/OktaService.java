package com.vts.clientcenter.service;

import com.okta.sdk.authc.credentials.TokenClientCredentials;
import com.okta.sdk.client.Client;
import com.okta.sdk.client.Clients;
import com.okta.sdk.resource.user.User;
import com.okta.sdk.resource.user.UserBuilder;
import com.vts.clientcenter.config.OktaConfig;
import com.vts.clientcenter.service.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;


@Service
public class OktaService {

    @Autowired
    private OktaConfig oktaConfig;

    private Client client;

    @PostConstruct
    void init(){
        client = Clients.builder()
            .setClientId(oktaConfig.getClientId())
            .setOrgUrl(oktaConfig.getDomain())
            .setClientCredentials(new TokenClientCredentials(oktaConfig.getApiToken()))
            .build();
    }

    public User createOktaAccount(UserDTO userDTO, String tempPassword) throws Exception {

        return UserBuilder.instance()
            .setEmail(userDTO.getEmail())
            .setFirstName("Joe")
            .setLastName("Coder")
            .setPassword(tempPassword.toCharArray())
            .setGroups(userDTO.getAuthorities())
            .setActive(false)
            .buildAndCreate(client);
    }

    public User updateAccount(UserDTO userDTO) throws Exception {
        User user = client.getUser(userDTO.getId());
        user.getProfile().setEmail(userDTO.getEmail());
        user.getProfile().setFirstName(userDTO.getFirstName());
        user.getProfile().setLastName(userDTO.getLastName());
        user.update();
        return user;
    }


    public User activateAccount(UserDTO userDTO) throws Exception {
        User user = client.getUser(userDTO.getId());
        user.activate(true);
        return user;
    }

}

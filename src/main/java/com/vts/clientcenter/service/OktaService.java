package com.vts.clientcenter.service;

import com.okta.sdk.authc.credentials.TokenClientCredentials;
import com.okta.sdk.client.Client;
import com.okta.sdk.client.Clients;
import com.okta.sdk.resource.group.Group;
import com.okta.sdk.resource.group.GroupBuilder;
import com.okta.sdk.resource.group.GroupList;
import com.okta.sdk.resource.group.GroupProfile;
import com.okta.sdk.resource.user.User;
import com.okta.sdk.resource.user.UserBuilder;
import com.vts.clientcenter.config.Constants;
import com.vts.clientcenter.config.OktaConfig;
import com.vts.clientcenter.service.dto.UserDTO;
import com.vts.clientcenter.web.rest.errors.BadRequestAlertException;
import java.util.List;
import java.util.Set;
import java.util.prefs.BackingStoreException;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OktaService {
    @Autowired
    private OktaConfig oktaConfig;

    private Client client;

    @PostConstruct
    void init() {
        client =
            Clients
                .builder()
                .setClientId(oktaConfig.getClientId())
                .setOrgUrl(oktaConfig.getDomain())
                .setClientCredentials(new TokenClientCredentials(oktaConfig.getApiToken()))
                .build();
    }

    public User createOktaAccount(UserDTO userDTO, String tempPassword) throws Exception {
        GroupList groups = client.listGroups();

        List<GroupProfile> groupProfiles = groups.stream().map(Group::getProfile).collect(Collectors.toList());

        List<String> oktaRoles = groupProfiles.stream().map(GroupProfile::getName).collect(Collectors.toList());

        boolean allMatch = oktaRoles.containsAll(userDTO.getAuthorities());

        if (!allMatch) {
            throw new BadRequestAlertException("Roles not match.", "Users", Constants.ROLE_NOT_MATCH);
        }

        Set<String> groupIds = groups
            .stream()
            .filter(group -> userDTO.getAuthorities().contains(group.getProfile().getName()))
            .map(Group::getId)
            .collect(Collectors.toSet());

        User user = UserBuilder
            .instance()
            .setEmail(userDTO.getEmail())
            .setFirstName(userDTO.getFirstName())
            .setLastName(userDTO.getLastName())
            .setPassword(tempPassword.toCharArray())
            .setActive(false)
            .buildAndCreate(client);

        for (String groupId : groupIds) {
            user.addToGroup(groupId);
        }

        return user;
    }

    public User updateAccount(UserDTO userDTO) throws Exception {
        User user = client.getUser(userDTO.getId());
        user.getProfile().setEmail(userDTO.getEmail());
        user.getProfile().setFirstName(userDTO.getFirstName());
        user.getProfile().setLastName(userDTO.getLastName());
        user.update();
        return user;
    }

    public User activateAccount(String userId) throws Exception {
        User userDb = client.getUser(userId);
        userDb.activate(true);
        return userDb;
    }

    public void removeAccount(String userId) {
        User user = client.getUser(userId);
        user.deactivate();
        user.delete();
    }


    public Group createGroup(String groupName, String description) {
        return GroupBuilder.instance()
            .setName(groupName)
            .setDescription(description)
            .buildAndCreate(client);
    }

    public void removeGroup(Group group) {
        group.delete();
    }
}

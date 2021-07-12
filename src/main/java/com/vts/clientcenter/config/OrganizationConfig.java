package com.vts.clientcenter.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationConfig {

    @Value("${app.keycloak.client.lifespan}")
    private String lifeSpan;
    @Value("${app.keycloak.client.browser.id}")
    private String browserId;

    public Map<String, Boolean> getAccessAttributes() {
        Map<String, Boolean> accessAttributes = new HashMap<>();
        accessAttributes.put("configure", true);
        accessAttributes.put("manage", true);
        accessAttributes.put("view", true);
        return accessAttributes;
    }

    public Map<String, String> getAuthenticationFlowBindingOverrides() {
        Map<String, String> authenticationAttributes =  new HashMap<>();
        authenticationAttributes.put("browser", browserId);
        return authenticationAttributes;
    }

    public Map<String, String> getCommonAttributes() {
        Map<String, String> attributes =  new HashMap<>();
        attributes.put("access.token.lifespan", lifeSpan);
        attributes.put("use.refresh.tokens", "true");
        return attributes;
    }

    public List<String> getDefaultClientScopes() {
        return Arrays.asList("web-origins", "roles", "profile", "email");
    }
}

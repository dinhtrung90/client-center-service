package com.vts.clientcenter.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
public class OktaConfig {
    @Value("${okta.client-id}")
    private String clientId;

    @Value("${okta.domain}")
    private String domain;

    @Value("${okta.api-token}")
    private String apiToken;
}

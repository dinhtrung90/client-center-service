package com.vts.clientcenter.config;

import com.vts.clientcenter.security.SpringSecurityAuditorAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(auditorAwareRef ="springSecurityAuditorAware")
public class AuditingConfiguration {

    @Bean
    public AuditorAware<String> springSecurityAuditorAware() {
        return new SpringSecurityAuditorAware();
    }
}

package com.vts.clientcenter.events.listeners;

import com.vts.clientcenter.config.Constants;
import com.vts.clientcenter.domain.User;
import com.vts.clientcenter.events.OrganizationCreatedEvent;
import com.vts.clientcenter.events.UserCreatedEvent;
import com.vts.clientcenter.events.UserCreatedEventListener;
import com.vts.clientcenter.kafka.messages.MessageType;
import com.vts.clientcenter.kafka.messages.UserCreatedMessage;
import com.vts.clientcenter.service.OrganizationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Objects;

@Component
public class OrganizationListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrganizationListener.class);

    @Value("${app.kafka.topic.notification.internal.client-center}")
    private String internalSystemNotification;

    @Autowired
    private OrganizationService organizationService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void processUserCreatedEvent(OrganizationCreatedEvent event) {
        LOGGER.info("Event received: " + event);
        if (Objects.isNull(event.getOrganization())) {
            return;
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void processUserCreatedEventAfterRollBack(OrganizationCreatedEvent event) {
        LOGGER.info("Event received: " + event);
        if (Objects.nonNull(event.getOrganization()) && Objects.nonNull(event.getOrganization().getId())) {
            organizationService.removeOrganizationFromKeycloak(event.getOrganization().getId(), event.getOrganization().getName());
        }
    }
}

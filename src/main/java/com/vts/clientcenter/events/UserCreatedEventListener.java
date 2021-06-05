package com.vts.clientcenter.events;

import com.okta.sdk.resource.group.Group;
import com.vts.clientcenter.config.Constants;
import com.vts.clientcenter.domain.User;
import com.vts.clientcenter.kafka.KafkaSender;
import com.vts.clientcenter.kafka.messages.MessageType;
import com.vts.clientcenter.kafka.messages.UserCreatedMessage;
import com.vts.clientcenter.service.OktaService;
import com.vts.clientcenter.service.dto.UserDTO;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class UserCreatedEventListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserCreatedEventListener.class);

    @Autowired
    private KafkaSender<UserCreatedMessage> kafkaSender;

    @Autowired
    private OktaService oktaService;

    @Value("${cloudkarafka.email-invitation}")
    private String emailTopic;

    @Value("${app.kafka.topic.notification.internal.client-center}")
    private String internalSystemNotification;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void processUserCreatedEvent(UserCreatedEvent event) {
        LOGGER.info("Event received: " + event);
        if (Objects.isNull(event.getUser())) {
           return;
        }

        User user = event.getUser();

        UserCreatedMessage message = UserCreatedMessage.builder()
            .userId(user.getId())
            .email(user.getEmail())
            .message(Constants.ACCOUNT_CREATION_MESSAGE)
            .type(MessageType.ACCOUNT_CREATION)
            .build();

        kafkaSender.sendMessageWithCallback(message, internalSystemNotification);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void processUserCreatedEventAfterRollBack(UserCreatedEvent event) {
        LOGGER.info("Event received: " + event);
        if (Objects.nonNull(event.getUser()) && Objects.nonNull(event.getUser().getId())) {
            oktaService.removeAccount(event.getUser().getId());
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void processGroupCreatedEventAfterRollBack(GroupCreationEvent event) {
        LOGGER.info("Event received: " + event);
        if (Objects.nonNull(event.getGroup())) {
//            oktaService.removeGroup(event.getGroup());
        }
    }

}

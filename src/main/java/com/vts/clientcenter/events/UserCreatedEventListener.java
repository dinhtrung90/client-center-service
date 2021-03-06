package com.vts.clientcenter.events;

import com.vts.clientcenter.kafka.Producer;
import com.vts.clientcenter.service.OktaService;
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
    private Producer kafkaSender;

    @Autowired
    private OktaService oktaService;

    @Value("${cloudkarafka.email-invitation}")
    private String emailTopic;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void processUserCreatedEvent(UserCreatedEvent event) {
        LOGGER.info("Event received: " + event);
        if (Objects.nonNull(event.getUser())) {
            kafkaSender.sendMessage(event.getUser(), emailTopic);
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void processUserCreatedEventAfterRollBack(UserCreatedEvent event) {
        LOGGER.info("Event received: " + event);
        if (Objects.nonNull(event.getUser()) && Objects.nonNull(event.getUser().getId())) {
            oktaService.removeAccount(event.getUser().getId());
        }
    }
}

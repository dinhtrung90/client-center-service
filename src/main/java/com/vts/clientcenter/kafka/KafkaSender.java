package com.vts.clientcenter.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Component
public class KafkaSender<T> {
    private final Logger log = LoggerFactory.getLogger(KafkaSender.class);

    private final KafkaTemplate<String, T> kafkaTemplate;

    @Autowired
    public KafkaSender(KafkaTemplate<String, T> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendCustomMessage(T payload, String topicName) {
        log.info("Sending Json Serializer : {}", payload);
        log.info("--------------------------------");
        kafkaTemplate.send(topicName, payload);
    }

    public void sendMessageWithCallback(T message, String topicName) {
        log.info("Sending : {}", message);
        log.info("---------------------------------");

        ListenableFuture<SendResult<String, T>> future = kafkaTemplate.send(topicName, message);
        future.addCallback(
            new ListenableFutureCallback<SendResult<String, T>>() {

                @Override
                public void onFailure(Throwable ex) {
                    log.warn("Failure Callback: Unable to deliver message [{}]. {}", message, ex.getMessage());
                }

                @Override
                public void onSuccess(SendResult<String, T> result) {
                    log.info("Success Callback: [{}] delivered with offset -{}", message, result.getRecordMetadata().offset());
                }
            }
        );
    }
}

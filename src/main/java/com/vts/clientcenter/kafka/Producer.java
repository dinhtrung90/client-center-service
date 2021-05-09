package com.vts.clientcenter.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Component
public class Producer {
    private final Logger log = LoggerFactory.getLogger(Producer.class);

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final KafkaTemplate<String, Object> senderObject;

    @Value("${cloudkarafka.topic}")
    private String topic;

    public Producer(KafkaTemplate<String, String> kafkaTemplate, KafkaTemplate<String, Object> senderObject) {
        this.kafkaTemplate = kafkaTemplate;
        this.senderObject = senderObject;
    }

    public void sendMessage(String message) {
        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, message);

        future.addCallback(
            new ListenableFutureCallback<SendResult<String, String>>() {

                @Override
                public void onSuccess(SendResult<String, String> result) {
                    System.out.println("Sent message=[" + message + "] with offset=[" + result.getRecordMetadata().offset() + "]");
                }

                @Override
                public void onFailure(Throwable ex) {
                    System.out.println("Unable to send message=[" + message + "] due to : " + ex.getMessage());
                }
            }
        );
    }

    public void sendMessage(Object message, String topicName) {
        ListenableFuture<SendResult<String, Object>> future = senderObject.send(topicName, message);

        future.addCallback(
            new ListenableFutureCallback<SendResult<String, Object>>() {

                @Override
                public void onSuccess(SendResult<String, Object> result) {
                    System.out.println("Sent message=[" + message + "] with offset=[" + result.getRecordMetadata().offset() + "]");
                }

                @Override
                public void onFailure(Throwable ex) {
                    System.out.println("Unable to send message=[" + message + "] due to : " + ex.getMessage());
                }
            }
        );
    }
}
